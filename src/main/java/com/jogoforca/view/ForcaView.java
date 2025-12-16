package com.jogoforca.view;

import com.jogoforca.JogoController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.logging.Logger;

public class ForcaView {
    private static final Logger logger = Logger.getLogger(ForcaView.class.getName());

    private final ImageView imgBase, imgForca;

    public ForcaView(ImageView base, ImageView imgForca) {
        this.imgBase = base;
        this.imgForca = imgForca;
    }

    public void atualizar(int erros) {
        try {
            // Busca o nome da imagem correspondente ao número de erros
            String nomeImagem = "/img/forca" + erros + ".png";
            // Irá procurar pela imagem no resource da classe atual
            InputStream imgStream = JogoController.class.getResourceAsStream(nomeImagem);

            if (imgStream != null) {
                Image imagem = new Image(imgStream);

                if (erros == 0) {
                    imgBase.setImage(null);
                    imgForca.setImage(imagem);
                    imgForca.setClip(null);
                } else {
                    imgBase.setImage(imgForca.getImage());
                    imgForca.setImage(imagem);

                    animacaoDesenho();
                }

            } else {
                System.out.println("Imagem não encontrada: " + nomeImagem);
            }
        } catch (Exception e) {
            logger.severe("Um erro ocorreu:");
            logger.severe(e.toString());
        }
    }

    public void animacaoDesenho() {
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
}
