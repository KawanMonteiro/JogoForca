package com.jogoforca.model;

public class Jogadores {
    private String nome;
    private int acertos, erros;

    public Jogadores(String nome) {
        setNome(nome);
        setAcertos(0);
        setErros(0);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null) {
            throw new NullPointerException("Nome não pode ser nulo!");
        }
        this.nome = nome;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(int acertos) {
        if (acertos < 0) {
            throw new IllegalArgumentException("Acertos não podem ser negativos!");
        }
        this.acertos = acertos;
    }

    public int getErros() {
        return erros;
    }

    public void setErros(int erros) {
        if (erros < 0) {
            throw new IllegalArgumentException("Error não podem ser negativos!");
        }
        this.erros = erros;
    }
}
