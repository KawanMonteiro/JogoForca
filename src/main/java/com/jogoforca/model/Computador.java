package com.jogoforca.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Computador {

    private int acertosConsecutivos = 0;
    private final String dificuldade;
    private final Random random = new Random();

    public Computador(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public char escolherLetra(List<Character> letrasUsadas, List<Character> letrasRestantes) {
        List<Character> alfabetoErro = new ArrayList<>();
        // Preenche o alfabetoErro sem as letras certas e sem letras que já foram usadas
        for (char letra = 'A'; letra <= 'Z'; letra++) {
            if (!letrasUsadas.contains(letra) && !letrasRestantes.contains(letra)) {
                alfabetoErro.add(letra);
            }
        }

        // Se só faltar uma letra ele sempre acerta
        if (letrasRestantes.size() == 1) {
            return letrasRestantes.getFirst();
        }

        char letraSorteada;
        // Lógica de acerto dependendo da dificuldade
        int aleatorio = random.nextInt(1, 10);
        switch (dificuldade) {
            case "FÁCIL":
                if (aleatorio <= 8 || acertosConsecutivos > 1) {
                    letraSorteada = alfabetoErro.get(random.nextInt(alfabetoErro.size()));
                    acertosConsecutivos = 0;
                } else {
                    letraSorteada = letrasRestantes.get(random.nextInt(letrasRestantes.size()));
                    acertosConsecutivos++;
                }
                break;
            case "NORMAL":
                if (aleatorio <= 6 || acertosConsecutivos > 2) {
                    letraSorteada = alfabetoErro.get(random.nextInt(alfabetoErro.size()));
                    acertosConsecutivos = 0;
                } else {
                    letraSorteada = letrasRestantes.get(random.nextInt(letrasRestantes.size()));
                    acertosConsecutivos++;
                }
                break;
            case "DIFÍCIL":
                if (aleatorio <= 3 || acertosConsecutivos > 4) {
                    letraSorteada = alfabetoErro.get(random.nextInt(alfabetoErro.size()));
                    acertosConsecutivos = 0;
                } else {
                    letraSorteada = letrasRestantes.get(random.nextInt(letrasRestantes.size()));
                    acertosConsecutivos++;
                }
                break;
            default:
                letraSorteada = alfabetoErro.get(random.nextInt(alfabetoErro.size()));
        }
        return letraSorteada;
    }
}
