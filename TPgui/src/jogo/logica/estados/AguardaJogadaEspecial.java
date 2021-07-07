package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class AguardaJogadaEspecial extends JogoEstadoAdapter{
    public AguardaJogadaEspecial(Jogo dados){
        super(dados);
        getDados().vaiJogarPecaEspecial();
    }

    @Override
    public JogoEstado jogaPecaEspecial(int coluna){
        getDados().jogaPecaEspecial(coluna);
        if(getDados().jogadorAtualIsHumano())
            if (getDados().jogadorAtualMiniJogo())
                return new AguardaDecisaoMJ(getDados());
            else if (getDados().jogadorAtualTemPecaEspecial())
                return new AguardaDecisaoPE(getDados());
            else
                return new AguardaJogadaHumana(getDados());
        else
            return new VerJogadaMaquina(getDados());
    }

    @Override
    public JogoEstado joga(int coluna){
        return jogaPecaEspecial(coluna);
    }

    @Override
    public JogoEstado fezUndo(){
        return new AguardaJogadaHumana(getDados());
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.AGUARDA_JOGADA_ESPECIAL; }
}
