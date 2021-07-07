package jogo.logica.dados;

import java.io.Serial;
import java.io.Serializable;

public class Jogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    final private String nome;
    private String miniJogoAtual = "contas";
    final boolean humano;
    final int numero;
    private  int pecaEspecial = 0;
    private  int creditos = 5;
    private  int jogadas = 0;

    public Jogador(String n, char t, int nr) {
        nome = n;
        humano = t == 'H';
        numero = nr;
    }

    public String getNome() { return nome; }
    public boolean isHumano() { return humano; }
    public boolean temPecaEspecial() {
        return pecaEspecial > 0;
    }
    public boolean miniJogo(){
        if (!humano || jogadas != 4)
            return false;
        jogadas = 0;
        return true;

    }
    public void incrementaJogadas(){
        if (humano) {
            jogadas++;
        }
    }
    public int getNumero() { return numero; }
    public void incrementaPecaEspecial(){
        pecaEspecial++;
    }
    public void resetJogadas(){
        jogadas = 0;
    }

    public int getPecasEspeciais() {
        return pecaEspecial;
    }

    public void usaPecaEspecial() {
        if (pecaEspecial > 0)
            pecaEspecial--;
    }
    public void resetPecaEspecial() {
        pecaEspecial = 0;
    }
    public String getMiniJogoAtual(){
        return miniJogoAtual;
    }
    public void trocaMiniJogo(){
        if (miniJogoAtual.equals("contas"))
            miniJogoAtual = "palavras";
        else
            miniJogoAtual = "contas";
    }
    public boolean preparaUndo(int vezes){
        return creditos >= vezes;
    }
    public void fezUndo(int vezes, Jogador set) {
        jogadas = 0;
        creditos = set.creditos - vezes;
    }

    public int getCreditos() {
        return creditos;
    }

    public int getJogadasAteMiniJogo() {
        return (4-jogadas);
    }
}
