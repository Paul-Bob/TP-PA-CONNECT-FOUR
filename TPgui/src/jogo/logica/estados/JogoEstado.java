package jogo.logica.estados;

import jogo.logica.JogoSituacao;

public interface JogoEstado {
    JogoEstado configuraJogador(int nr, String n, char t);
    JogoEstado joga(int coluna);
    JogoEstado verJogadaMaquina();
    JogoEstado novoJogo();
    JogoEstado comecaJogo();
    JogoEstado recusaMiniJogo();
    JogoEstado aceitaMiniJogo();
    JogoEstado verificaResposta(float resposta, String respostaPalavras);
    JogoEstado fimMiniJogo();
    JogoEstado recusaPecaEspecial();
    JogoEstado aceitaPecaEspecial();
    JogoEstado jogaPecaEspecial(int coluna);
    JogoEstado fezUndo();
    JogoSituacao getSituacaoAtual();
}
