package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class AguardaDecisaoMJ extends JogoEstadoAdapter{
    public AguardaDecisaoMJ(Jogo dados){
        super(dados);
        getDados().aguardaDecisao();
    }

    @Override
    public JogoEstado recusaMiniJogo(){
        getDados().trocaMiniJogo();
        if (getDados().jogadorAtualTemPecaEspecial())
            return new AguardaDecisaoPE(getDados());
        return new AguardaJogadaHumana(getDados());
    }

    @Override
    public JogoEstado aceitaMiniJogo(){
        Jogo dados = getDados();
        if (dados.miniJogoAtualContas())
            return new MiniJogoContas(dados);
        else
            return new MiniJogoPalavras(dados);
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.AGUARDA_DECISAO_MJ; }
}
