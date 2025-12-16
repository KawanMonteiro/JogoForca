package com.jogoforca.model;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Dica {

    private static final Random random = new Random();
    private static final Image btnIcon = new Image(
            Objects.requireNonNull(Dica.class.getResourceAsStream("/img/imgBtnDica.png"))
    );


    public static Image getImagem() {
        return btnIcon;
    }

    public static char escolherLetra(List<Character> letras) {
        return letras.get(random.nextInt(letras.size()));
    }
}
