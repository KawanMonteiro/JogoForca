package com.jogoforca.model;

import java.util.ArrayList;
import java.util.Locale;

public class Jogada {

    final private int limiteErros = 6;
    private Palavra palavra;
    private ArrayList<Character> letrasUsadas, letrasRestantes;
    private int erros;

    public Jogada(Palavra palavra) {
        this.palavra = palavra;
        this.letrasUsadas = new ArrayList<>();
        this.erros = 0;
        setLetras();
    }

    public ArrayList<Character> getLetrasRestantes() {
        return letrasRestantes;
    }

    public ArrayList<Character> getLetrasUsadas() {
        return letrasUsadas;
    }

    private void setLetras() {
        letrasRestantes = new ArrayList<>();
        char letra;

        // Adiciona as letras na lista se elas já não estiverem nela
        for (int i = 0; i < palavra.getPalavra().length(); i++) {
            letra = palavra.getPalavra().charAt(i);

            if (!letrasRestantes.contains(letra)) {
                letrasRestantes.add(letra);
            }
        }
    }

    public Palavra getPalavra() {
        return palavra;
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
            if (letrasUsadas.contains(verificarAcento(tempPalavra.charAt(i)))) {
                palavraOculta.append(tempPalavra.charAt(i));
                palavraOculta.append(" ");
            } else {
                palavraOculta.append('_');
                palavraOculta.append(' ');
            }
        }

        return palavraOculta.toString().toUpperCase().trim();
    }

    public void aumentarErros() {
        this.erros++;
    }

    public void removerLetra(char letra) {
        letrasRestantes.remove(Character.valueOf(letra));
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

    public char verificarAcento(char letra) {
        // Verifica se a letra está no alfabeto sem acento
        if (letra >= 'A' && letra <= 'Z') return letra;

        // Verifica qual letra é
        String letrasAcento = "ÁÂÃÉÊÍÎÓÔÕÚÛÇ";
        for (int i = 0; i < letrasAcento.length(); i++) {
            if (letra == letrasAcento.charAt(i)) {
                switch (i) {
                    case 0, 1, 2: return 'A';
                    case 3, 4: return 'E';
                    case 5, 6: return 'I';
                    case 7, 8, 9: return 'O';
                    case 10, 11: return 'U';
                    case 12: return 'C';
                }
            }
        }
        throw new IllegalArgumentException("Letra desconhecida");
    }

    public boolean vitoria() {
        return !getPalavraOculta().contains("_");
    }

    public boolean derrota() {
        return erros >= limiteErros;
    }
}
