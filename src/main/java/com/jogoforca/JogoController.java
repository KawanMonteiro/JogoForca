package com.jogoforca;

import com.jogoforca.model.Jogada;
import com.jogoforca.model.Palavra;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class JogoController {

    // Dados do FXML
    @FXML private ImageView imgForca;
    @FXML private Label txtRodada;
    @FXML private Label txtPalavra;
    @FXML private Label txtDica;
    @FXML private Label txtPontosJ1;
    @FXML private Label txtPontosJ2;
    @FXML private Label txtFimDoJogo;
    @FXML private FlowPane painelTeclado;
    @FXML private AnchorPane containerFimDeRodada;

    // Dados do jogo
    private boolean modoContraComputador;
    private String nomeJogador1;
    private String nomeJogador2;
    private String categoria;
    private String dificuldade;
    private String vezJogador = "";
    private int pontosJ1 = 0;
    private int pontosJ2 = 0;
    private Jogada jogada;
    private int errosAtuais;
    private List<String> palavras = new ArrayList<>();


    @FXML public void initialize() {}

    @FXML public void onReiniciarJogo() {
        rodada();
    }

    // Recebe os nomes dos jogadores do MenuController
    public void setJogadores(String j1, String j2, String categoria, String dificuldade) {
        this.nomeJogador1 = j1.trim().toUpperCase();
        this.categoria = categoria.trim().toUpperCase();

        if (j2 == null) {
            this.nomeJogador2 = "COMP";
            this.modoContraComputador = true;
            this.dificuldade = dificuldade;
        } else {
            this.nomeJogador2 = j2.trim().toUpperCase();
            this.modoContraComputador = false;
        }
        rodada();
    }

    // Atualiza a imagem da forca conforme os erros
    private void atualizarForca() {
        try {
            // Busca o nome da imagem correspondente ao número de erros
            String nomeImagem = "/img/forca" + errosAtuais + ".png";

            // Irá procurar pela imagem no resource da classe atual
            InputStream img = JogoController.class.getResourceAsStream(nomeImagem);
            if (img != null) {
                imgForca.setImage(new Image(img));
            } else {
                System.out.println("Imagem não encontrada: " + nomeImagem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cria o teclado para o jogo
    private void criarTeclado() {
        // Apaga o teclado da rodada anterior
        painelTeclado.getChildren().clear();
        painelTeclado.setVisible(true);
        // Loop para criar todas as letras
        for (char letra = 'A'; letra<= 'Z'; letra++) {
            Button btn = new Button(String.valueOf(letra));
            btn.setPrefWidth(50);
            btn.setPrefHeight(50);
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 3px;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-family: 'Segoe Print';" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 16px;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-radius: 30;"
            );

            // Função lambda para quando o botão for clicado
            btn.setOnAction(event -> {
                btn.setDisable(true);
                boolean acertou = jogada.tentarLetra((btn.getText().charAt(0)));
                if (acertou) {
                    txtPalavra.setText(jogada.getPalavraOculta());

                    // Atualiza a pontuação
                    if (jogada.vitoria()) {
                        if (vezJogador.equals(nomeJogador1)) {
                            pontosJ1++;
                            txtPontosJ1.setText((nomeJogador1 + ": " + pontosJ1));
                        } else {
                            pontosJ2++;
                            txtPontosJ2.setText((nomeJogador2 + ": " + pontosJ2));
                        }
                        // Mostra a mensagem de vitória
                        txtFimDoJogo.setText(vezJogador + " venceu!");
                        txtFimDoJogo.setVisible(true);
                        // Troca o teclado pelos botões para o fim de rodada
                        painelTeclado.setVisible(false);
                        containerFimDeRodada.setVisible(true);
                    }
                } else {
                    errosAtuais = jogada.getErros();
                    atualizarForca();

                    if (jogada.derrota()) {
                        txtFimDoJogo.setText("Ninguém venceu!");
                        txtFimDoJogo.setVisible(true);
                        // Mostra a palavra
                        txtPalavra.setText(jogada.getPalavra().getPalavra());

                        painelTeclado.setVisible(false);
                        containerFimDeRodada.setVisible(true);
                    }
                    // Muda a vez dos jogadores
                    if (vezJogador.equals(nomeJogador1)) {
                        vezJogador = nomeJogador2;
                    } else {
                        vezJogador = nomeJogador1;
                    }
                    txtRodada.setText("Vez de " + vezJogador);
                }
            });

            // Adiciona o botão
            painelTeclado.getChildren().add(btn);
        }
    }

    public void rodada() {
        carregarPalavras();
        try {
            jogada = new Jogada(sortearPalavra());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (vezJogador.equals(nomeJogador2) || vezJogador.isEmpty()) {
            vezJogador = nomeJogador1;
        } else {
            vezJogador = nomeJogador2;
        }
        containerFimDeRodada.setVisible(false);
        txtFimDoJogo.setVisible(false);

        txtPontosJ1.setText(nomeJogador1 + ": " + pontosJ1);
        txtPontosJ2.setText(nomeJogador2 + ": " + pontosJ2);
        txtRodada.setText("Vez de " + vezJogador);

        errosAtuais = jogada.getErros();
        txtDica.setText(jogada.getPalavra().getCategoria());
        txtPalavra.setText(jogada.getPalavraOculta());
        atualizarForca();
        criarTeclado();
    }

    public void carregarPalavras() {
        if (!palavras.isEmpty()) {
            return;
        }
        try {
            // Procura o aquivo que contém as palavras
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Palavra sortearPalavra() {
        // Sorteia uma linha conforme o tamanho da lista de palavras
        Random random = new Random();
        String linha = palavras.get(random.nextInt(palavras.size()));
        // Cria a palavra com a classe Palavra
        String[] palavraCategoria = linha.split(";");
        if (palavraCategoria.length == 2) {
            return new Palavra(palavraCategoria[0].trim().toUpperCase(), palavraCategoria[1].trim().toUpperCase());
        } else {
            throw new IllegalArgumentException("Arquivo 'palavras.txt' mal formatado!");
        }
    }
}
