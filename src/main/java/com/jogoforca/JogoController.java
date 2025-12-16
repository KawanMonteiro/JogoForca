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
    private Jogada jogada;
    private Jogadores jogadores;
    private Computador comp;
    private Partida partida;
    private boolean isDica;


    @FXML public void initialize() {
        forca = new ForcaView(imgBase, imgForca);
        carregarImgDica();
    }

    @FXML public void onReiniciarJogo() {
        partida.reiniciarPartida();
        forca.atualizar(partida.getErros());
        iniciarPartida();
    }

    @FXML public void onTerminarJogo() {

        txtVencedor.setText(jogadores.getVencedor());

        txtFinalJ1.setText(jogadores.getNomeJ1() + ": " + jogadores.getPontosJ1());
        txtFinalJ2.setText(jogadores.getNomeJ2() + ": " + jogadores.getPontosJ2());
        txtAcertosLetrasJ1.setText("Letras Acertadas: " + jogadores.getAcertosJ1());
        txtAcertosLetrasJ2.setText("Letras Acertadas: " + jogadores.getAcertosJ2());
        txtErrosLetrasJ1.setText("Letras Erradas: " + jogadores.getErrosJ1());
        txtErrosLetrasJ2.setText("Letras Erradas: " + jogadores.getErrosJ2());

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
        char letraEscolhida;
        do {
            letraEscolhida = jogada.verificarAcento(Dica.escolherLetra(jogada.getLetrasRestantes()));
        } while (letraEscolhida < 'A' || 'Z' < letraEscolhida);

        // Aumenta o erro como penalidade e diminui a quantidade de dicas
        partida.aumentarErros();
        forca.atualizar(partida.getErros());
        partida.diminuirDicas();
        txtDica.setText("Dicas: " +  partida.getQtdDicas());

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
        boolean semDicasRestantes = partida.getQtdDicas() <= 0;
        boolean poucasLetrasRestantes = jogada.getLetrasRestantes().size() <= 1;
        boolean limiteDeErrosAtingido = partida.getErros() == 5;
        if (semDicasRestantes || poucasLetrasRestantes || limiteDeErrosAtingido) {
            btnDica.setDisable(true);
        }
    }

    // Recebe os nomes dos jogadores do MenuController
    public void setJogadores(String j1, String j2, String categoria, Dificuldade dificuldade) {
        partida = new Partida(categoria, dificuldade);

        if (!(dificuldade == null)) {
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
        txtFimDaRodada.setVisible(false);

        txtLetrasUsadas.setText("");
        txtDica.setText("Dicas: " +  partida.getQtdDicas());
        txtPontosJ1.setText(jogadores.getNomeJ1() + ": " + jogadores.getPontosJ1());
        txtPontosJ2.setText(jogadores.getNomeJ2() + ": " + jogadores.getPontosJ2());
        txtRodada.setText("Vez de " + partida.getVezJogador());

        txtCategoria.setText(jogada.getPalavra().getCategoria());
        txtPalavra.setText(jogada.getPalavraOculta());
        forca.atualizar(partida.getErros());
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
                boolean acertou = jogada.tentarLetra(btn.getText().charAt(0));

                // Atualiza o texta que mostra as letras que já foram jogadas
                if (jogada.getLetrasUsadas().size() == 1) {
                    txtLetrasUsadas.setText(btn.getText());
                } else {
                    txtLetrasUsadas.setText(txtLetrasUsadas.getText() + "-" + btn.getText());
                }

                if (acertou) {
                    if (!isDica) jogadores.aumentarAcertos(partida.getVezJogador());
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
                        txtFimDaRodada.setText(partida.getVezJogador() + " acertou!");
                        txtFimDaRodada.setVisible(true);
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
                    forca.atualizar(partida.getErros());

                    if (partida.derrota()) {
                        txtFimDaRodada.setText("Ninguém venceu!");
                        txtFimDaRodada.setVisible(true);
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

                    // Desativa a dica se faltar apenas um erro para a derrota
                    if (partida.getErros() == 5) btnDica.setDisable(true);

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
