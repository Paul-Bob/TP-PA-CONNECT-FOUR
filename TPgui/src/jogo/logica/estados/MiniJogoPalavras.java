package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class MiniJogoPalavras extends JogoEstadoAdapter{
    public MiniJogoPalavras(Jogo dados){
        super(dados);
        getDados().miniJogoInicia();
    }

    @Override
    public JogoEstado verificaResposta(float resposta, String respostaPalavras){
        Jogo dados = getDados();
        dados.miniJogoPalavrasVerificaResposta(respostaPalavras);
        if (dados.jogadorAtualIsHumano()) {
            if (dados.jogadorAtualMiniJogo())
                return new AguardaDecisaoMJ(dados);
            else if (dados.jogadorAtualTemPecaEspecial())
                return new AguardaDecisaoPE(dados);
            else
                return new AguardaJogadaHumana(dados);
        }
        else
            return new VerJogadaMaquina(dados);
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.MINI_JOGO_PALAVRAS; }
}
