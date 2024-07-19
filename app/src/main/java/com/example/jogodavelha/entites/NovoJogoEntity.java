package com.example.jogodavelha.entites;

import com.example.jogodavelha.Jogador;

import java.io.Serializable;

public class NovoJogoEntity implements Serializable {

    private JogadorEntity primeiroJogador;
    private JogadorEntity segundoJogador;
    private String simboloPrimeiroJogador;
    private String simboloSegundoJogador;

    public NovoJogoEntity(JogadorEntity primeiroJogador, JogadorEntity segundoJogador, String simboloPrimeiroJogador, String simboloSegundoJogador) {
        this.primeiroJogador = primeiroJogador;
        this.segundoJogador = segundoJogador;
        this.simboloPrimeiroJogador = simboloPrimeiroJogador;
        this.simboloSegundoJogador = simboloSegundoJogador;
    }

    public String getSimboloPrimeiroJogador() {
        return simboloPrimeiroJogador;
    }

    public void setSimboloPrimeiroJogador(String simboloPrimeiroJogador) {
        this.simboloPrimeiroJogador = simboloPrimeiroJogador;
    }

    public String getSimboloSegundoJogador() {
        return simboloSegundoJogador;
    }

    public void setSimboloSegundoJogador(String simboloSegundoJogador) {
        this.simboloSegundoJogador = simboloSegundoJogador;
    }

    public JogadorEntity getPrimeiroJogador() {
        return primeiroJogador;
    }

    public void setPrimeiroJogador(JogadorEntity primeiroJogador) {
        this.primeiroJogador = primeiroJogador;
    }

    public JogadorEntity getSegundoJogador() {
        return segundoJogador;
    }

    public void setSegundoJogador(JogadorEntity segundoJogador) {
        this.segundoJogador = segundoJogador;
    }
}
