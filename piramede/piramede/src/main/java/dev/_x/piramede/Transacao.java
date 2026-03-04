package dev._x.piramede;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {
    private final String tipo;
    private final double valor;
    private final LocalDateTime dataHora;
    private final String detalhe;

    public Transacao(String tipo, double valor, String dataHoraString, String detalhe) {
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = LocalDateTime.parse(dataHoraString);
        this.detalhe = detalhe;
    }

    public String getTipo() { return tipo; }
    public double getValor() { return valor; }
    public String getDetalhe() { return detalhe; }

    public String getDataFormatada() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}