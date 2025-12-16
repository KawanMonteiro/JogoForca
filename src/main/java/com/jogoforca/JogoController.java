package com.jogoforca;

import com.jogoforca.model.*;
import com.jogoforca.view.ForcaView;
import com.jogoforca.view.TransicoesTela;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;

import java.util.logging.Logger;


public class JogoController {

    private static final Logger logger = Logger.getLogger(JogoController.class.getName());

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
    @FXML private Label txtFimDaRodada;
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

    // Dados de animação
    private ForcaView forca;

    // Dados do jogo
    private MotorDeJogo motor;
    private boolean isDica;


    @FXML public void initialize() {
        forca = new ForcaView(imgBase, imgForca);
        carregarImgDica();
    }

    @FXML public void onReiniciarJogo() {
        motor.getPartida().reiniciarPartida();
        forca.atualizar(motor.getPartida().getErros());
        motor.iniciarPartida();
        iniciarPartida();
    }

    @FXML public void onTerminarJogo() {

        txtVencedor.setText(motor.getJogadores().getVencedor());

        txtFinalJ1.setText(motor.getJogadores().getNomeJ1() + ": " + motor.getJogadores().getPontosJ1());
        txtFinalJ2.setText(motor.getJogadores().getNomeJ2() + ": " + motor.getJogadores().getPontosJ2());
        txtAcertosLetrasJ1.setText("Letras Acertadas: " + motor.getJogadores().getAcertosJ1());
        txtAcertosLetrasJ2.setText("Letras Acertadas: " + motor.getJogadores().getAcertosJ2());
        txtErrosLetrasJ1.setText("Letras Erradas: " + motor.getJogadores().getErrosJ1());
        txtErrosLetrasJ2.setText("Letras Erradas: " + motor.getJogadores().getErrosJ2());

        TransicoesTela.trocarTela(ctnJogo, ctnFimDeJogo, 1);
    }

    @FXML public void onVoltarMenu(javafx.event.ActionEvent event) {
        // Deixa a tela do jogo invisível
        ctnJogo.setVisible(false);
        try {
            // Carrega o arquivo do menu
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent raizMenu = fxmlLoader.load();

            TransicoesTela.trocarCena(event, raizMenu, -1);
        } catch (Exception e) {
            logger.severe("Algum erro ocorreu:");
            logger.severe(e.toString());
        }
    }

    @FXML public void onFecharJogo() {
        javafx.application.Platform.exit();
    }

    @FXML public void onDica() {
        // Aumenta o erro como penalidade e diminui a quantidade de dicas
        char letraEscolhida = motor.pegarDica();
        forca.atualizar(motor.getPartida().getErros());
        txtDica.setText("Dicas: " +  motor.getPartida().getQtdDicas());

        // Procura o botão que corresponde a letra e "clica" nele
        for (Node node: painelTeclado.getChildren()) {
            if (node instanceof Button btn) {
                if (btn.getText().equals(String.valueOf(letraEscolhida))) {
                    isDica = true;
                    btn.fire();
                    isDica = false;
                    break;
                }
            }
        }

        // Desativa o botão se acabar as dicas, se tiver apenas 1 letra restante ou se faltar 1 erro para a derrota
        boolean semDicasRestantes = motor.getPartida().getQtdDicas() <= 0;
        boolean poucasLetrasRestantes = motor.getJogada().getLetrasRestantes().size() <= 1;
        boolean limiteDeErrosAtingido = motor.getPartida().getErros() == 5;
        if (semDicasRestantes || poucasLetrasRestantes || limiteDeErrosAtingido) {
            btnDica.setDisable(true);
        }
    }

    // Recebe os nomes dos jogadores do MenuController
    public void setMotor(String j1, String j2, String categoria, Dificuldade dificuldade) {
        motor = new MotorDeJogo(j1, j2, categoria, dificuldade);
        iniciarPartida();
    }

