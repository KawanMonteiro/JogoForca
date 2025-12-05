package com.jogoforca.model;


public class Palavra {
    private String palavra, categoria;

    public Palavra(String palavra,  String categoria) {
        setPalavra(palavra);
        setCategoria(categoria);
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        if (palavra == null) {
            throw new NullPointerException("Palavra não pode ser nula!");
        }
        this.palavra = palavra.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null) {
            throw new NullPointerException("Categoria não pode ser nula!");
        }
        this.categoria = categoria.trim();
    }

}
