package jogo.logica.estados;

import jogo.logica.JogoSituacao;
import jogo.logica.dados.Jogo;

public class AguardaConfiguracao extends JogoEstadoAdapter{

    public AguardaConfiguracao(Jogo dados){
        super(dados);
    }

    @Override
    public JogoEstado configuraJogador(int nr, String n, char t){
        getDados().registo(nr,n,t);
        if(nr == 2)
            if(getDados().jogadorAtualIsHumano())
                return new AguardaJogadaHumana(getDados());
            else
                return new VerJogadaMaquina(getDados());
        return this;
    }

    @Override
    public JogoSituacao getSituacaoAtual(){ return JogoSituacao.AGUARDA_CONFIGURACAO; }
}
