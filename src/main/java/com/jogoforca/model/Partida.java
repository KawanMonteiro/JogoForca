package com.jogoforca.model;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("CallToPrintStackTrace")
public class Partida {

    private static final int LIMITE_ERROS = 6;
    private final Random random = new Random();
    private final List<String> palavras = new ArrayList<>();
    private String categoria;
    private Dificuldade dificuldade;
    private String vezJogador;
    private int qtdDicas, erros;
    private boolean modoContraComputador;

    public Partida(String categoria, Dificuldade dificuldade) {
        setCategoria(categoria);
        setDificuldade(dificuldade);
        setModoContraComputador();

        setVezJogador("");
        setQtdDicas();
        setErros();

        carregarPalavras();
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria.trim().toUpperCase();
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getVezJogador() {
        return vezJogador;
    }

    public void setVezJogador(String vezJogador) {
        this.vezJogador = vezJogador;
    }

    public int getQtdDicas() {
        return qtdDicas;
    }

    public void setQtdDicas() {
        if (dificuldade == null) {
            qtdDicas = 1;
            return;
        }

        switch (dificuldade) {
            case Dificuldade.FACIL:
                qtdDicas = 3;
                break;
            case Dificuldade.NORMAL:
                qtdDicas = 1;
                break;
            case Dificuldade.DIFICIL:
                qtdDicas = 0;
        }
    }

    public int getErros() {
        return erros;
    }

    public void setErros() {
        this.erros = 0;
    }

    public boolean isModoContraComputador() {
        return modoContraComputador;
    }

    public void setModoContraComputador() {
        modoContraComputador = !(dificuldade == null);
    }

    public void reiniciarPartida() {
        setErros();
        setQtdDicas();
    }

    public void aumentarErros() {
        erros++;
    }

    public void diminuirDicas() {
        qtdDicas--;
    }

    public void carregarPalavras() {
        if (!palavras.isEmpty()) {
            return;
        }
        try {
            // Procura o arquivo que contém as palavras
            InputStream is = getClass().getResourceAsStream("/dados/palavras.txt");
            if (is == null) {
                System.out.println("Arquivo não encontrado");
                return;
            }
            // Abre o arquivo com Scanner
            Scanner sc = new Scanner((new InputStreamReader(is)));

            // Adiciona as palavras conforme a categoria escolhida pelo jogador
            if (categoria.equals("TODAS")) {
                while (sc.hasNextLine()) {
                    palavras.add(sc.nextLine());
                }
            } else {
                String[] linha;

                while (sc.hasNextLine()) {
                    linha = sc.nextLine().split(";");
                    if (categoria.equals(linha[1].trim().toUpperCase())) {
                        palavras.add(linha[0]+";"+linha[1]);
                    }
                }
            }
            sc.close();


            if (palavras.isEmpty()) {
                throw new IllegalArgumentException("Não há palavras para a categoria escolhida!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Palavra sortearPalavra() {
        // Sorteia uma linha conforme o tamanho da lista de palavras
        String linha = palavras.get(random.nextInt(palavras.size()));

        // Cria a palavra com a classe Palavra
        String[] palavraCategoria = linha.split(";");
        if (palavraCategoria.length == 2) {
            return new Palavra(palavraCategoria[0].trim().toUpperCase(), palavraCategoria[1].trim().toUpperCase());
        } else {
            throw new IllegalArgumentException("Arquivo 'palavras.txt' mal formatado!");
        }
    }

    public boolean vitoria(String palavra) {
        return !palavra.contains("_");
    }

    public boolean derrota() {
        return erros >= LIMITE_ERROS;
    }
}
