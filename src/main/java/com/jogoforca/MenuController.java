package com.jogoforca;

import com.jogoforca.model.Dificuldade;
import com.jogoforca.view.TransicoesTela;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Logger;


public class MenuController {
    Logger logger = Logger.getLogger(MenuController.class.getName());

    // Containers do Menu Inicial e Cadastros
    @FXML private VBox ctnMenuInicial;
    @FXML private VBox ctnCadastroDoisJogadores;
    @FXML private VBox ctnCadastroUmJogador;

    // Caixas de texto com os nomes dos jogadores
    @FXML private TextField inputJogadorUm;
    @FXML private TextField inputJogadorDois;
    @FXML private TextField inputJogador;

    // Categorias e dificuldade
    @FXML private ComboBox<String> cbCategorias;
    @FXML private ComboBox<String> cbCategoriasComp;
    @FXML private ComboBox<Dificuldade> cbDificuldade;

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
        TransicoesTela.trocarTela(ctnMenuInicial, ctnCadastroUmJogador, 1);
    }

    @FXML protected void onClicarDoisJogadores() {
        TransicoesTela.trocarTela(ctnMenuInicial, ctnCadastroDoisJogadores, 1);
    }

    @FXML protected void onVoltarMenu() {
        Node telaAtual = ctnCadastroUmJogador.isVisible() ? ctnCadastroUmJogador : ctnCadastroDoisJogadores;
        TransicoesTela.trocarTela(telaAtual, ctnMenuInicial, -1);
    }

    @FXML protected void onJogarComp(ActionEvent event) {
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

        String categoria = cbCategoriasComp.getValue();
        Dificuldade dificuldade = cbDificuldade.getValue();

        iniciarJogo(event, jogador, null, categoria, dificuldade);
    }

    @FXML protected void onJogar(ActionEvent event) {
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

        String categoria = cbCategorias.getValue();

        iniciarJogo(event, jogador1, jogador2, categoria, null);
    }

    private void iniciarJogo(ActionEvent event, String jogador1, String jogador2, String categoria, Dificuldade dificuldade) {
        try {
            // Carrega o arquivo do jogo
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jogo.fxml"));
            Parent raizJogo = fxmlLoader.load();

            // Carrega o controlador da partida
            JogoController controller = fxmlLoader.getController();
            controller.setMotor(jogador1, jogador2, categoria, dificuldade);

            TransicoesTela.trocarCena(event, raizJogo, 1);
        } catch (IOException e) {
            logger.severe("Algum erro ocorreu:");
            logger.severe(e.toString());
        }
    }

    private void criarCategorias() {
        cbCategorias.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
        cbCategoriasComp.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
    }

    private void setDificuldade() {
        cbDificuldade.getItems().addAll(
                Dificuldade.FACIL, Dificuldade.NORMAL, Dificuldade.DIFICIL
        );
    }
}
