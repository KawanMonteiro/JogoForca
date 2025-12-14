package com.jogoforca.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ComputadorTest {

    @Test
    void acertoQuandoFaltarUmaLetra() {
        Computador pc = new Computador("NORMAL");
        char letra = pc.escolherLetra(List.of('U', 'V'), List.of('A'));

        assertEquals('A', letra);
    }

    @Test
    void naoRepetirLetra() {
        Computador pc = new Computador("NORMAL");
        List<Character> usadas = List.of('A', 'B', 'C');
        char letra = pc.escolherLetra(usadas, List.of('E', 'T'));
        assertFalse(usadas.contains(letra));
    }
}
