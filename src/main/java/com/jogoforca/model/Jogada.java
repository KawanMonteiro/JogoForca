package com.jogoforca.model;

import java.util.ArrayList;
import java.util.Locale;

public class Jogada {

    final private int limiteErros = 6;
    private Palavra palavra;
    private ArrayList<Character> letrasUsadas;
    private int erros;

    public Jogada(Palavra palavra) {
        this.palavra = palavra;
        this.letrasUsadas = new ArrayList<>();
        this.erros = 0;
    }

    public Palavra getPalavra() {
        return palavra;
    }

    public ArrayList<Character> getLetrasUsadas() {
        return letrasUsadas;
    }

    public int getErros() {
        return erros;
    }

    // Retorna a palavra apenas com a letras que já foram acertadas
    public String getPalavraOculta() {
        String tempPalavra = palavra.getPalavra();
        StringBuilder palavraOculta = new StringBuilder();
        for (int i = 0; i < tempPalavra.length(); i++) {
            // Adiciona a letra na palavraOculta se estiver na lista de letras usadas
            if (letrasUsadas.contains(tempPalavra.charAt(i))) {
                palavraOculta.append(tempPalavra.charAt(i));
                palavraOculta.append(" ");
            } else {
                palavraOculta.append('_');
                palavraOculta.append(' ');
            }
        }

        return palavraOculta.toString().toUpperCase().trim();
    }

    public boolean tentarLetra(char letra) {
        // Verifica se a letra já foi jogada antes
        if (letrasUsadas.contains(letra)) {
            throw new IllegalArgumentException("Letra já jogada!");
        }
        letrasUsadas.add(letra);
        // Verifica se a letra está na palavra
        for (int i = 0; i < palavra.getPalavra().length(); i++) {
            if (palavra.getPalavra().charAt(i) == letra) return true;
        }
        erros++;
        return false;
    }

    public boolean vitoria() {
        return !getPalavraOculta().contains("_");
    }

    public boolean derrota() {
        return erros >= limiteErros;
    }
}
