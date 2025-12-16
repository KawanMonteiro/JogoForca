package com.jogoforca.model;

public class MotorDeJogo {

    private Jogada jogada;
    private Jogadores jogadores;
    private Computador comp;
    private Partida partida;

    public MotorDeJogo(String j1, String j2, String categoria, Dificuldade dificuldade) {
        partida = new Partida(categoria, dificuldade);
        setJogadores(j1, j2);
        iniciarPartida();
    }

    public Jogada getJogada() {
        return jogada;
    }

    public Jogadores getJogadores() {
        return jogadores;
    }

    private void setJogadores(String j1, String j2) {
        if (partida.getDificuldade() != null) {
            jogadores = new Jogadores(j1, "COMP");
            this.comp = new Computador(partida.getDificuldade());
        } else {
            jogadores = new Jogadores(j1, j2);
        }
    }

    public Computador getComp() {
        return comp;
    }

    public Partida getPartida() {
        return partida;
    }

    public void iniciarPartida() {
        partida.carregarPalavras();

        try {
            jogada = new Jogada(partida.sortearPalavra());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (partida.getVezJogador().equals(jogadores.getNomeJ2())
                || partida.getVezJogador().isEmpty()) {
            partida.setVezJogador(jogadores.getNomeJ1());
        } else {
            partida.setVezJogador(jogadores.getNomeJ2());
        }
    }

    // Chamado toda a vez que um botão é apertado
    public boolean tentativa(char letra, boolean isDica) {
        boolean acertou = jogada.tentarLetra(letra);

        if (acertou) {
            if (!isDica) jogadores.aumentarAcertos(partida.getVezJogador());
            jogada.removerLetra(letra);

            // Atualiza a pontuação em caso de vitória
            if (partida.vitoria(jogada.getPalavraOculta())) {
                jogadores.aumentarPontos(partida.getVezJogador());
            }
        } else {
            jogadores.aumentarErros(partida.getVezJogador());
            partida.aumentarErros();

            // Muda a vez dos jogadores
            if (partida.getVezJogador().equals(jogadores.getNomeJ1())) {
                partida.setVezJogador(jogadores.getNomeJ2());
            } else {
                partida.setVezJogador(jogadores.getNomeJ1());
            }
        }

        return acertou;
    }

    public char pegarDica() {
        partida.aumentarErros();
        partida.diminuirDicas();

        char letraEscolhida;
        do {
            letraEscolhida = jogada.verificarAcento(Dica.escolherLetra(jogada.getLetrasRestantes()));
        } while (letraEscolhida < 'A' || 'Z' < letraEscolhida);
        return letraEscolhida;
    }
}
