package com.jogoforca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PalavraTest {

    @Test
    void criarPalavraTeste() {
        Palavra palavra = new Palavra(" BANANA ", " FRUTA ");

        assertEquals("BANANA", palavra.getPalavra());
        assertEquals("FRUTA", palavra.getCategoria());
    }

    @Test
    void palavraNullTeste() {
        assertThrows(NullPointerException.class, () ->
                new Palavra(null, null));
    }
}
