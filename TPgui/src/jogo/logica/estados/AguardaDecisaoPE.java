package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class AguardaDecisaoPE extends JogoEstadoAdapter{
    public AguardaDecisaoPE(Jogo dados){
        super(dados);
        getDados().aguardaDecisao();
    }

    @Override
    public JogoEstado recusaPecaEspecial(){
        Jogo dados = getDados();
        if (dados.jogadorAtualIsHumano()){
            return new AguardaJogadaHumana(dados);
        }
        else
            return new VerJogadaMaquina(dados);
    }

    @Override
    public JogoEstado aceitaPecaEspecial(){
        return new AguardaJogadaEspecial(getDados());
    }


    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.AGUARDA_DECISAO_PE; }
}
