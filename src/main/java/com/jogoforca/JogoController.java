package com.jogoforca;

import com.jogoforca.model.*;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("CallToPrintStackTrace")
public class JogoController {

    private final Random random = new Random();

    // FXML do jogo
    @FXML private ImageView imgForca;
    @FXML private ImageView imgBase;
    @FXML private Label txtRodada;
    @FXML private Label txtPalavra;
    @FXML private Label txtCategoria;
    @FXML private Label txtLetrasUsadas;
    @FXML private Label txtDica;
    @FXML private Label txtPontosJ1;
    @FXML private Label txtPontosJ2;
    @FXML private Label txtFimDoJogo;
    @FXML private Button btnDica;
    @FXML private BorderPane ctnJogo;
    @FXML private FlowPane painelTeclado;
    @FXML private AnchorPane ctnFimDeRodada;

    // FXML do fim do jogo
    @FXML private Label txtVencedor;
    @FXML private Label txtFinalJ1;
    @FXML private Label txtFinalJ2;
    @FXML private Label txtAcertosLetrasJ1;
    @FXML private Label txtAcertosLetrasJ2;
    @FXML private Label txtErrosLetrasJ1;
    @FXML private Label txtErrosLetrasJ2;
    @FXML private AnchorPane ctnFimDeJogo;

    // Dados do jogo
    private Jogada jogada;
    private Jogadores jogadores;
    private Computador comp;
    private Partida partida;


    @FXML public void initialize() {
        carregarImgDica();
    }

    @FXML public void onReiniciarJogo() {
        partida.reiniciarPartida();
        iniciarPartida();
    }

    @FXML public void onTerminarJogo() {

        if (jogadores.getPontosJ1() > jogadores.getPontosJ2()) {
            txtVencedor.setText(jogadores.getNomeJ1() + " Venceu!");
        } else if (jogadores.getPontosJ2() > jogadores.getPontosJ1()) {
            txtVencedor.setText(jogadores.getNomeJ2() + " Venceu!");
        } else {
            txtVencedor.setText("Empate");
        }

        txtFinalJ1.setText(jogadores.getNomeJ1() + ": " + jogadores.getPontosJ1());
        txtFinalJ2.setText(jogadores.getNomeJ2() + ": " + jogadores.getPontosJ2());
        txtAcertosLetrasJ1.setText("Letras Acertadas: " + jogadores.getAcertosJ1());
        txtAcertosLetrasJ2.setText("Letras Acertadas: " + jogadores.getAcertosJ2());
        txtErrosLetrasJ1.setText("Letras Erradas: " + jogadores.getErrosJ1());
        txtErrosLetrasJ2.setText("Letras Erradas: " + jogadores.getErrosJ2());

        // Animação
        ctnFimDeJogo.setVisible(true);
        ctnFimDeJogo.setTranslateX(800);

        TranslateTransition entrada = new TranslateTransition(Duration.millis(375), ctnFimDeJogo);
        entrada.setToX(0);
        TranslateTransition saida = new TranslateTransition(Duration.millis(375), ctnJogo);
        saida.setToX(-800);

        ParallelTransition animacao = new ParallelTransition(entrada, saida);
        animacao.play();
    }

