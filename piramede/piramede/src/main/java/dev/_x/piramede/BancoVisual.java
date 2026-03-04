package dev._x.piramede;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.BiConsumer;

public class BancoVisual {


    private static final Color COLOR_PRIMARY         = new Color(85, 55, 220);
    private static final Color COLOR_PRIMARY_DARK    = new Color(50, 30, 160);
    private static final Color COLOR_PRIMARY_LIGHT   = new Color(108, 80, 240);
    private static final Color COLOR_SURFACE         = new Color(250, 250, 252);
    private static final Color COLOR_CARD            = Color.WHITE;
    private static final Color COLOR_BACKGROUND      = new Color(242, 242, 248);
    private static final Color COLOR_TEXT_DARK       = new Color(18, 18, 30);
    private static final Color COLOR_TEXT_MUTED      = new Color(110, 110, 130);
    private static final Color COLOR_DIVIDER         = new Color(230, 230, 238);
    private static final Color COLOR_SUCCESS         = new Color(16, 163, 96);
    private static final Color COLOR_DANGER          = new Color(220, 50, 60);
    private static final Color COLOR_CHIP_BG         = new Color(220, 180, 80);

    private static final Font FONT_DISPLAY  = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font FONT_HERO     = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 17);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_LABEL    = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_MONO     = new Font("JetBrains Mono", Font.PLAIN, 15);

    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static boolean isBalanceVisible = true;



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                configurarAparencia();
                BancoDeDados.inicializarBanco();
                abrirTelaLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Falha ao inicializar: " + ex.getMessage(),
                        "Erro Critico", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void configurarAparencia() {
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.largura", 6);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.track", COLOR_BACKGROUND);
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
        FlatLightLaf.setup();
    }



    private static void abrirTelaLogin() {
        JFrame janela = construirJanela("BlueBank", 440, 680);


        JPanel painelRaiz = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(COLOR_PRIMARY);
                g2.fillRect(0, 0, getWidth(), getHeight() / 2 + 60);
                g2.setColor(COLOR_BACKGROUND);
                g2.fillRect(0, getHeight() / 2 + 60, getWidth(), getHeight());
            }
        };
        painelRaiz.setLayout(new GridBagLayout());

        JPanel cardFormulario = construirCardLogin(janela);
        painelRaiz.add(cardFormulario);


        JLabel logoLabel = new JLabel("BlueBank");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel taglineLabel = new JLabel("Simples, rapido e seguro.");
        taglineLabel.setFont(FONT_LABEL);
        taglineLabel.setForeground(new Color(255, 255, 255, 160));
        taglineLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JPanel pilhaVertical = new JPanel();
        pilhaVertical.setOpaque(false);
        pilhaVertical.setLayout(new BoxLayout(pilhaVertical, BoxLayout.Y_AXIS));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardFormulario.setAlignmentX(Component.CENTER_ALIGNMENT);

        pilhaVertical.add(logoLabel);
        pilhaVertical.add(Box.createVerticalStrut(4));
        pilhaVertical.add(taglineLabel);
        pilhaVertical.add(Box.createVerticalStrut(40));
        pilhaVertical.add(cardFormulario);

        painelRaiz.removeAll();
        painelRaiz.add(pilhaVertical);

        janela.add(painelRaiz);
        janela.setVisible(true);
    }

    private static JPanel construirCardLogin(JFrame janelaParente) {
        // Card com sombra simulada via borda exterior
        JPanel sombraCard = new JPanel(new BorderLayout());
        sombraCard.setOpaque(false);
        sombraCard.setBorder(new EmptyBorder(4, 4, 8, 4));

        JPanel cardFormulario = new JPanel();
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBackground(COLOR_CARD);
        cardFormulario.setBorder(new EmptyBorder(36, 32, 36, 32));
        cardFormulario.setPreferredSize(new Dimension(360, 340));
        cardFormulario.putClientProperty(FlatClientProperties.STYLE, "arc: 24");

        JLabel tituloTela = new JLabel("Acesse sua conta");
        tituloTela.setFont(FONT_TITLE);
        tituloTela.setForeground(COLOR_TEXT_DARK);
        tituloTela.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtituloTela = new JLabel("Digite suas credenciais para continuar");
        subtituloTela.setFont(FONT_LABEL);
        subtituloTela.setForeground(COLOR_TEXT_MUTED);
        subtituloTela.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoIdentificador = construirCampoTexto("CPF ou numero da conta");
        JPasswordField campoSenha = new JPasswordField();
        aplicarEstiloCampoEntrada(campoSenha, "Senha");
        campoSenha.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true; arc: 12");

        JButton botaoContinuar = construirBotaoPrimario("ENTRAR");
        botaoContinuar.addActionListener(e -> {
            String identificadorUsuario = campoIdentificador.getText().trim();
            String senha = new String(campoSenha.getPassword());
            processarTentativaDeLogin(janelaParente, identificadorUsuario, senha);
        });

        cardFormulario.add(tituloTela);
        cardFormulario.add(Box.createVerticalStrut(4));
        cardFormulario.add(subtituloTela);
        cardFormulario.add(Box.createVerticalStrut(28));
        cardFormulario.add(construirGrupoCampo("CPF ou Conta", campoIdentificador));
        cardFormulario.add(Box.createVerticalStrut(14));
        cardFormulario.add(construirGrupoCampo("Senha", campoSenha));
        cardFormulario.add(Box.createVerticalStrut(28));
        cardFormulario.add(botaoContinuar);

        sombraCard.add(cardFormulario);
        return cardFormulario;
    }

    private static void processarTentativaDeLogin(JFrame janelaLogin, String identificadorUsuario, String senha) {
        if (identificadorUsuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(janelaLogin, "Preencha usuario e senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (identificadorUsuario.equals("admin") && senha.equals("admin")) {
            janelaLogin.dispose();
            abrirPainelAdministrador();
            return;
        }

        try {

            Conta conta = BancoDeDados.buscarConta(identificadorUsuario);
            if (conta == null) {
                String digitosCpf = identificadorUsuario.replaceAll("[^0-9]", "");
                conta = BancoDeDados.buscarContaPorCpf(digitosCpf);
            }

            if (conta != null && conta.validarSenha(senha)) {
                janelaLogin.dispose();
                abrirPainelCliente(conta);
            } else {
                JOptionPane.showMessageDialog(janelaLogin,
                        "CPF/Conta ou senha incorretos.",
                        "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(janelaLogin,
                    "Erro ao verificar credenciais: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }



    private static void abrirPainelCliente(Conta conta) {
        JFrame janela = construirJanela("BlueBank", 420, 800);

        CardLayout navegador = new CardLayout();
        JPanel painelNavegacao = new JPanel(navegador);
        painelNavegacao.setBackground(COLOR_BACKGROUND);

        painelNavegacao.add(construirTelaInicial(janela, conta, navegador, painelNavegacao), "HOME");
        painelNavegacao.add(construirTelaPix(conta, navegador, painelNavegacao), "PIX");
        painelNavegacao.add(construirTelaOperacao("Deposito",   null,        "Valor (R$)", "Confirmar",     navegador, painelNavegacao, (v, k) -> executarDeposito(conta, v)),         "DEPOSIT");
        painelNavegacao.add(construirTelaOperacao("Recarga",   "Numero (DDD + numero)", "Valor (R$)", "Recarregar", navegador, painelNavegacao, (v, k) -> executarRecargaCelular(conta, v, k)), "RECHARGE");
        painelNavegacao.add(construirTelaExtrato(conta, navegador, painelNavegacao), "EXTRACT");
        painelNavegacao.add(construirTelaPerfil(conta, navegador, painelNavegacao), "PROFILE");
        painelNavegacao.add(construirTelaCartoes(conta, navegador, painelNavegacao), "CARDS");

        janela.add(painelNavegacao);
        janela.setVisible(true);
    }



    private static JPanel construirTelaInicial(JFrame janela, Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);

        // ---- Header colorido (hero section) ----
        JPanel secaoHero = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY, getWidth(), getHeight(), COLOR_PRIMARY_DARK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        secaoHero.setOpaque(false);
        secaoHero.setBorder(new EmptyBorder(20, 20, 30, 20));

        // Linha top: saudacao + botao sair
        String primeiroNome = conta.getNome().split(" ")[0];
        JLabel rotuloSaudacao = new JLabel("Ola, " + primeiroNome + "  \uD83D\uDC4B");
        rotuloSaudacao.setFont(FONT_SUBTITLE);
        rotuloSaudacao.setForeground(new Color(255, 255, 255, 200));

        JButton botaoSair = new JButton("Sair");
        botaoSair.setFont(FONT_LABEL);
        botaoSair.setForeground(Color.WHITE);
        botaoSair.setContentAreaFilled(false);
        botaoSair.setBorderPainted(false);
        botaoSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoSair.addActionListener(e -> { janela.dispose(); abrirTelaLogin(); });

        JPanel barraTopo = new JPanel(new BorderLayout());
        barraTopo.setOpaque(false);
        barraTopo.add(rotuloSaudacao, BorderLayout.WEST);
        barraTopo.add(botaoSair, BorderLayout.EAST);


        JLabel rotuloTituloSaldo = new JLabel("Saldo disponivel");
        rotuloTituloSaldo.setFont(FONT_LABEL);
        rotuloTituloSaldo.setForeground(new Color(255, 255, 255, 160));

        JLabel rotuloValorSaldo = new JLabel();
        rotuloValorSaldo.setFont(FONT_DISPLAY);
        rotuloValorSaldo.setForeground(Color.WHITE);
        atualizarRotuloSaldo(rotuloValorSaldo, conta.getSaldo());

        JButton botaoAlternarSaldo = new JButton(isBalanceVisible ? "ocultar" : "exibir");
        botaoAlternarSaldo.setFont(FONT_LABEL);
        botaoAlternarSaldo.setForeground(new Color(255, 255, 255, 160));
        botaoAlternarSaldo.setContentAreaFilled(false);
        botaoAlternarSaldo.setBorderPainted(false);
        botaoAlternarSaldo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoAlternarSaldo.addActionListener(e -> {
            isBalanceVisible = !isBalanceVisible;
            botaoAlternarSaldo.setText(isBalanceVisible ? "ocultar" : "exibir");
            atualizarRotuloSaldo(rotuloValorSaldo, conta.getSaldo());
        });

        JPanel balanceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        balanceRow.setOpaque(false);
        balanceRow.add(rotuloValorSaldo);
        balanceRow.add(Box.createHorizontalStrut(10));
        balanceRow.add(botaoAlternarSaldo);

        JPanel blocoSaldo = new JPanel();
        blocoSaldo.setOpaque(false);
        blocoSaldo.setLayout(new BoxLayout(blocoSaldo, BoxLayout.Y_AXIS));
        blocoSaldo.add(Box.createVerticalStrut(16));
        blocoSaldo.add(rotuloTituloSaldo);
        blocoSaldo.add(Box.createVerticalStrut(4));
        blocoSaldo.add(balanceRow);

        secaoHero.add(barraTopo, BorderLayout.NORTH);
        secaoHero.add(blocoSaldo, BorderLayout.CENTER);


        JPanel cardRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardRow.setBackground(COLOR_BACKGROUND);
        cardRow.setBorder(new EmptyBorder(20, 20, 10, 20));
        VirtualCardPanel cartaoVirtual = new VirtualCardPanel(conta);
        cardRow.add(cartaoVirtual);


        JPanel secaoAcoes = new JPanel();
        secaoAcoes.setBackground(COLOR_BACKGROUND);
        secaoAcoes.setLayout(new BoxLayout(secaoAcoes, BoxLayout.Y_AXIS));
        secaoAcoes.setBorder(new EmptyBorder(0, 20, 20, 20));

        JLabel tituloAcoes = new JLabel("O que voce quer fazer?");
        tituloAcoes.setFont(FONT_LABEL);
        tituloAcoes.setForeground(COLOR_TEXT_MUTED);
        tituloAcoes.setBorder(new EmptyBorder(16, 0, 12, 0));

        JPanel gradeAcoes = new JPanel(new GridLayout(2, 3, 12, 12));
        gradeAcoes.setBackground(COLOR_BACKGROUND);

        gradeAcoes.add(construirBotaoAcaoInicial("Pix",      "\u21C4", COLOR_PRIMARY,               e -> navegador.show(painelNavegacao, "PIX")));
        gradeAcoes.add(construirBotaoAcaoInicial("Extrato",  "\u2630", new Color(30, 150, 200),     e -> atualizarEExibirExtrato(conta, navegador, painelNavegacao)));
        gradeAcoes.add(construirBotaoAcaoInicial("Deposito", "\u2B07", COLOR_SUCCESS,               e -> navegador.show(painelNavegacao, "DEPOSIT")));
        gradeAcoes.add(construirBotaoAcaoInicial("Cartoes",  "\u2665", new Color(200, 60, 120),     e -> navegador.show(painelNavegacao, "CARDS")));
        gradeAcoes.add(construirBotaoAcaoInicial("Recarga",  "\u260E", new Color(255, 140, 0),      e -> navegador.show(painelNavegacao, "RECHARGE")));
        gradeAcoes.add(construirBotaoAcaoInicial("Perfil",   "\u2699", COLOR_TEXT_MUTED,            e -> navegador.show(painelNavegacao, "PROFILE")));

        secaoAcoes.add(tituloAcoes);
        secaoAcoes.add(gradeAcoes);


        JPanel conteudoScroll = new JPanel();
        conteudoScroll.setBackground(COLOR_BACKGROUND);
        conteudoScroll.setLayout(new BoxLayout(conteudoScroll, BoxLayout.Y_AXIS));
        conteudoScroll.add(secaoHero);
        conteudoScroll.add(cardRow);
        conteudoScroll.add(secaoAcoes);

        JScrollPane painelScroll = new JScrollPane(conteudoScroll);
        painelScroll.setBorder(null);
        painelScroll.getViewport().setBackground(COLOR_BACKGROUND);
        painelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        painelScroll.getVerticalScrollBar().setUnitIncrement(16);

        painelRaiz.add(painelScroll, BorderLayout.CENTER);
        return painelRaiz;
    }


    private static JPanel construirBotaoAcaoInicial(String rotulo, String simbolo, Color corDestaque,
                                                    java.awt.event.ActionListener aoClicar) {
        JLabel rotuloIcone = new JLabel(simbolo, SwingConstants.CENTER);
        rotuloIcone.setFont(new Font("Segoe UI", Font.BOLD, 20));
        rotuloIcone.setForeground(corDestaque);
        rotuloIcone.setPreferredSize(new Dimension(42, 42));
        rotuloIcone.setOpaque(true);
        Color bgTint = new Color(corDestaque.getRed(), corDestaque.getGreen(), corDestaque.getBlue(), 25);
        rotuloIcone.setBackground(bgTint);
        rotuloIcone.putClientProperty(FlatClientProperties.STYLE, "arc: 999");
        rotuloIcone.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel rotuloTexto = new JLabel(rotulo, SwingConstants.CENTER);
        rotuloTexto.setFont(FONT_LABEL);
        rotuloTexto.setForeground(COLOR_TEXT_DARK);

        JPanel celulaAcao = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        celulaAcao.setOpaque(false);
        celulaAcao.setLayout(new BoxLayout(celulaAcao, BoxLayout.Y_AXIS));
        celulaAcao.setBorder(new EmptyBorder(14, 8, 14, 8));
        celulaAcao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        rotuloIcone.setAlignmentX(Component.CENTER_ALIGNMENT);
        rotuloTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        celulaAcao.add(rotuloIcone);
        celulaAcao.add(Box.createVerticalStrut(8));
        celulaAcao.add(rotuloTexto);

        celulaAcao.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { aoClicar.actionPerformed(null); }
            @Override public void mouseEntered(MouseEvent e) {
                celulaAcao.putClientProperty("hover", true);
                celulaAcao.setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 2, 0, corDestaque),
                        new EmptyBorder(14, 8, 12, 8)));
            }
            @Override public void mouseExited(MouseEvent e) {
                celulaAcao.setBorder(new EmptyBorder(14, 8, 14, 8));
            }
        });
        return celulaAcao;
    }

    private static void atualizarEExibirExtrato(Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        painelNavegacao.add(construirTelaExtrato(conta, navegador, painelNavegacao), "EXTRACT");
        navegador.show(painelNavegacao, "EXTRACT");
    }



    private static JPanel construirTelaPix(Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);
        painelRaiz.add(construirCabecalhoInterno("Area Pix", navegador, painelNavegacao), BorderLayout.NORTH);


        JPanel conteudoScroll = new JPanel();
        conteudoScroll.setBackground(COLOR_BACKGROUND);
        conteudoScroll.setLayout(new BoxLayout(conteudoScroll, BoxLayout.Y_AXIS));
        conteudoScroll.setBorder(new EmptyBorder(20, 20, 20, 20));


        JPanel cardFormulario = new JPanel();
        cardFormulario.setBackground(COLOR_CARD);
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        cardFormulario.putClientProperty(FlatClientProperties.STYLE, "arc: 18");
        cardFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9999));

        JLabel tituloFormulario = new JLabel("Nova transferencia");
        tituloFormulario.setFont(FONT_TITLE);
        tituloFormulario.setForeground(COLOR_TEXT_DARK);
        tituloFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField campoChavePix    = construirCampoTexto("Numero da conta ou CPF");
        JTextField campoValor = construirCampoTexto("0,00");

        JButton botaoEnviar = construirBotaoPrimario("Enviar Pix");

        cardFormulario.add(tituloFormulario);
        cardFormulario.add(Box.createVerticalStrut(16));
        cardFormulario.add(construirGrupoCampo("Chave / Conta destino", campoChavePix));
        cardFormulario.add(Box.createVerticalStrut(12));
        cardFormulario.add(construirGrupoCampo("Valor (R$)", campoValor));
        cardFormulario.add(Box.createVerticalStrut(20));
        cardFormulario.add(botaoEnviar);


        JLabel tituloContatos = new JLabel("Contatos frequentes");
        tituloContatos.setFont(FONT_LABEL);
        tituloContatos.setForeground(COLOR_TEXT_MUTED);
        tituloContatos.setBorder(new EmptyBorder(20, 2, 10, 0));
        tituloContatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Painel da lista — reconstruido a cada refresh
        JPanel painelListaContatos = new JPanel();
        painelListaContatos.setBackground(COLOR_BACKGROUND);
        painelListaContatos.setLayout(new BoxLayout(painelListaContatos, BoxLayout.Y_AXIS));
        painelListaContatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Renderiza contatos e permite clicar para preencher o campo chave
        Runnable atualizarListaContatos = () -> {
            painelListaContatos.removeAll();
            List<String[]> listaContatos = BancoDeDados.buscarContatosPix(conta.getNumero());

            if (listaContatos.isEmpty()) {
                JLabel rotuloVazio = new JLabel("Nenhum contato ainda. Faca seu primeiro Pix!");
                rotuloVazio.setFont(FONT_LABEL);
                rotuloVazio.setForeground(COLOR_TEXT_MUTED);
                rotuloVazio.setAlignmentX(Component.LEFT_ALIGNMENT);
                painelListaContatos.add(rotuloVazio);
            } else {
                for (String[] contato : listaContatos) {
                    String numeroContaContato = contato[0];
                    String nomeContato          = contato[1];
                    painelListaContatos.add(construirItemContato(
                            nomeContato, numeroContaContato,
                            () -> campoChavePix.setText(numeroContaContato),
                            () -> {
                                int confirm = JOptionPane.showConfirmDialog(painelRaiz,
                                        "Remover " + nomeContato + " dos contatos?",
                                        "Remover Contato", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    BancoDeDados.removerContatoPix(conta.getNumero(), numeroContaContato);
                                    SwingUtilities.invokeLater(() -> {
                                        // Aciona refresh via listener apos remocao
                                        painelListaContatos.removeAll();
                                        painelListaContatos.revalidate();
                                        painelListaContatos.repaint();
                                    });
                                }
                            }
                    ));
                    painelListaContatos.add(Box.createVerticalStrut(8));
                }
            }
            painelListaContatos.revalidate();
            painelListaContatos.repaint();
        };

        // Acao do botao enviar: executa Pix, salva contato automaticamente, atualiza lista
        botaoEnviar.addActionListener(e -> {
            try {
                String chaveBruta    = campoChavePix.getText().trim();
                String valorBruto = campoValor.getText().trim().replace(",", ".");

                if (chaveBruta.isEmpty())    throw new IllegalArgumentException("Informe a chave ou conta destino.");
                if (valorBruto.isEmpty()) throw new IllegalArgumentException("Informe o valor.");

                double valor = Double.parseDouble(valorBruto);
                if (valor <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");

                executarTransferenciaPix(conta, valor, chaveBruta);


                Conta recipient = BancoDeDados.buscarConta(chaveBruta);
                if (recipient != null) {
                    BancoDeDados.salvarContatoPix(
                            conta.getNumero(),
                            recipient.getNumero(),
                            recipient.getNome());
                }

                JOptionPane.showMessageDialog(painelRaiz,
                        "Pix enviado com sucesso!\n" + formatarComoMoeda(valor) + " para " + chaveBruta,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                campoChavePix.setText("");
                campoValor.setText("");
                atualizarListaContatos.run();

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(painelRaiz,
                        "Valor invalido. Use o formato: 150,00",
                        "Formato Invalido", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painelRaiz,
                        ex.getMessage(), "Operacao Recusada", JOptionPane.ERROR_MESSAGE);
            }
        });


        atualizarListaContatos.run();

        conteudoScroll.add(cardFormulario);
        conteudoScroll.add(tituloContatos);
        conteudoScroll.add(painelListaContatos);

        JScrollPane painelScroll = new JScrollPane(conteudoScroll);
        painelScroll.setBorder(null);
        painelScroll.getViewport().setBackground(COLOR_BACKGROUND);
        painelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        painelScroll.getVerticalScrollBar().setUnitIncrement(16);

        painelRaiz.add(painelScroll, BorderLayout.CENTER);
        return painelRaiz;
    }

    private static JPanel construirItemContato(String nome, String numeroConta,
                                               Runnable aoSelecionar, Runnable aoRemover) {
        JPanel celulaAcao = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        celulaAcao.setOpaque(false);
        celulaAcao.setBorder(new EmptyBorder(12, 14, 12, 14));
        celulaAcao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        celulaAcao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        String inicial = nome.isBlank() ? "?" : nome.substring(0, 1).toUpperCase();
        JLabel rotuloAvatar = new JLabel(inicial, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int hue = Math.abs(nome.hashCode()) % 360;
                Color avatarColor = Color.getHSBColor(hue / 360f, 0.55f, 0.75f);
                g2.setColor(avatarColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        rotuloAvatar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rotuloAvatar.setForeground(Color.WHITE);
        rotuloAvatar.setOpaque(false);
        rotuloAvatar.setPreferredSize(new Dimension(40, 40));
        rotuloAvatar.setMinimumSize(new Dimension(40, 40));
        rotuloAvatar.setMaximumSize(new Dimension(40, 40));

        // Texto central: nome + numero
        JPanel blocoTexto = new JPanel(new BorderLayout(0, 2));
        blocoTexto.setOpaque(false);
        JLabel rotuloNome = new JLabel(nome);
        rotuloNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rotuloNome.setForeground(COLOR_TEXT_DARK);
        JLabel rotuloNumero = new JLabel("Conta " + numeroConta);
        rotuloNumero.setFont(FONT_LABEL);
        rotuloNumero.setForeground(COLOR_TEXT_MUTED);
        blocoTexto.add(rotuloNome,   BorderLayout.NORTH);
        blocoTexto.add(rotuloNumero, BorderLayout.CENTER);

        // Botao remover (X) discreto
        JButton botaoRemover = new JButton("\u00D7");
        botaoRemover.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botaoRemover.setForeground(COLOR_TEXT_MUTED);
        botaoRemover.setContentAreaFilled(false);
        botaoRemover.setBorderPainted(false);
        botaoRemover.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoRemover.setToolTipText("Remover contato");
        botaoRemover.addActionListener(e -> aoRemover.run());
        botaoRemover.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { botaoRemover.setForeground(COLOR_DANGER); }
            @Override public void mouseExited(MouseEvent e)  { botaoRemover.setForeground(COLOR_TEXT_MUTED); }
        });

        celulaAcao.add(rotuloAvatar,   BorderLayout.WEST);
        celulaAcao.add(blocoTexto,     BorderLayout.CENTER);
        celulaAcao.add(botaoRemover,  BorderLayout.EAST);


        celulaAcao.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { aoSelecionar.run(); }
            @Override public void mouseEntered(MouseEvent e) {
                celulaAcao.setBorder(new CompoundBorder(
                        new MatteBorder(0, 3, 0, 0, COLOR_PRIMARY),
                        new EmptyBorder(12, 11, 12, 14)));
            }
            @Override public void mouseExited(MouseEvent e)  {
                celulaAcao.setBorder(new EmptyBorder(12, 14, 12, 14));
            }
        });

        return celulaAcao;
    }



    private static JPanel construirTelaOperacao(
            String tituloTela, String auxiliaryLabel, String amountLabel,
            String confirmLabel, CardLayout navegador, JPanel painelNavegacao,
            BiConsumer<Double, String> aoConfirmar) {

        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);


        painelRaiz.add(construirCabecalhoInterno(tituloTela, navegador, painelNavegacao), BorderLayout.NORTH);


        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(COLOR_BACKGROUND);

        JPanel cardFormulario = new JPanel();
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBackground(COLOR_CARD);
        cardFormulario.setBorder(new EmptyBorder(28, 28, 28, 28));
        cardFormulario.setPreferredSize(new Dimension(360, auxiliaryLabel != null ? 320 : 260));
        cardFormulario.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        JTextField campoAuxiliar = construirCampoTexto(auxiliaryLabel != null ? auxiliaryLabel : "");
        if (auxiliaryLabel != null) {
            cardFormulario.add(construirGrupoCampo(auxiliaryLabel, campoAuxiliar));
            cardFormulario.add(Box.createVerticalStrut(16));
        }

        JTextField campoValor = construirCampoTexto("0,00");

        cardFormulario.add(construirGrupoCampo(amountLabel, campoValor));
        cardFormulario.add(Box.createVerticalStrut(28));

        JButton botaoConfirmar = construirBotaoPrimario(confirmLabel);
        botaoConfirmar.addActionListener(e -> {
            try {
                String valorBruto = campoValor.getText().trim().replace(",", ".");
                if (valorBruto.isEmpty()) throw new IllegalArgumentException("Informe o valor.");
                double valorConvertido = Double.parseDouble(valorBruto);
                if (valorConvertido <= 0)  throw new IllegalArgumentException("O valor deve ser maior que zero.");
                aoConfirmar.accept(valorConvertido, campoAuxiliar.getText().trim());
                JOptionPane.showMessageDialog(painelRaiz, "Operacao realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                campoValor.setText("");
                campoAuxiliar.setText("");
                navegador.show(painelNavegacao, "HOME");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(painelRaiz, "Valor invalido. Use o formato: 150,00", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painelRaiz, ex.getMessage(), "Operacao Recusada", JOptionPane.ERROR_MESSAGE);
            }
        });
        cardFormulario.add(botaoConfirmar);

        centerWrapper.add(cardFormulario);
        painelRaiz.add(centerWrapper, BorderLayout.CENTER);
        return painelRaiz;
    }



    private static JPanel construirTelaExtrato(Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);
        painelRaiz.add(construirCabecalhoInterno("Extrato", navegador, painelNavegacao), BorderLayout.NORTH);

        List<Transacao> transactions = BancoDeDados.buscarExtrato(conta.getNumero());

        if (transactions == null || transactions.isEmpty()) {
            JPanel estadoVazio = new JPanel(new GridBagLayout());
            estadoVazio.setBackground(COLOR_BACKGROUND);
            JLabel msg = new JLabel("Nenhuma movimentacao encontrada.");
            msg.setFont(FONT_SUBTITLE);
            msg.setForeground(COLOR_TEXT_MUTED);
            estadoVazio.add(msg);
            painelRaiz.add(estadoVazio, BorderLayout.CENTER);
            return painelRaiz;
        }

        String[] columns = {"Data", "Descricao", "Valor"};
        DefaultTableModel modeloTabela = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Transacao transaction : transactions) {
            modeloTabela.addRow(new Object[]{
                    transaction.getDataFormatada(),
                    transaction.getTipo(),
                    formatarComoMoeda(transaction.getValor())
            });
        }

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(48);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setBackground(COLOR_CARD);
        tabela.setForeground(COLOR_TEXT_DARK);
        tabela.setFont(FONT_BODY);
        tabela.setSelectionBackground(COLOR_BACKGROUND);
        tabela.setSelectionForeground(COLOR_TEXT_DARK);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cabecalho.setBackground(COLOR_SURFACE);
        cabecalho.setForeground(COLOR_TEXT_MUTED);
        cabecalho.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_DIVIDER));
        cabecalho.setReorderingAllowed(false);


        tabela.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int linhaDados, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, linhaDados, col);
                setHorizontalAlignment(JLabel.RIGHT);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                String val = v != null ? v.toString() : "";
                // Depositos e entradas em verde, saidas em vermelho
                String tipo = (String) t.getValueAt(linhaDados, 1);
                boolean isCredit = tipo != null && (tipo.contains("DEPOSITO") || tipo.contains("PIX_RECEBIDO"));
                setForeground(isCredit ? COLOR_SUCCESS : COLOR_TEXT_DARK);
                return this;
            }
        });

        // Padding nas celulas
        tabela.getColumnModel().getColumn(0).setCellRenderer(construirRenderizadorCelulaPadding(JLabel.LEFT));
        tabela.getColumnModel().getColumn(1).setCellRenderer(construirRenderizadorCelulaPadding(JLabel.LEFT));

        tabela.getColumnModel().getColumn(0).setPreferredWidth(90);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(110);

        JScrollPane painelScroll = new JScrollPane(tabela);
        painelScroll.setBorder(new EmptyBorder(12, 16, 16, 16));
        painelScroll.getViewport().setBackground(COLOR_BACKGROUND);
        painelScroll.setBackground(COLOR_BACKGROUND);

        painelRaiz.add(painelScroll, BorderLayout.CENTER);
        return painelRaiz;
    }

    private static DefaultTableCellRenderer construirRenderizadorCelulaPadding(int alinhamento) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int linhaDados, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, linhaDados, col);
                setHorizontalAlignment(alinhamento);
                setBorder(new EmptyBorder(0, 8, 0, 8));
                setBackground(sel ? COLOR_BACKGROUND : COLOR_CARD);
                return this;
            }
        };
    }



    private static JPanel construirTelaPerfil(Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);
        painelRaiz.add(construirCabecalhoInterno("Meu Perfil", navegador, painelNavegacao), BorderLayout.NORTH);

        JPanel conteudoScroll = new JPanel();
        conteudoScroll.setBackground(COLOR_BACKGROUND);
        conteudoScroll.setLayout(new BoxLayout(conteudoScroll, BoxLayout.Y_AXIS));
        conteudoScroll.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Avatar / iniciais
        String iniciais = extrairIniciais(conta.getNome());
        JLabel rotuloAvatar = new JLabel(iniciais, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        rotuloAvatar.setFont(new Font("Segoe UI", Font.BOLD, 28));
        rotuloAvatar.setForeground(Color.WHITE);
        rotuloAvatar.setOpaque(false);
        rotuloAvatar.setPreferredSize(new Dimension(72, 72));
        rotuloAvatar.setMinimumSize(new Dimension(72, 72));
        rotuloAvatar.setMaximumSize(new Dimension(72, 72));
        rotuloAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rotuloNome = new JLabel(conta.getNome(), SwingConstants.CENTER);
        rotuloNome.setFont(FONT_HERO);
        rotuloNome.setForeground(COLOR_TEXT_DARK);
        rotuloNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rotuloNumeroConta = new JLabel("Conta #" + conta.getNumero(), SwingConstants.CENTER);
        rotuloNumeroConta.setFont(FONT_LABEL);
        rotuloNumeroConta.setForeground(COLOR_TEXT_MUTED);
        rotuloNumeroConta.setAlignmentX(Component.CENTER_ALIGNMENT);

        conteudoScroll.add(rotuloAvatar);
        conteudoScroll.add(Box.createVerticalStrut(12));
        conteudoScroll.add(rotuloNome);
        conteudoScroll.add(Box.createVerticalStrut(4));
        conteudoScroll.add(rotuloNumeroConta);
        conteudoScroll.add(Box.createVerticalStrut(24));

        // Card de dados
        JPanel cardDados = construirCardDadosPerfil(conta);
        cardDados.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoScroll.add(cardDados);

        JScrollPane painelScroll = new JScrollPane(conteudoScroll);
        painelScroll.setBorder(null);
        painelScroll.getViewport().setBackground(COLOR_BACKGROUND);

        painelRaiz.add(painelScroll, BorderLayout.CENTER);
        return painelRaiz;
    }

    private static JPanel construirCardDadosPerfil(Conta conta) {
        JPanel cardFormulario = new JPanel();
        cardFormulario.setBackground(COLOR_CARD);
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBorder(new EmptyBorder(8, 0, 8, 0));
        cardFormulario.putClientProperty(FlatClientProperties.STYLE, "arc: 18");
        cardFormulario.setMaximumSize(new Dimension(380, 9999));

        adicionarLinhaPerfil(cardFormulario, "Endereco",          conta.getEndereco(),            true);
        adicionarLinhaPerfil(cardFormulario, "Data de Nascimento", conta.getNascimentoFormatado(), true);
        adicionarLinhaPerfil(cardFormulario, "Fiador Responsavel", conta.getNomeFiador(),          false);

        return cardFormulario;
    }

    private static void adicionarLinhaPerfil(JPanel painelPai, String rotulo, String valorExibido, boolean temDivisor) {
        JPanel linhaDados = new JPanel(new BorderLayout());
        linhaDados.setBackground(COLOR_CARD);
        linhaDados.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel textoRotulo = new JLabel(rotulo);
        textoRotulo.setFont(FONT_LABEL);
        textoRotulo.setForeground(COLOR_TEXT_MUTED);

        JLabel textoValor = new JLabel(valorExibido != null && !valorExibido.isBlank() ? valorExibido : "Nao informado");
        textoValor.setFont(FONT_BODY);
        textoValor.setForeground(COLOR_TEXT_DARK);

        JPanel painelInterno = new JPanel(new BorderLayout(0, 3));
        painelInterno.setOpaque(false);
        painelInterno.add(textoRotulo, BorderLayout.NORTH);
        painelInterno.add(textoValor, BorderLayout.CENTER);

        linhaDados.add(painelInterno, BorderLayout.CENTER);

        painelPai.add(linhaDados);
        if (temDivisor) {
            JSeparator sep = new JSeparator();
            sep.setForeground(COLOR_DIVIDER);
            sep.setBackground(COLOR_CARD);
            painelPai.add(sep);
        }
    }

    private static String extrairIniciais(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isBlank()) return "?";
        String[] partesNome = nomeCompleto.trim().split("\\s+");
        if (partesNome.length == 1) return partesNome[0].substring(0, 1).toUpperCase();
        return (partesNome[0].substring(0, 1) + partesNome[partesNome.length - 1].substring(0, 1)).toUpperCase();
    }



    private static JPanel construirTelaCartoes(Conta conta, CardLayout navegador, JPanel painelNavegacao) {
        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_BACKGROUND);
        painelRaiz.add(construirCabecalhoInterno("Meus Cartoes", navegador, painelNavegacao), BorderLayout.NORTH);

        JPanel conteudoScroll = new JPanel();
        conteudoScroll.setBackground(COLOR_BACKGROUND);
        conteudoScroll.setLayout(new BoxLayout(conteudoScroll, BoxLayout.Y_AXIS));
        conteudoScroll.setBorder(new EmptyBorder(24, 20, 24, 20));

        VirtualCardPanel cartaoVirtual = new VirtualCardPanel(conta);
        cartaoVirtual.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoScroll.add(cartaoVirtual);
        conteudoScroll.add(Box.createVerticalStrut(24));

        JPanel secaoCredito = construirCardLimiteCredito(conta, painelRaiz);
        secaoCredito.setAlignmentX(Component.CENTER_ALIGNMENT);
        conteudoScroll.add(secaoCredito);

        JScrollPane painelScroll = new JScrollPane(conteudoScroll);
        painelScroll.setBorder(null);
        painelScroll.getViewport().setBackground(COLOR_BACKGROUND);

        painelRaiz.add(painelScroll, BorderLayout.CENTER);
        return painelRaiz;
    }

    private static JPanel construirCardLimiteCredito(Conta conta, JPanel painelPai) {
        double limiteAprovado = conta.getLimiteCredito();
        int limiteComoInteiro = (int) Math.max(limiteAprovado, 0);

        JPanel cardFormulario = new JPanel();
        cardFormulario.setBackground(COLOR_CARD);
        cardFormulario.setLayout(new BoxLayout(cardFormulario, BoxLayout.Y_AXIS));
        cardFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        cardFormulario.putClientProperty(FlatClientProperties.STYLE, "arc: 18");
        cardFormulario.setMaximumSize(new Dimension(380, 9999));

        JLabel tituloSecao = new JLabel("Limite de Credito");
        tituloSecao.setFont(FONT_TITLE);
        tituloSecao.setForeground(COLOR_TEXT_DARK);
        tituloSecao.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLimiteAprovado = new JLabel("Limite aprovado: " + formatarComoMoeda(limiteAprovado));
        infoLimiteAprovado.setFont(FONT_LABEL);
        infoLimiteAprovado.setForeground(COLOR_TEXT_MUTED);
        infoLimiteAprovado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotuloValorSelecionado = new JLabel(formatarComoMoeda(limiteAprovado));
        rotuloValorSelecionado.setFont(new Font("Segoe UI", Font.BOLD, 26));
        rotuloValorSelecionado.setForeground(COLOR_SUCCESS);
        rotuloValorSelecionado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider controleSliderLimite = new JSlider(0, Math.max(limiteComoInteiro, 1), limiteComoInteiro);
        controleSliderLimite.setBackground(COLOR_CARD);
        controleSliderLimite.setFocusable(false);
        controleSliderLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
        controleSliderLimite.putClientProperty(FlatClientProperties.STYLE, "thumbColor: " + converterParaHex(COLOR_PRIMARY) + "; trackColor: " + converterParaHex(COLOR_PRIMARY));
        controleSliderLimite.addChangeListener(e -> rotuloValorSelecionado.setText(formatarComoMoeda(controleSliderLimite.getValue())));

        JButton botaoSalvar = construirBotaoPrimario("Confirmar Ajuste");
        botaoSalvar.addActionListener(e ->
                JOptionPane.showMessageDialog(painelPai,
                        "Limite ajustado para " + formatarComoMoeda(controleSliderLimite.getValue()) + ".",
                        "Limite Atualizado", JOptionPane.INFORMATION_MESSAGE));

        cardFormulario.add(tituloSecao);
        cardFormulario.add(Box.createVerticalStrut(4));
        cardFormulario.add(infoLimiteAprovado);
        cardFormulario.add(Box.createVerticalStrut(16));
        cardFormulario.add(rotuloValorSelecionado);
        cardFormulario.add(Box.createVerticalStrut(10));
        cardFormulario.add(controleSliderLimite);
        cardFormulario.add(Box.createVerticalStrut(20));
        cardFormulario.add(botaoSalvar);

        return cardFormulario;
    }



    private static void abrirPainelAdministrador() {
        JFrame janela = new JFrame("BlueBank - Administracao");
        janela.setSize(860, 620);
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel modeloTabela = new DefaultTableModel(
                new String[]{"ID / Conta", "Nome", "Saldo", "Limite de Credito"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabelaContas = construirTabelaSomenteLeitura(modeloTabela);
        tabelaContas.setRowHeight(42);
        recarregarTabelaContas(modeloTabela);

        JPanel barraBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        barraBotoes.setBackground(COLOR_SURFACE);
        barraBotoes.setBorder(new MatteBorder(0, 0, 1, 0, COLOR_DIVIDER));

        JButton botaoNovaConta  = construirBotaoBarra("+ Nova Conta",    COLOR_SUCCESS);
        JButton botaoEditarLimite   = construirBotaoBarra("Alterar Limite",   COLOR_PRIMARY);
        JButton botaoRedefinirSenha   = construirBotaoBarra("Redefinir Senha",  COLOR_TEXT_MUTED);
        JButton botaoExcluir      = construirBotaoBarra("Excluir",          COLOR_DANGER);
        JButton botaoSair      = construirBotaoBarra("Sair",             COLOR_TEXT_MUTED);

        botaoNovaConta.addActionListener(e -> { executarFluxoCadastroConta(janela); recarregarTabelaContas(modeloTabela); });
        botaoEditarLimite.addActionListener(e  -> editarLimiteContaSelecionada(tabelaContas, modeloTabela, janela));
        botaoRedefinirSenha.addActionListener(e  -> redefinirSenhaContaSelecionada(tabelaContas, janela));
        botaoExcluir.addActionListener(e     -> excluirContaSelecionada(tabelaContas, modeloTabela, janela));
        botaoSair.addActionListener(e     -> { janela.dispose(); abrirTelaLogin(); });

        barraBotoes.add(botaoNovaConta);
        barraBotoes.add(botaoEditarLimite);
        barraBotoes.add(botaoRedefinirSenha);
        barraBotoes.add(botaoExcluir);
        barraBotoes.add(Box.createHorizontalGlue());
        barraBotoes.add(botaoSair);

        JScrollPane scrollTabela = new JScrollPane(tabelaContas);
        scrollTabela.setBorder(null);
        scrollTabela.getViewport().setBackground(Color.WHITE);

        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.add(barraBotoes, BorderLayout.NORTH);
        painelRaiz.add(scrollTabela, BorderLayout.CENTER);

        janela.add(painelRaiz);
        janela.setVisible(true);
    }

    private static JButton construirBotaoBarra(String texto, Color corDestaque) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_LABEL);
        btn.setForeground(corDestaque);
        btn.setContentAreaFilled(false);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "border: 1," + converterParaHex(new Color(corDestaque.getRed(), corDestaque.getGreen(), corDestaque.getBlue(), 80)) + ",,6");
        return btn;
    }

    private static void executarFluxoCadastroConta(JFrame janelaParente) {
        JDialog dialog = new JDialog(janelaParente, "Nova Conta", true);
        dialog.setSize(420, 680);
        dialog.setLocationRelativeTo(janelaParente);
        dialog.setResizable(false);

        JPanel painelRaiz = new JPanel(new BorderLayout());
        painelRaiz.setBackground(COLOR_CARD);

        JPanel cabecalhoDialog = new JPanel(new BorderLayout());
        cabecalhoDialog.setBackground(COLOR_PRIMARY);
        cabecalhoDialog.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel tituloDialog = new JLabel("Cadastro de Nova Conta");
        tituloDialog.setFont(FONT_TITLE);
        tituloDialog.setForeground(Color.WHITE);
        cabecalhoDialog.add(tituloDialog, BorderLayout.CENTER);

        JPanel formulario = new JPanel();
        formulario.setBackground(COLOR_CARD);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(new EmptyBorder(24, 24, 16, 24));

        JTextField campoNome        = construirCampoTexto("Nome completo");
        JTextField campoCpf         = construirCampoTexto("000.000.000-00");
        JTextField campoDataNascimento   = construirCampoTexto("dd/MM/aaaa");
        JTextField campoCep         = construirCampoTexto("00000-000");
        JTextField campoLogradouro      = construirCampoTexto("Preenchido automaticamente");
        JTextField campoNumeroEndereco  = construirCampoTexto("Ex: 142");
        JTextField campoFiador   = construirCampoTexto("Nome do fiador");
        JPasswordField campoSenha = new JPasswordField();
        aplicarEstiloCampoEntrada(campoSenha, "4 digitos numericos");
        campoSenha.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true; arc: 12");


        campoLogradouro.setEditable(false);
        campoLogradouro.setBackground(COLOR_BACKGROUND);


        JLabel rotuloStatusCep = new JLabel(" ");
        rotuloStatusCep.setFont(FONT_LABEL);
        rotuloStatusCep.setForeground(COLOR_TEXT_MUTED);
        rotuloStatusCep.setAlignmentX(Component.LEFT_ALIGNMENT);


        campoCep.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String cepBruto = campoCep.getText().trim().replaceAll("[^0-9]", "");
                if (cepBruto.length() != 8) return;

                rotuloStatusCep.setText("Buscando endereco...");
                rotuloStatusCep.setForeground(COLOR_TEXT_MUTED);
                campoLogradouro.setText("");


                new Thread(() -> {
                    try {
                        java.net.URL urlViaCep = new java.net.URL("https://viacep.com.br/ws/" + cepBruto + "/respostaJson/");
                        java.net.HttpURLConnection conexaoHttp = (java.net.HttpURLConnection) urlViaCep.openConnection();
                        conexaoHttp.setRequestMethod("GET");
                        conexaoHttp.setConnectTimeout(5000);
                        conexaoHttp.setReadTimeout(5000);

                        java.io.BufferedReader leitorResposta = new java.io.BufferedReader(
                                new java.io.InputStreamReader(conexaoHttp.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                        StringBuilder corpoResposta = new StringBuilder();
                        String linhaLida;
                        while ((linhaLida = leitorResposta.readLine()) != null) corpoResposta.append(linhaLida);
                        leitorResposta.close();

                        String respostaJson = corpoResposta.toString();


                        if (respostaJson.contains("\"erro\"")) {
                            SwingUtilities.invokeLater(() -> {
                                rotuloStatusCep.setText("CEP nao encontrado.");
                                rotuloStatusCep.setForeground(COLOR_DANGER);
                            });
                            return;
                        }

                        String nomeLogradouro = extrairCampoJson(respostaJson, "nomeLogradouro");
                        String nomeBairro     = extrairCampoJson(respostaJson, "nomeBairro");
                        String nomeCidade     = extrairCampoJson(respostaJson, "localidade");
                        String uf         = extrairCampoJson(respostaJson, "uf");

                        String fullStreet = nomeLogradouro
                                + (nomeBairro.isEmpty() ? "" : ", " + nomeBairro)
                                + " - " + nomeCidade + "/" + uf;

                        SwingUtilities.invokeLater(() -> {
                            campoLogradouro.setText(fullStreet);
                            rotuloStatusCep.setText("Endereco encontrado.");
                            rotuloStatusCep.setForeground(COLOR_SUCCESS);
                        });

                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            rotuloStatusCep.setText("Falha ao buscar CEP. Verifique a conexao.");
                            rotuloStatusCep.setForeground(COLOR_DANGER);
                        });
                    }
                }).start();
            }
        });

        // Grupo CEP com status inline
        JPanel grupoCep = new JPanel();
        grupoCep.setOpaque(false);
        grupoCep.setLayout(new BoxLayout(grupoCep, BoxLayout.Y_AXIS));
        grupoCep.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel rotuloCep = new JLabel("CEP *");
        rotuloCep.setFont(FONT_LABEL);
        rotuloCep.setForeground(COLOR_TEXT_MUTED);
        rotuloCep.setBorder(new EmptyBorder(0, 2, 5, 0));
        rotuloCep.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoCep.setAlignmentX(Component.LEFT_ALIGNMENT);
        rotuloStatusCep.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupoCep.add(rotuloCep);
        grupoCep.add(campoCep);
        grupoCep.add(Box.createVerticalStrut(3));
        grupoCep.add(rotuloStatusCep);

        formulario.add(construirGrupoCampo("Nome Completo *",         campoNome));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("CPF *",                   campoCpf));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("Data de Nascimento *",    campoDataNascimento));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(grupoCep);
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("Logradouro",              campoLogradouro));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("Numero da Residencia *",  campoNumeroEndereco));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("Fiador Responsavel",      campoFiador));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(construirGrupoCampo("Senha (4 digitos) *",     campoSenha));

        JPanel filhaBotoes = new JPanel(new GridLayout(1, 2, 12, 0));
        filhaBotoes.setBackground(COLOR_CARD);
        filhaBotoes.setBorder(new EmptyBorder(20, 24, 24, 24));

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setFont(FONT_BODY);
        botaoCancelar.setForeground(COLOR_TEXT_MUTED);
        botaoCancelar.setContentAreaFilled(false);
        botaoCancelar.addActionListener(e -> dialog.dispose());

        JButton botaoConfirmar = construirBotaoPrimario("Criar Conta");
        botaoConfirmar.addActionListener(e -> {
            String nomeCompleto     = campoNome.getText().trim();
            String cpf          = campoCpf.getText().trim().replaceAll("[^0-9]", "");
            String birthDateRaw = campoDataNascimento.getText().trim();
            String cep          = campoCep.getText().trim().replaceAll("[^0-9]", "");
            String logradouroCompleto       = campoLogradouro.getText().trim();
            String addressNum   = campoNumeroEndereco.getText().trim();
            String guarantor    = campoFiador.getText().trim();
            String senha     = new String(campoSenha.getPassword()).trim();

            if (nomeCompleto.isEmpty() || cpf.isEmpty() || birthDateRaw.isEmpty()
                    || cep.isEmpty() || addressNum.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Preencha todos os campos obrigatorios (*).",
                        "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(dialog,
                        "CPF deve conter 11 digitos numericos.",
                        "CPF Invalido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (senha.length() != 4 || !senha.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(dialog,
                        "A senha deve conter exatamente 4 digitos numericos.",
                        "Senha Invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(birthDateRaw,
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Data de nascimento invalida. Use o formato dd/MM/aaaa.",
                        "Data Invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String idGerado    = String.valueOf(new Random().nextInt(9000) + 1000);
                String enderecoCompleto    = (logradouroCompleto.isEmpty() ? cep : logradouroCompleto) + ", N " + addressNum;
                String fiadorFinal = guarantor.isEmpty() ? "N/A" : guarantor;

                BancoDeDados.salvarConta(new Conta(
                        idGerado, senha, nomeCompleto,
                        enderecoCompleto, dataNascimento, fiadorFinal, 0.0, cpf));

                JOptionPane.showMessageDialog(dialog,
                        "Conta criada com sucesso!\n\n"
                                + "Numero da Conta: " + idGerado + "\n"
                                + "CPF: " + formatarCpf(cpf) + "\n"
                                + "Senha: " + senha + "\n\n"
                                + "O cliente pode entrar com o CPF ou o numero da conta.",
                        "Conta Criada", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Erro ao criar conta: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        filhaBotoes.add(botaoCancelar);
        filhaBotoes.add(botaoConfirmar);

        JScrollPane scrollFormulario = new JScrollPane(formulario);
        scrollFormulario.setBorder(null);
        scrollFormulario.getViewport().setBackground(COLOR_CARD);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);

        painelRaiz.add(cabecalhoDialog, BorderLayout.NORTH);
        painelRaiz.add(scrollFormulario,   BorderLayout.CENTER);
        painelRaiz.add(filhaBotoes,    BorderLayout.SOUTH);

        dialog.add(painelRaiz);
        dialog.setVisible(true);
    }


    private static String extrairCampoJson(String respostaJson, String nomeCampo) {
        String key = "\"" + nomeCampo + "\"";
        int posicaoChave = respostaJson.indexOf(key);
        if (posicaoChave == -1) return "";
        int posicaoDoisPontos = respostaJson.indexOf(":", posicaoChave);
        if (posicaoDoisPontos == -1) return "";
        int posicaoAbreAspas = respostaJson.indexOf("\"", posicaoDoisPontos);
        if (posicaoAbreAspas == -1) return "";
        int posicaoFechaAspas = respostaJson.indexOf("\"", posicaoAbreAspas + 1);
        if (posicaoFechaAspas == -1) return "";
        return respostaJson.substring(posicaoAbreAspas + 1, posicaoFechaAspas);
    }

    private static String formatarCpf(String digitosCpf) {
        if (digitosCpf == null || digitosCpf.length() != 11) return digitosCpf;
        return digitosCpf.substring(0, 3) + "."
                + digitosCpf.substring(3, 6) + "."
                + digitosCpf.substring(6, 9) + "-"
                + digitosCpf.substring(9);
    }

    private static void editarLimiteContaSelecionada(JTable tabela, DefaultTableModel modeloTabela, JFrame janelaParente) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) { JOptionPane.showMessageDialog(janelaParente, "Selecione uma conta.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String idConta = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        String limiteBruto  = JOptionPane.showInputDialog(janelaParente, "Novo limite (ex: 2500,00):");
        if (limiteBruto == null || limiteBruto.isBlank()) return;
        try {
            double novoLimite = Double.parseDouble(limiteBruto.replace(",", "."));
            BancoDeDados.atualizarLimite(idConta, novoLimite);
            recarregarTabelaContas(modeloTabela);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(janelaParente, "Formato invalido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(janelaParente, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void redefinirSenhaContaSelecionada(JTable tabela, JFrame janelaParente) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) { JOptionPane.showMessageDialog(janelaParente, "Selecione uma conta.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String idConta   = (String) tabela.getValueAt(linhaSelecionada, 0);
        String novaSenha = JOptionPane.showInputDialog(janelaParente, "Nova senha para conta " + idConta + ":");
        if (novaSenha == null || novaSenha.isBlank()) return;
        try {
            BancoDeDados.alterarSenha(idConta, novaSenha);
            JOptionPane.showMessageDialog(janelaParente, "Senha redefinida.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(janelaParente, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void excluirContaSelecionada(JTable tabela, DefaultTableModel modeloTabela, JFrame janelaParente) {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada == -1) { JOptionPane.showMessageDialog(janelaParente, "Selecione uma conta.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        String idConta = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        int confirm = JOptionPane.showConfirmDialog(janelaParente,
                "Excluir permanentemente a conta " + idConta + "?",
                "Confirmar Exclusao", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            BancoDeDados.excluirConta(idConta);
            recarregarTabelaContas(modeloTabela);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(janelaParente, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void recarregarTabelaContas(DefaultTableModel modeloTabela) {
        modeloTabela.setRowCount(0);
        for (String summary : BancoDeDados.listarTodasContas()) {
            try {
                String[] fields = summary.split("\\|");
                modeloTabela.addRow(new Object[]{
                        fields[0].split(":")[1].trim(),
                        fields[1].split(":")[1].trim(),
                        fields[2].split(":")[1].trim(),
                        fields[3].split(":")[1].trim()
                });
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
    }



    private static void executarTransferenciaPix(Conta contaRemetente, double valor, String chavePix) {
        if (chavePix == null || chavePix.isBlank())
            throw new IllegalArgumentException("Informe a chave Pix do destinatario.");
        Conta contaDestinatario = BancoDeDados.buscarConta(chavePix);
        if (contaDestinatario == null)
            throw new IllegalArgumentException("Destinatario nao encontrado para a chave: " + chavePix);
        if (contaDestinatario.getNumero().equals(contaRemetente.getNumero()))
            throw new IllegalArgumentException("Nao e possivel enviar Pix para a propria conta.");
        if (!contaRemetente.transferir(valor, contaDestinatario))
            throw new IllegalStateException("Saldo insuficiente para esta operacao.");
        BancoDeDados.atualizarSaldo(contaRemetente);
        BancoDeDados.atualizarSaldo(contaDestinatario);
        BancoDeDados.registrarTransacao("PIX", valor, contaRemetente.getNumero(), contaDestinatario.getNumero());
    }

    private static void executarDeposito(Conta conta, double valor) {
        if (valor <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        conta.depositar(valor);
        BancoDeDados.atualizarSaldo(conta);
        BancoDeDados.registrarTransacao("DEPOSITO", valor, "Caixa", conta.getNumero());
    }

    private static void executarRecargaCelular(Conta conta, double valor, String numeroCelular) {
        if (numeroCelular == null || numeroCelular.isBlank())
            throw new IllegalArgumentException("Informe o numero do celular.");
        if (!conta.sacarSomenteSaldo(valor))
            throw new IllegalStateException("Saldo insuficiente para a recarga.");
        BancoDeDados.atualizarSaldo(conta);
        BancoDeDados.registrarTransacao("RECARGA", valor, conta.getNumero(), numeroCelular);
    }



    static class VirtualCardPanel extends JPanel {
        private final Conta conta;
        private static final int CARD_W = 360;
        private static final int CARD_H = 210;

        VirtualCardPanel(Conta conta) {
            this.conta = conta;
            setPreferredSize(new Dimension(CARD_W, CARD_H));
            setMaximumSize(new Dimension(CARD_W, CARD_H));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);


            GradientPaint gradiente = new GradientPaint(0, 0, COLOR_PRIMARY, CARD_W, CARD_H, COLOR_PRIMARY_DARK);
            g2.setPaint(gradiente);
            g2.fill(new RoundRectangle2D.Double(0, 0, CARD_W, CARD_H, 22, 22));


            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(CARD_W - 120, -40, 200, 200);
            g2.fillOval(-60, CARD_H - 80, 200, 200);


            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString("BlueBank", 24, 38);


            g2.setColor(new Color(255, 255, 255, 180));
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString(")))", CARD_W - 52, 38);

            // Chip EMV simulado
            g2.setColor(COLOR_CHIP_BG);
            g2.fillRoundRect(24, 68, 44, 32, 7, 7);
            g2.setColor(new Color(180, 140, 40));
            g2.drawRoundRect(24, 68, 44, 32, 7, 7);
            // Linhas do chip
            g2.setColor(new Color(180, 140, 40, 120));
            g2.drawLine(38, 68, 38, 100);
            g2.drawLine(54, 68, 54, 100);
            g2.drawLine(24, 82, 68, 82);


            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Courier New", Font.BOLD, 17));
            g2.drawString("\u2022\u2022\u2022\u2022  \u2022\u2022\u2022\u2022  \u2022\u2022\u2022\u2022  " + conta.getNumero(), 24, 148);


            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(new Color(255, 255, 255, 200));
            g2.drawString("TITULAR", 24, 172);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(conta.getNome().toUpperCase(), 24, 190);

            g2.dispose();
        }
    }

    private static JPanel construirCabecalhoInterno(String titulo, CardLayout navegador, JPanel painelNavegacao) {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COLOR_CARD);
        cabecalho.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, COLOR_DIVIDER),
                new EmptyBorder(14, 16, 14, 16)));

        JButton botaoVoltar = new JButton("\u2190  Voltar");
        botaoVoltar.setFont(FONT_BODY);
        botaoVoltar.setForeground(COLOR_PRIMARY);
        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botaoVoltar.addActionListener(e -> navegador.show(painelNavegacao, "HOME"));

        JLabel rotuloTitulo = new JLabel(titulo, SwingConstants.CENTER);
        rotuloTitulo.setFont(FONT_TITLE);
        rotuloTitulo.setForeground(COLOR_TEXT_DARK);

        cabecalho.add(botaoVoltar, BorderLayout.WEST);
        cabecalho.add(rotuloTitulo, BorderLayout.CENTER);
        cabecalho.add(new JLabel("         "), BorderLayout.EAST);

        return cabecalho;
    }

    private static void atualizarRotuloSaldo(JLabel rotulo, double balance) {
        if (isBalanceVisible) {
            rotulo.setText(formatarComoMoeda(balance));
        } else {
            rotulo.setText("R$ \u2022 \u2022 \u2022 \u2022 \u2022");
        }
    }


    private static JTextField construirCampoTexto(String textoDica) {
        JTextField campo = new JTextField();
        aplicarEstiloCampoEntrada(campo, textoDica);
        return campo;
    }

    private static void aplicarEstiloCampoEntrada(JTextField campo, String textoDica) {
        campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, textoDica);
        campo.setFont(FONT_BODY);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; margin: 6,12,6,12");
    }

    private static JPanel construirGrupoCampo(String textoRotulo, JTextField campo) {
        JPanel grupoCampo = new JPanel();
        grupoCampo.setOpaque(false);
        grupoCampo.setLayout(new BoxLayout(grupoCampo, BoxLayout.Y_AXIS));
        grupoCampo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotulo = new JLabel(textoRotulo);
        rotulo.setFont(FONT_LABEL);
        rotulo.setForeground(COLOR_TEXT_MUTED);
        rotulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        rotulo.setBorder(new EmptyBorder(0, 2, 5, 0));

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        grupoCampo.add(rotulo);
        grupoCampo.add(campo);
        return grupoCampo;
    }

    private static JButton construirBotaoPrimario(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botao.setBackground(COLOR_PRIMARY);
        botao.setForeground(Color.WHITE);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.putClientProperty(FlatClientProperties.STYLE,
                "arc: 12; background: " + converterParaHex(COLOR_PRIMARY) + "; foreground: #ffffff;");
        return botao;
    }

    private static JTable construirTabelaSomenteLeitura(DefaultTableModel modelo) {
        JTable tabela = new JTable(modelo);
        tabela.setRowHeight(42);
        tabela.setShowGrid(false);
        tabela.setFocusable(false);
        tabela.setFont(FONT_BODY);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COLOR_SURFACE);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setDefaultEditor(Object.class, null);
        return tabela;
    }

    private static String formatarComoMoeda(double valor) {
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(valor);
    }

    private static String converterParaHex(Color cor) {
        return String.format("#%02x%02x%02x", cor.getRed(), cor.getGreen(), cor.getBlue());
    }

    private static JFrame construirJanela(String titulo, int largura, int altura) {
        JFrame janela = new JFrame(titulo);
        janela.setSize(largura, altura);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        return janela;
    }
}