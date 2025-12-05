package com.jogoforca;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    // Containers do Menu Inicial e Cadastros
    @FXML private HBox containerMenuInicial;
    @FXML private VBox containerCadastroDoisJogadores;
    @FXML private VBox containerCadastroUmJogador;

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
        mostrarTela(containerMenuInicial);
        criarCategorias();
        setDificuldade();
    }

    @FXML protected void onClicarUmJogador() {
        mostrarTela(containerCadastroUmJogador);
    }

    @FXML protected void onClicarDoisJogadores() {
        mostrarTela(containerCadastroDoisJogadores);
    }

    @FXML protected void onVoltarMenu() {
        mostrarTela(containerMenuInicial);
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

    private void iniciarJogo(ActionEvent event, String jogador1, String jogador2, String categoria, String dificuldade)
            throws IOException {
        // Carrega o arquivo jogo.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jogo.fxml"));
        // Cria a cena com o arquivo
        Scene cenaJogo = new Scene(fxmlLoader.load(), 800, 600);

        // Carrega o controlador da partida
        JogoController controller = fxmlLoader.getController();

        controller.setJogadores(jogador1, jogador2, categoria, dificuldade);

        // Pega o stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(cenaJogo); // Muda para a cena do jogo
        stage.show();
    }

    // Esconde todas as telas e mostra somente a tela que for pedida
    private void mostrarTela(Node tela) {
        containerMenuInicial.setVisible(false);
        containerMenuInicial.setManaged(false);

        containerCadastroUmJogador.setVisible(false);
        containerCadastroUmJogador.setManaged(false);

        containerCadastroDoisJogadores.setVisible(false);
        containerCadastroDoisJogadores.setManaged(false);

        tela.setVisible(true);
        tela.setManaged(true);
    }

    private void criarCategorias() {
        cbCategorias.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
        cbCategoriasComp.getItems().addAll("Todas", "Objeto", "Animal", "País", "Fruta");
    }

    private void setDificuldade() {
        cbDificuldade.getItems().addAll("Fácil", "Difícil");
    }
}