    @FXML public void onVoltarMenu(javafx.event.ActionEvent event) {
        // Deixa a tela do jogo invisível
        ctnJogo.setVisible(false);
        try {
            // Carrega o arquivo do menu
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent raizMenu = fxmlLoader.load();

            // Carrega o menu na esquerda
            raizMenu.setTranslateX(-800);

            TranslateTransition saidaJogo = animarTrocaCena(event, raizMenu);

            saidaJogo.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TranslateTransition animarTrocaCena(ActionEvent event, Parent raizMenu) {
        // Pega a tela inteira do menu
        Node noAtual = (Node) event.getSource();
        Scene cenaAtual = noAtual.getScene();
        Parent raizJogo = noAtual.getScene().getRoot();

        // Configura a animação
        TranslateTransition saidaMenu = new TranslateTransition(Duration.millis(250), raizJogo);
        saidaMenu.setToX(800);
        // Troca a cena quando terminar a saida do menu
        saidaMenu.setOnFinished(_ -> {
            cenaAtual.setRoot(raizMenu);

            // Animação de entrada da tela do jogo
            TranslateTransition entradaJogo = new TranslateTransition(Duration.millis(250), raizMenu);
            entradaJogo.setToX(0);
            entradaJogo.play();
        });
        return saidaMenu;
    }

    @FXML public void onFecharJogo() {
        javafx.application.Platform.exit();
    }

    @FXML public void onDica() {
        ArrayList<Character> letrasRestantes = jogada.getLetrasRestantes();
        // Verifica se falta mais de 1 letra para dar a dica
        if (letrasRestantes.size() <= 1) {
            btnDica.setDisable(true);
        }

        // Escolhe uma letra aleatória da palavra para mostrar
        char letraEscolhida;
        do {
            letraEscolhida = letrasRestantes.get(random.nextInt(jogada.getLetrasRestantes().size()));
            letraEscolhida = jogada.verificarAcento(letraEscolhida);
        } while (letraEscolhida < 'A' || 'Z' < letraEscolhida);

        // Procura o botão que corresponde a letra e "clica" nele
        for (Node node: painelTeclado.getChildren()) {
            if (node instanceof Button btn) {
                if (btn.getText().equals(String.valueOf(letraEscolhida))) {
                    btn.fire();
                    break;
                }
            }
        }

        // Aumenta o erro como penalidade e diminui a quantidade de dicas
        partida.aumentarErros();
        atualizarForca();
        partida.diminuirDicas();
        txtDica.setText("Dicas: " +  partida.getQtdDicas());

        // Desativa o botão se acabar as dicas, se tiver apenas 1 letra restante ou se faltar 1 erro para a derrota
        if (partida.getQtdDicas() <= 0 || letrasRestantes.size() <= 1 || partida.getErros() == 5) {
            btnDica.setDisable(true);
        }
    }

    // Recebe os nomes dos jogadores do MenuController
    public void setJogadores(String j1, String j2, String categoria, String dificuldade) {
        partida = new Partida(categoria, dificuldade);

        if (!dificuldade.isBlank()) {
            jogadores = new Jogadores(j1, "COMP");
            this.comp = new Computador(partida.getDificuldade());
        } else {
            jogadores = new Jogadores(j1, j2);
        }
        iniciarPartida();
    }

    // Prepara o jogo para cada rodada
    public void iniciarPartida() {
        partida.carregarPalavras();

        try {
            jogada = new Jogada(partida.sortearPalavra());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (partida.isModoContraComputador()) {
            switch (partida.getDificuldade()) {
                case "FÁCIL", "NORMAL":
                    btnDica.setDisable(false);
                    break;
                case "DIFÍCIL":
                    btnDica.setDisable(true);
                    btnDica.setVisible(false);
                    txtDica.setVisible(false);
            }
        } else {
            btnDica.setDisable(false);
        }

        if (partida.getVezJogador().equals(jogadores.getNomeJ2())
                || partida.getVezJogador().isEmpty()) {
            partida.setVezJogador(jogadores.getNomeJ1());

            if (partida.isModoContraComputador()) {
                btnDica.setDisable(false);
                painelTeclado.setDisable(false);
            }
        } else {
            partida.setVezJogador(jogadores.getNomeJ2());

            if (partida.isModoContraComputador()) {
                btnDica.setDisable(true);
                jogadaComputador();
            }
        }

        ctnFimDeRodada.setVisible(false);
        txtFimDoJogo.setVisible(false);

        txtLetrasUsadas.setText("");
        txtDica.setText("Dicas: " +  partida.getQtdDicas());
        txtPontosJ1.setText(jogadores.getNomeJ1() + ": " + jogadores.getPontosJ1());
        txtPontosJ2.setText(jogadores.getNomeJ2() + ": " + jogadores.getPontosJ2());
        txtRodada.setText("Vez de " + partida.getVezJogador());

        txtCategoria.setText(jogada.getPalavra().getCategoria());
        txtPalavra.setText(jogada.getPalavraOculta());
        atualizarForca();
        criarTeclado();
    }

    // Atualiza a imagem da forca conforme os erros
    private void atualizarForca() {
        try {
            // Busca o nome da imagem correspondente ao número de erros
            String nomeImagem = "/img/forca" + partida.getErros() + ".png";
            // Irá procurar pela imagem no resource da classe atual
            InputStream imgStream = JogoController.class.getResourceAsStream(nomeImagem);

            if (imgStream != null) {
                Image imagem = new Image(imgStream);

                if (partida.getErros() == 0) {
                    imgBase.setImage(null);
                    imgForca.setImage(imagem);
                    imgForca.setClip(null);
                } else {
                    imgBase.setImage(imgForca.getImage());
                    imgForca.setImage(imagem);

                    // Animação
                    Rectangle quadro = new Rectangle(0, 0);
                    imgForca.setClip(quadro);

                    Timeline animacaoDesenho = new Timeline(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(quadro.widthProperty(), 0),
                                    new KeyValue(quadro.heightProperty(), 0)
                            ),
                            new KeyFrame(Duration.millis(375),
                                    new KeyValue(quadro.widthProperty(), 250),
                                    new KeyValue(quadro.heightProperty(), 250)
                            )
                    );

                    animacaoDesenho.play();
                }

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
        // “Loop” para criar todas as letras
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
            btn.setOnAction(_ -> {
                btn.setDisable(true);
                boolean acertou = jogada.tentarLetra(btn.getText().charAt(0));

                // Atualiza o texta que mostra as letras que já foram jogadas
                if (jogada.getLetrasUsadas().size() == 1) {
                    txtLetrasUsadas.setText(btn.getText());
                } else {
                    txtLetrasUsadas.setText(txtLetrasUsadas.getText() + "-" + btn.getText());
                }

                if (acertou) {
                    jogadores.aumentarAcertos(partida.getVezJogador());
                    txtPalavra.setText(jogada.getPalavraOculta());
                    jogada.removerLetra(btn.getText().charAt(0));

                    // Desativa o botão de dica se sobrar apenas 1 letra para adivinhar
                    if (jogada.getLetrasRestantes().size() <= 1) {
                        btnDica.setDisable(true);
                        txtDica.setText("Dicas: 0");
                    }

                    // Desativa o teclado de novo para o jogador não jogar na vez do COMP
                    if (partida.isModoContraComputador() &&
                            partida.getVezJogador().equals(jogadores.getNomeJ2())) {
                        painelTeclado.setDisable(true);
                    }

                    // Atualiza a pontuação
                    if (partida.vitoria(jogada.getPalavraOculta())) {
                        jogadores.aumentarPontos(partida.getVezJogador());

                        if (partida.getVezJogador().equals(jogadores.getNomeJ1())) {
                            txtPontosJ1.setText((jogadores.getNomeJ1() + ": " + jogadores.getPontosJ1()));
                        } else {
                            txtPontosJ2.setText((jogadores.getNomeJ2() + ": " + jogadores.getPontosJ2()));
                        }
                        // Mostra a mensagem de vitória
                        txtFimDoJogo.setText(partida.getVezJogador() + " acertou!");
                        txtFimDoJogo.setVisible(true);
                        // Troca o teclado pelos botões para o fim de rodada
                        painelTeclado.setVisible(false);
                        ctnFimDeRodada.setVisible(true);
                    } else if (partida.isModoContraComputador() &&
                            partida.getVezJogador().equals(jogadores.getNomeJ2())) {
                        jogadaComputador();
                    }
                } else {
                    jogadores.aumentarErros(partida.getVezJogador());
                    partida.aumentarErros();
                    atualizarForca();

                    if (partida.derrota()) {
                        txtFimDoJogo.setText("Ninguém venceu!");
                        txtFimDoJogo.setVisible(true);
                        // Mostra a palavra
                        txtPalavra.setText(jogada.getPalavra().getPalavra());

                        btnDica.setDisable(true);
                        painelTeclado.setVisible(false);
                        ctnFimDeRodada.setVisible(true);
                    }
                    // Muda a vez dos jogadores
                    if (partida.getVezJogador().equals(jogadores.getNomeJ1())) {
                        partida.setVezJogador(jogadores.getNomeJ2());

                        // Manda o computador jogar na vez dele
                        if (partida.isModoContraComputador() && !partida.derrota()) {
                            btnDica.setDisable(true);
                            jogadaComputador();
                        }
                    } else {
                        partida.setVezJogador(jogadores.getNomeJ1());

                        // Desbloqueia o teclado caso o computador tenha errado
                        if (partida.isModoContraComputador()) {
                            btnDica.setDisable(false);
                            painelTeclado.setDisable(false);
                        }
                    }
                    txtRodada.setText("Vez de " + partida.getVezJogador());
                }
            });

            // Adiciona o botão
            painelTeclado.getChildren().add(btn);
        }
    }

    private void jogadaComputador() {
        // Bloqueia o teclado para o jogador não jogar
        painelTeclado.setDisable(true);

        // Espera 2 segundos para não ser instantâneo
        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(_ -> {
            // Computador escolhe a letra
            char letra = comp.escolherLetra(jogada.getLetrasUsadas(), jogada.getLetrasRestantes());
            letra = jogada.verificarAcento(letra);
            // Clica no botão da letra escolhida
            for (Node node : painelTeclado.getChildren()) {
                if (node instanceof Button btn) {
                    if (btn.getText().equals(String.valueOf(letra))) {
                        painelTeclado.setDisable(false);
                        btn.fire();
                        break;
                    }
                }
            }
        });

        pausa.play();
    }

    private void carregarImgDica() {
        try {
            InputStream imgStream = JogoController.class.getResourceAsStream("/img/imgBtnDica.png");

            if (imgStream != null) {
                Image img = new Image(imgStream);
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(58);
                imgView.setFitWidth(70);
                btnDica.setText("");
                btnDica.setGraphic(imgView);
            } else {
                System.out.println("Imagem não encontrada");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
