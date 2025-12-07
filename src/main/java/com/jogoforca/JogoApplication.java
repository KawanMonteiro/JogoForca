package com.jogoforca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class JogoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JogoApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.setFill(javafx.scene.paint.Color.valueOf("#145924"));
        stage.setResizable(false);

        Image icone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/icone.png")));
        stage.getIcons().add(icone);
        stage.setTitle("Jogo da Forca");

        stage.setScene(scene);
        stage.show();
    }
}
