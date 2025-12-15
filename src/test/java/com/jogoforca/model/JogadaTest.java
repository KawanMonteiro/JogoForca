package com.jogoforca.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JogadaTest {

    private Jogada jogada;

    @BeforeEach
    void setUp() {
        jogada = new Jogada(new Palavra("UVA", "FRUTA"));
    }

    @Test
    void palavraOcultaTeste() {
        assertEquals("_ _ _", jogada.getPalavraOculta());
    }

    @Test
    void acertoErroTeste() {
        assertTrue(jogada.tentarLetra('U'));
        assertEquals("U _ _", jogada.getPalavraOculta());
        assertFalse(jogada.tentarLetra('Z'));
        assertEquals("U _ _", jogada.getPalavraOculta());
    }

    @Test
    void repetirLetraTeste() {
        jogada.tentarLetra('B');
        assertThrows(IllegalArgumentException.class, () ->
                jogada.tentarLetra('B'));
    }

    @Test
    void letraDesconhecidaTeste() {
        assertThrows(IllegalArgumentException.class, () ->
                jogada.verificarAcento('_'));
    }
}
