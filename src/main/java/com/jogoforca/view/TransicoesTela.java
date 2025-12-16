package com.jogoforca.view;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

public class TransicoesTela {

    public static void trocarTela(Node saindo, Node entrando, int direcao) {
        double largura = 800; // Largura da janela

        // Ajusta a posição inicial da tela que vai entrar
        entrando.setTranslateX(direcao * largura);
        entrando.setVisible(true);
        entrando.setManaged(true);

        // Anima a tela que está saindo
        TranslateTransition sair = new TranslateTransition(Duration.millis(375), saindo);
        sair.setToX(-direcao * largura);
        // Anima a tela que está entrando
        TranslateTransition entrar = new TranslateTransition(Duration.millis(375), entrando);
        entrar.setToX(0);
        // Faz as duas animações ao mesmo tempo
        ParallelTransition animacao = new ParallelTransition(sair, entrar);

        // "Desativa" a tela que saiu
        animacao.setOnFinished(_ -> {
            saindo.setVisible(false);
            saindo.setManaged(false);
        });

        animacao.play();
    }

    public static void trocarCena(ActionEvent event, Parent raizProximaCena, int direcao) {
        // Carrega a próxima cena
        raizProximaCena.setTranslateX(800 * direcao);

        // Pega a tela inteira atual
        Node noAtual = (Node) event.getSource();
        Scene cenaAtual = noAtual.getScene();
        Parent raizCenaAtual = noAtual.getScene().getRoot();

        // Configura a animação
        TranslateTransition transicao = new TranslateTransition(Duration.millis(250), raizCenaAtual);
        transicao.setToX(-direcao * 800);
        // Troca a cena quando terminar a saída da tela atual
        transicao.setOnFinished(_ -> {
            cenaAtual.setRoot(raizProximaCena);

            // Animação de entrada da próxima tela
            TranslateTransition entradaJogo = new TranslateTransition(Duration.millis(250), raizProximaCena);
            entradaJogo.setToX(0);
            entradaJogo.play();
        });
        transicao.play();
    }
}
