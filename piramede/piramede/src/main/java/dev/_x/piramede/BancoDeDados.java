package dev._x.piramede;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BancoDeDados {

    private static final String URL_CONEXAO = "jdbc:sqlite:banco_v4.db";

    public static void inicializarBanco() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS contas (
                        numero      TEXT PRIMARY KEY,
                        senha       TEXT NOT NULL,
                        saldo       REAL,
                        nome        TEXT NOT NULL,
                        endereco    TEXT,
                        nascimento  TEXT,
                        fiador      TEXT,
                        limite      REAL DEFAULT 0.0,
                        cpf         TEXT DEFAULT ''
                    )
                """);

                // Adiciona coluna cpf em bancos existentes que ainda nao a possuem
                adicionarColunaSeAusente(conn, "contas", "cpf", "TEXT DEFAULT ''");

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS transacoes (
                        id              INTEGER PRIMARY KEY AUTOINCREMENT,
                        tipo            TEXT NOT NULL,
                        valor           REAL NOT NULL,
                        data_hora       TEXT NOT NULL,
                        conta_origem    TEXT,
                        conta_destino   TEXT
                    )
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS contatos_pix (
                        id              INTEGER PRIMARY KEY AUTOINCREMENT,
                        conta_dono      TEXT NOT NULL,
                        conta_contato   TEXT NOT NULL,
                        nome_contato    TEXT NOT NULL,
                        UNIQUE(conta_dono, conta_contato)
                    )
                """);

                System.out.println("Banco inicializado em: " + new File("banco_v4.db").getAbsolutePath());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Falha critica ao inicializar banco de dados", ex);
        }
    }

    /**
     * Adiciona uma coluna a uma tabela existente caso ela ainda nao exista.
     * Necessario para migrar bancos criados antes da adicao do campo CPF.
     */
    private static void adicionarColunaSeAusente(Connection conn, String tabela,
                                                 String coluna, String definicao) {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, tabela, coluna)) {
            if (!rs.next()) {
                conn.createStatement()
                        .execute("ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + definicao);
            }
        } catch (SQLException ex) {
            System.err.println("Aviso ao verificar/adicionar coluna '" + coluna + "': " + ex.getMessage());
        }
    }

    public static void salvarConta(Conta conta) {
        String sql = """
            INSERT INTO contas(numero, senha, saldo, nome, endereco, nascimento, fiador, limite, cpf)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, conta.getNumero());
            pstmt.setString(2, conta.getSenha());
            pstmt.setDouble(3, 0.0);
            pstmt.setString(4, conta.getNome());
            pstmt.setString(5, conta.getEndereco());
            pstmt.setString(6, conta.getNascimentoFormatado());
            pstmt.setString(7, conta.getNomeFiador());
            pstmt.setDouble(8, conta.getLimiteCredito());
            pstmt.setString(9, conta.getCpf());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao salvar conta: " + ex.getMessage(), ex);
        }
    }

    public static Conta buscarConta(String numeroConta) {
        String sql = "SELECT * FROM contas WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroConta);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return montarContaDeResultSet(rs);

        } catch (Exception ex) {
            System.err.println("Erro ao buscar conta por numero: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Busca conta pelo CPF armazenado (somente digitos, sem pontos ou traco).
     * Retorna null se nenhuma conta estiver vinculada ao CPF informado.
     */
    public static Conta buscarContaPorCpf(String cpfSomenteDigitos) {
        if (cpfSomenteDigitos == null || cpfSomenteDigitos.isBlank()) return null;

        String sql = "SELECT * FROM contas WHERE cpf = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cpfSomenteDigitos);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return montarContaDeResultSet(rs);

        } catch (Exception ex) {
            System.err.println("Erro ao buscar conta por CPF: " + ex.getMessage());
        }
        return null;
    }

    private static Conta montarContaDeResultSet(ResultSet rs) throws SQLException {
        LocalDate nascimento = LocalDate.parse(
                rs.getString("nascimento"),
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String cpfSalvo = rs.getString("cpf");

        Conta conta = new Conta(
                rs.getString("numero"),
                rs.getString("senha"),
                rs.getString("nome"),
                rs.getString("endereco"),
                nascimento,
                rs.getString("fiador"),
                rs.getDouble("limite"),
                cpfSalvo
        );

        double saldoSalvo = rs.getDouble("saldo");
        if (saldoSalvo > 0) conta.depositar(saldoSalvo);

        return conta;
    }

    public static void atualizarSaldo(Conta conta) {
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, conta.getSaldo());
            pstmt.setString(2, conta.getNumero());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar saldo: " + ex.getMessage());
        }
    }

    public static void atualizarLimite(String numeroConta, double novoLimite) {
        String sql = "UPDATE contas SET limite = ? WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, novoLimite);
            pstmt.setString(2, numeroConta);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar limite: " + ex.getMessage());
        }
    }

    public static void alterarSenha(String numeroConta, String novaSenha) {
        String sql = "UPDATE contas SET senha = ? WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novaSenha);
            pstmt.setString(2, numeroConta);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao alterar senha: " + ex.getMessage());
        }
    }

    public static boolean excluirConta(String numeroConta) {
        String sql = "DELETE FROM contas WHERE numero = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroConta);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir conta: " + ex.getMessage());
            return false;
        }
    }

    public static void registrarTransacao(String tipo, double valor, String origem, String destino) {
        String sql = """
            INSERT INTO transacoes(tipo, valor, data_hora, conta_origem, conta_destino)
            VALUES(?, ?, ?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            pstmt.setDouble(2, valor);
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.setString(4, origem);
            pstmt.setString(5, destino);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao registrar transacao: " + ex.getMessage());
        }
    }

    public static List<Transacao> buscarExtrato(String numeroConta) {
        List<Transacao> extrato = new ArrayList<>();
        String sql = """
            SELECT * FROM transacoes
            WHERE conta_origem = ? OR conta_destino = ?
            ORDER BY id DESC
        """;
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroConta);
            pstmt.setString(2, numeroConta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String tipo    = rs.getString("tipo");
                double valor   = rs.getDouble("valor");
                String dataHora = rs.getString("data_hora");
                String origem  = rs.getString("conta_origem");
                String destino = rs.getString("conta_destino");

                String detalhe;
                if ("DEPOSITO".equals(tipo)) {
                    detalhe = "Deposito recebido";
                } else if ("PIX".equals(tipo)) {
                    if (numeroConta.equals(origem)) {
                        detalhe = "Pix enviado para " + destino;
                        valor = -valor;
                    } else {
                        detalhe = "Pix recebido de " + origem;
                    }
                } else if ("RECARGA".equals(tipo)) {
                    detalhe = "Recarga celular " + destino;
                    valor = -valor;
                } else {
                    detalhe = tipo;
                }

                extrato.add(new Transacao(tipo, valor, dataHora, detalhe));
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao buscar extrato: " + ex.getMessage());
        }
        return extrato;
    }

    public static List<String> listarTodasContas() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT numero, nome, saldo, limite FROM contas ORDER BY nome";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(String.format("CONTA: %s | CLIENTE: %s | SALDO: R$ %.2f | LIMITE: R$ %.2f",
                        rs.getString("numero"),
                        rs.getString("nome"),
                        rs.getDouble("saldo"),
                        rs.getDouble("limite")));
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao listar contas: " + ex.getMessage());
        }
        return lista;
    }

    // -----------------------------------------------------------------------------------------
    // CONTATOS PIX
    // -----------------------------------------------------------------------------------------

    /**
     * Salva um contato Pix para a conta dona. Se o contato ja existir (UNIQUE),
     * atualiza o nome caso tenha mudado — comportamento de upsert.
     */
    public static void salvarContatoPix(String contaDono, String contaContato, String nomeContato) {
        String sql = """
            INSERT INTO contatos_pix(conta_dono, conta_contato, nome_contato)
            VALUES(?, ?, ?)
            ON CONFLICT(conta_dono, conta_contato) DO UPDATE SET nome_contato = excluded.nome_contato
        """;
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contaDono);
            pstmt.setString(2, contaContato);
            pstmt.setString(3, nomeContato);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar contato Pix: " + ex.getMessage());
        }
    }

    /**
     * Retorna todos os contatos Pix salvos para uma conta, ordenados por nome.
     * Cada entrada e um array {conta_contato, nome_contato}.
     */
    public static List<String[]> buscarContatosPix(String contaDono) {
        List<String[]> contatos = new ArrayList<>();
        String sql = """
            SELECT conta_contato, nome_contato
            FROM contatos_pix
            WHERE conta_dono = ?
            ORDER BY nome_contato COLLATE NOCASE
        """;
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contaDono);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                contatos.add(new String[]{
                        rs.getString("conta_contato"),
                        rs.getString("nome_contato")
                });
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao buscar contatos Pix: " + ex.getMessage());
        }
        return contatos;
    }

    public static void removerContatoPix(String contaDono, String contaContato) {
        String sql = "DELETE FROM contatos_pix WHERE conta_dono = ? AND conta_contato = ?";
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, contaDono);
            pstmt.setString(2, contaContato);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Erro ao remover contato Pix: " + ex.getMessage());
        }
    }
}