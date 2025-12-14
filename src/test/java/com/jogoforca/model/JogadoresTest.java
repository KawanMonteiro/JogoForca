package com.jogoforca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JogadoresTest {

    @Test
    void nomeNullTeste() {
        assertThrows(NullPointerException.class, () -> {
            new Jogadores(null, "B");
        });
        assertThrows(NullPointerException.class, () -> {
            new Jogadores("A", null);
        });
    }

    @Test
    void acertosTeste() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Jogadores("A", "B").setAcertosJ1(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Jogadores("A", "B").setAcertosJ2(-1);
        });
    }

    @Test
    void errosTeste() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Jogadores("A", "B").setErrosJ1(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Jogadores("A", "B").setErrosJ2(-1);
        });
    }
}