    // Prepara o jogo para cada rodada
    public void iniciarPartida() {
        if (motor.getPartida().isModoContraComputador()) {
            switch (motor.getPartida().getDificuldade()) {
                case Dificuldade.FACIL, Dificuldade.NORMAL:
                    btnDica.setDisable(false);
                    break;
                case Dificuldade.DIFICIL:
                    btnDica.setDisable(true);
                    btnDica.setVisible(false);
                    txtDica.setVisible(false);
            }
        } else {
            btnDica.setDisable(false);
        }

        boolean vezJ1 = motor.getPartida().getVezJogador().equals(motor.getJogadores().getNomeJ1());
        boolean modoComp = motor.getPartida().isModoContraComputador();
        if (vezJ1 && modoComp) {
            btnDica.setDisable(false);
            painelTeclado.setDisable(false);
        } else {
            if (modoComp) {
                btnDica.setDisable(true);
                jogadaComputador();
            }
        }

        ctnFimDeRodada.setVisible(false);
        txtFimDaRodada.setVisible(false);

        txtLetrasUsadas.setText("");
        txtDica.setText("Dicas: " +  motor.getPartida().getQtdDicas());
        txtPontosJ1.setText(motor.getJogadores().getNomeJ1() + ": " + motor.getJogadores().getPontosJ1());
        txtPontosJ2.setText(motor.getJogadores().getNomeJ2() + ": " + motor.getJogadores().getPontosJ2());
        txtRodada.setText("Vez de " + motor.getPartida().getVezJogador());

        txtCategoria.setText(motor.getJogada().getPalavra().getCategoria());
        txtPalavra.setText(motor.getJogada().getPalavraOculta());
        forca.atualizar(motor.getPartida().getErros());
        criarTeclado();
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
            btn.getStyleClass().add("btnTeclado");

            // Função lambda para quando o botão for clicado
            btn.setOnAction(_ -> {
                btn.setDisable(true);
                boolean acertou = motor.tentativa(btn.getText().charAt(0), isDica);

                // Atualiza o texta que mostra as letras que já foram jogadas
                if (motor.getJogada().getLetrasUsadas().size() == 1) {
                    txtLetrasUsadas.setText(btn.getText());
                } else {
                    txtLetrasUsadas.setText(txtLetrasUsadas.getText() + "-" + btn.getText());
                }

                boolean modoComp = motor.getPartida().isModoContraComputador();
                boolean vezJ2 = motor.getPartida().getVezJogador().equals(motor.getJogadores().getNomeJ2());
                if (acertou) {
                    txtPalavra.setText(motor.getJogada().getPalavraOculta());

                    // Desativa o botão de dica se sobrar apenas 1 letra para adivinhar
                    if (motor.getJogada().getLetrasRestantes().size() <= 1) {
                        btnDica.setDisable(true);
                        txtDica.setText("Dicas: 0");
                    }

                    // Desativa o teclado de novo para o jogador não jogar na vez do COMP
                    if (modoComp && vezJ2) {
                        painelTeclado.setDisable(true);
                    }

                    // Atualiza a tela em caso de vitória
                    if (motor.getPartida().vitoria(motor.getJogada().getPalavraOculta())) {
                        if (motor.getPartida().getVezJogador().equals(motor.getJogadores().getNomeJ1())) {
                            txtPontosJ1.setText(
                                    (motor.getJogadores().getNomeJ1() + ": " + motor.getJogadores().getPontosJ1())
                            );
                        } else {
                            txtPontosJ2.setText(
                                    (motor.getJogadores().getNomeJ2() + ": " + motor.getJogadores().getPontosJ2())
                            );
                        }
                        // Mostra a mensagem de vitória
                        txtFimDaRodada.setText(motor.getPartida().getVezJogador() + " acertou!");
                        txtFimDaRodada.setVisible(true);
                        // Troca o teclado pelos botões para o fim de rodada
                        painelTeclado.setVisible(false);
                        ctnFimDeRodada.setVisible(true);
                    } else if (modoComp && vezJ2) {
                        jogadaComputador();
                    }
                } else {
                    forca.atualizar(motor.getPartida().getErros());

                    boolean isDerrota = motor.getPartida().derrota();
                    if (isDerrota) {
                        txtFimDaRodada.setText("Ninguém venceu!");
                        txtFimDaRodada.setVisible(true);
                        // Mostra a palavra
                        txtPalavra.setText(motor.getJogada().getPalavra().getPalavra());

                        btnDica.setDisable(true);
                        painelTeclado.setVisible(false);
                        ctnFimDeRodada.setVisible(true);
                    }

                    // Manda o computador jogar na vez dele
                    if (vezJ2 && modoComp && !isDerrota) {
                        btnDica.setDisable(true);
                        jogadaComputador();
                    } else if (modoComp && !isDerrota) {
                        // Desbloqueia o teclado caso o computador tenha errado
                        btnDica.setDisable(false);
                        painelTeclado.setDisable(false);
                    }

                    // Desativa a dica se faltar apenas um erro para a derrota
                    if (motor.getPartida().getErros() == 5) btnDica.setDisable(true);

                    txtRodada.setText("Vez de " + motor.getPartida().getVezJogador());
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
            char letra = motor.getComp().escolherLetra(motor.getJogada().getLetrasUsadas(), motor.getJogada().getLetrasRestantes());
            letra = motor.getJogada().verificarAcento(letra);
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
        // Adiciona o estilo que está no css
        btnDica.getStyleClass().add("btnDica");

        // Carrega a imagem e a implementa no botão
        Image img = Dica.getImagem();
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(58);
        imgView.setFitWidth(70);
        btnDica.setText("");
        btnDica.setGraphic(imgView);
    }
}
