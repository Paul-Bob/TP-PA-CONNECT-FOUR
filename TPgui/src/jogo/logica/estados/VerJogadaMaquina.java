package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class VerJogadaMaquina extends JogoEstadoAdapter{

    public VerJogadaMaquina(Jogo dados){
        super(dados);
    }

    @Override
    public JogoEstado verJogadaMaquina(){
        Jogo dados = getDados();
        dados.jogaMaquina();
        if (dados.existeVencedor() || dados.empate())
            return new FimJogo(dados);
        if(dados.jogadorAtualIsHumano()) {
            if (dados.jogadorAtualMiniJogo())
                return new AguardaDecisaoMJ(dados);
            else if (dados.jogadorAtualTemPecaEspecial())
                return new AguardaDecisaoPE(dados);
            else
                return new AguardaJogadaHumana(dados);
        }
        return new VerJogadaMaquina(dados);
    }

    @Override
    public JogoEstado fezUndo(){
        return new AguardaJogadaHumana(getDados());
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.VER_JOGADA_MAQUINA; }
}
