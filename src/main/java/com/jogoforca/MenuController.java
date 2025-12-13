package com.jogoforca;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class MenuController {

    // Containers do Menu Inicial e Cadastros
    @FXML private VBox ctnMenuInicial;
    @FXML private VBox ctnCadastroDoisJogadores;
    @FXML private VBox ctnCadastroUmJogador;

    // Caixas de texto com os nomes dos jogadores
    @FXML private TextField inputJogadorUm;
    @FXML private TextField inputJogadorDois;
    @FXML private TextField inputJogador;

    // Categorias e dificuldade
    @FXML private ComboBox cbCategorias;
    @FXML private ComboBox cbCategoriasComp;
    @FXML private ComboBox cbDificuldade;

    // Textos de aviso
    @FXML private Label txtAviso;
    @FXML private Label txtAvisoComp;

    @FXML public void initialize() {
        criarCategorias();
        setDificuldade();
    }

    @FXML public void onSair() {
        javafx.application.Platform.exit();
    }

    @FXML protected void onClicarUmJogador() {
        animarTroca(ctnMenuInicial, ctnCadastroUmJogador, 1);
    }

    @FXML protected void onClicarDoisJogadores() {
        animarTroca(ctnMenuInicial, ctnCadastroDoisJogadores, 1);
    }

    @FXML protected void onVoltarMenu() {
        Node telaAtual = ctnCadastroUmJogador.isVisible() ? ctnCadastroUmJogador : ctnCadastroDoisJogadores;
        animarTroca(telaAtual, ctnMenuInicial, -1);
    }

    @FXML protected void onJogarComp(ActionEvent event) throws IOException {
        String jogador = inputJogador.getText();

        // Muda o texto de aviso e mostra ele
        if (jogador.isEmpty()) {
            txtAvisoComp.setVisible(true);
            return;
        } else if (cbCategoriasComp.getSelectionModel().isEmpty()) {
            txtAvisoComp.setText("Selecione uma categoria");
            txtAvisoComp.setVisible(true);
            return;
        } else if (cbDificuldade.getSelectionModel().isEmpty()) {
            txtAvisoComp.setText("Selecione uma dificuldade");
            txtAvisoComp.setVisible(true);
            return;
        }

        String categoria = cbCategoriasComp.getValue().toString();
        String dificuldade = cbDificuldade.getValue().toString();

        iniciarJogo(event, jogador, null, categoria, dificuldade);
    }

    @FXML protected void onJogar(ActionEvent event) throws IOException {
        String jogador1 = inputJogadorUm.getText();
        String jogador2 = inputJogadorDois.getText();

        if (jogador1.isEmpty() || jogador2.isEmpty()) {
            txtAviso.setVisible(true);
            return;
        } else if (cbCategorias.getSelectionModel().isEmpty()) {
            txtAviso.setText("Selecione uma categoria");
            txtAviso.setVisible(true);
            return;
        }

        String categoria = cbCategorias.getValue().toString();

        iniciarJogo(event, jogador1, jogador2, categoria, null);
    }

    private void iniciarJogo(ActionEvent event, String jogador1, String jogador2, String categoria, String dificuldade) {
        try {
            // Carrega o arquivo do jogo
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jogo.fxml"));
            Parent raizJogo = fxmlLoader.load();

            // Carrega o controlador da partida
            JogoController controller = fxmlLoader.getController();
            controller.setJogadores(jogador1, jogador2, categoria, dificuldade);

            // Carrega o jogo na direita
            raizJogo.setTranslateX(800);

            TranslateTransition saidaMenu = animarTrocaCena(event, raizJogo);

            saidaMenu.play();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private TranslateTransition animarTrocaCena(ActionEvent event, Parent raizJogo) {
        // Pega a tela inteira do menu
        Node noAtual = (Node) event.getSource();
        Scene cenaAtual = noAtual.getScene();
        Parent raizMenu = noAtual.getScene().getRoot();

        // Configura a animação
        TranslateTransition saidaMenu = new TranslateTransition(Duration.millis(250), raizMenu);
        saidaMenu.setToX(-800);
        // Troca a cena quando terminar a saida do menu
        saidaMenu.setOnFinished(_ -> {
            cenaAtual.setRoot(raizJogo);

            // Animação de entrada da tela do jogo
            TranslateTransition entradaJogo = new TranslateTransition(Duration.millis(250), raizJogo);
            entradaJogo.setToX(0);
            entradaJogo.play();
        });
        return saidaMenu;
    }

    // Faz a animação entre as trocas de telas
    private void animarTroca(Node saindo, Node entrando, int direcao) {
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
        animacao.setOnFinished(e -> {
            saindo.setVisible(false);
            saindo.setManaged(false);
        });

        animacao.play();
    }

    private void criarCategorias() {
        cbCategorias.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
        cbCategoriasComp.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
    }

    private void setDificuldade() {
        cbDificuldade.getItems().addAll("Fácil", "Normal", "Difícil");
    }
}
