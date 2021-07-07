package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class Replay extends JogoEstadoAdapter{

    public Replay(Jogo dados) {
        super(dados);
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.REPLAY; }
}
