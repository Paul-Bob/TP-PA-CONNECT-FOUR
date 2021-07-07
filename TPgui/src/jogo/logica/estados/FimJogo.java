package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class FimJogo extends JogoEstadoAdapter{
    public FimJogo(Jogo dados){
        super(dados);
    }

    @Override
    public JogoEstado novoJogo(){
        getDados().preparaInicio();
        return new AguardaConfiguracao(getDados());
    }

    @Override
    public JogoEstado comecaJogo(){
        getDados().preparaReInicio();
        if (getDados().jogadorAtualIsHumano())
            return new AguardaJogadaHumana(getDados());
        else
            return new VerJogadaMaquina(getDados());
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.FIM_JOGO; }
}
