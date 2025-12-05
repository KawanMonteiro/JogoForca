package com.jogoforca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JogoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JogoApplication.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setResizable(false);
        stage.setTitle("Jogo da Forca");
        stage.setScene(scene);
        stage.show();
    }
}
