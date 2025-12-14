package com.jogoforca.model;

public class Jogadores {
    private String nomeJ1, nomeJ2;
    private int pontosJ1, pontosJ2;
    private int acertosJ1, acertosJ2;
    private int errosJ1, errosJ2;

    public Jogadores(String nomeJ1, String nomeJ2) {
        setNomeJ1(nomeJ1);
        setNomeJ2(nomeJ2);
        setPontosJ1(0);
        setPontosJ2(0);
        setAcertosJ1(0);
        setAcertosJ2(0);
        setErrosJ1(0);
        setErrosJ2(0);
    }

    public String getNomeJ1() {
        return nomeJ1;
    }

    public void setNomeJ1(String nome) {
        if (nome == null) {
            throw new NullPointerException("Nome não pode ser nulo!");
        }
        this.nomeJ1 = nome;
    }

    public String getNomeJ2() {
        return nomeJ2;
    }

    public void setNomeJ2(String nome) {
        if (nome == null) {
            throw new NullPointerException("Nome não pode ser nulo!");
        }
        this.nomeJ2 = nome;
    }

    public int getPontosJ1() {
        return pontosJ1;
    }

    public void setPontosJ1(int pontosJ1) {
        this.pontosJ1 = pontosJ1;
    }

    public int getPontosJ2() {
        return pontosJ2;
    }

    public void setPontosJ2(int pontosJ2) {
        this.pontosJ2 = pontosJ2;
    }

    public int getAcertosJ1() {
        return acertosJ1;
    }

    public void setAcertosJ1(int acertos) {
        if (acertos < 0) {
            throw new IllegalArgumentException("Acertos não podem ser negativos!");
        }
        this.acertosJ1 = acertos;
    }

    public int getAcertosJ2() {
        return acertosJ2;
    }

    public void setAcertosJ2(int acertos) {
        if (acertos < 0) {
            throw new IllegalArgumentException("Acertos não podem ser negativos!");
        }
        this.acertosJ2 = acertos;
    }

    public int getErrosJ1() {
        return errosJ1;
    }

    public void setErrosJ1(int erros) {
        if (erros < 0) {
            throw new IllegalArgumentException("Erros não podem ser negativos!");
        }
        this.errosJ1 = erros;
    }

    public int getErrosJ2() {
        return errosJ2;
    }

    public void setErrosJ2(int erros) {
        if (erros < 0) {
            throw new IllegalArgumentException("Erros não podem ser negativos!");
        }
        this.errosJ2 = erros;
    }

    public void aumentarPontos(String j) {
        if (j.equals(nomeJ1)) {
            setPontosJ1(getPontosJ1() + 1);
        } else if (j.equals(nomeJ2)) {
            setPontosJ2(getPontosJ2() + 1);
        } else {
            throw new IllegalArgumentException("Jogador inexistente");
        }
    }

    public void aumentarAcertos(String j) {
        if (j.equals(nomeJ1)) {
            setAcertosJ1(getAcertosJ1() + 1);
        } else if (j.equals(nomeJ2)) {
            setAcertosJ2(getAcertosJ2() + 1);
        } else {
            throw new IllegalArgumentException("Jogador inexistente");
        }
    }

    public void aumentarErros(String j) {
        if (j.equals(nomeJ1)) {
            setErrosJ1(getErrosJ1() + 1);
        } else if (j.equals(nomeJ2)) {
            setErrosJ2(getErrosJ2() + 1);
        } else {
            throw new IllegalArgumentException("Jogador inexistente");
        }
    }
}
