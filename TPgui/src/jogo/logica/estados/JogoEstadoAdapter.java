package jogo.logica.estados;

import jogo.logica.dados.Jogo;

import java.io.Serial;
import java.io.Serializable;

public abstract class JogoEstadoAdapter implements JogoEstado, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Jogo dados;
    public JogoEstadoAdapter (Jogo dados){
        this.dados = dados;
    }

    @Override
    public JogoEstado configuraJogador(int nr, String n, char t){
        return this;
    }

    @Override
    public JogoEstado joga(int coluna) {
        return this;
    }

    @Override
    public JogoEstado verJogadaMaquina(){
        return this;
    }

    @Override
    public JogoEstado novoJogo(){
        return this;
    }

    @Override
    public JogoEstado comecaJogo(){
        return this;
    }

    @Override
    public JogoEstado recusaMiniJogo(){
        return this;
    }

    @Override
    public JogoEstado aceitaMiniJogo(){
        return this;
    }


    @Override
    public JogoEstado verificaResposta(float resposta, String respostaPalavras){
        return this;
    }

    @Override
    public JogoEstado fimMiniJogo(){
        return this;
    }

    @Override
    public JogoEstado recusaPecaEspecial(){
        return this;
    }

    @Override
    public JogoEstado aceitaPecaEspecial(){
        return this;
    }

    @Override
    public JogoEstado jogaPecaEspecial(int coluna){
        return this;
    }

    @Override
    public JogoEstado fezUndo(){
        return this;
    }


    public Jogo getDados() {
        return dados;
    }
}
