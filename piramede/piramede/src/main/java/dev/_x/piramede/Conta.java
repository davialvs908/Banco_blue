package dev._x.piramede;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Conta {
    private final String numero;
    private final String senha;
    private double saldo;
    private final String nome;
    private final String endereco;
    private final LocalDate dataNascimento;
    private final String nomeFiador;
    private double limiteCredito;
    private String cpf;

    public Conta(String numero, String senha, String nome, String endereco,
                 LocalDate dataNascimento, String nomeFiador, double limiteCredito) {
        this.numero = numero;
        this.senha = senha;
        this.nome = nome;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
        this.nomeFiador = nomeFiador;
        this.limiteCredito = limiteCredito;
        this.saldo = 0.0;
        this.cpf = "";
    }

    public Conta(String numero, String senha, String nome, String endereco,
                 LocalDate dataNascimento, String nomeFiador, double limiteCredito, String cpf) {
        this(numero, senha, nome, endereco, dataNascimento, nomeFiador, limiteCredito);
        this.cpf = cpf != null ? cpf : "";
    }

    public boolean validarSenha(String senhaDigitada) {
        return this.senha != null && this.senha.equals(senhaDigitada);
    }

    public void depositar(double valor) {
        if (valor > 0) this.saldo += valor;
    }

    public boolean sacar(double valor) {
        double disponivelTotal = this.saldo + this.limiteCredito;
        if (valor > 0 && disponivelTotal >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    public boolean sacarSomenteSaldo(double valor) {
        if (valor > 0 && this.saldo >= valor) {
            this.saldo -= valor;
            return true;
        }
        return false;
    }

    public boolean transferir(double valor, Conta destino) {
        if (this.sacar(valor)) {
            destino.depositar(valor);
            return true;
        }
        return false;
    }

    public String getNumero()     { return numero; }
    public String getSenha()      { return senha; }
    public String getNome()       { return nome; }
    public String getEndereco()   { return endereco; }
    public String getNomeFiador() { return nomeFiador; }
    public double getSaldo()      { return saldo; }
    public double getLimiteCredito() { return limiteCredito; }
    public String getCpf()        { return cpf != null ? cpf : ""; }
    public void   setCpf(String cpf) { this.cpf = cpf != null ? cpf : ""; }

    public String getNascimentoFormatado() {
        return dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}