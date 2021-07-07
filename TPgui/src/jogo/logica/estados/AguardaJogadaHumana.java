package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class AguardaJogadaHumana extends JogoEstadoAdapter{
    public AguardaJogadaHumana(Jogo dados){
        super(dados);
        getDados().vaiJogarHumano();
    }

    @Override
    public JogoEstado joga(int coluna){
        Jogo dados = getDados();
        dados.joga(coluna);
        if(dados.existeVencedor() || dados.empate())
            return new FimJogo(getDados());
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

    //para gui
    @Override
    public JogoEstado aceitaPecaEspecial(){
        return new AguardaJogadaEspecial(getDados());
    }


    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.AGUARDA_JOGADA_HUMANA; }
}
