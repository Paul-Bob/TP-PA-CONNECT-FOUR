package jogo.logica;

import jogo.logica.dados.Jogador;
import jogo.logica.dados.Jogo;
import jogo.logica.estados.AguardaConfiguracao;
import jogo.logica.estados.FimJogo;
import jogo.logica.estados.JogoEstado;
import jogo.logica.estados.Replay;
import jogo.logica.memento.IMementoOriginator;
import jogo.logica.memento.Memento;

import java.io.*;
import java.util.ArrayList;

public class ContextoMaqEstados implements Serializable, IMementoOriginator {
    @Serial
    private static final long serialVersionUID = 1L;
    private JogoEstado estado;
    private Jogo dados;

    public ContextoMaqEstados(){
        dados = new Jogo();
        estado = new AguardaConfiguracao(dados);
    }

    //Estados
    private void setEstado(JogoEstado estado) {
        this.estado = estado;
    }
    public JogoSituacao getSituacaoAtual() {
        return estado.getSituacaoAtual();
    }

    public void registaJogador(int nr, String n, char t){
        setEstado(estado.configuraJogador(nr,n,t));
    }
    public void joga(int coluna) {
        setEstado(estado.joga(coluna));
    }
    public void verJogadaMaquina(){
        setEstado(estado.verJogadaMaquina());
    }
    public void novoJogo(){
        this.estado = new FimJogo(dados);
        setEstado(estado.novoJogo());
    }
    public void comecaJogo(){
        setEstado(estado.comecaJogo());
    }
    public void aceitaMiniJogo() {
        setEstado(estado.aceitaMiniJogo());
    }
    public void recusaMiniJogo() {
        setEstado(estado.recusaMiniJogo());
    }
    public void jogaPecaEspecial(int coluna) {
        setEstado(estado.jogaPecaEspecial(coluna));
    }
    public void aceitaPecaEspecial() {
        setEstado(estado.aceitaPecaEspecial());
    }
    public void recusaPecaEspecial() {
        setEstado(estado.recusaPecaEspecial());
    }
    public void miniJogoVerificaResposta(float resposta, String respostaPalavras){
        setEstado(estado.verificaResposta(resposta,respostaPalavras));
    }
    public void fimMiniJogo() {
        setEstado(estado.fimMiniJogo());
    }
    public void carregaGravacao() {
        dados.getLog();
        dados.vaiJogarHumano();
    }


    //dados
    public String getJogadorAtual(){
        return dados.getJogadorAtual();
    }
    public String getLog(){
        return dados.getLog();
    }
    //->MiniJogos
    public boolean miniJogoAtivo(){
        return dados.miniJogoAtivo();
    }
    public String getMiniJogoAtualDescricao() {
        return dados.getMiniJogoAtualDescricao();
    }

    //UNDO
    @Override
    public Memento getMemento() throws IOException {
        return new Memento(this);
    }
    @Override
    public void setMemento(Memento m, int vezes) throws IOException, ClassNotFoundException {
        Jogador atual = dados.manterAtual();
        Jogador prox  = dados.manterProximo();
        ArrayList<String> replay = getReplay();
        ContextoMaqEstados maquina = (ContextoMaqEstados) m.getSnapshot();
        this.estado = maquina.estado;
        this.dados = maquina.dados;
        //BCAP
        setEstado(estado.fezUndo());
        getLog();
        dados.setAtual(vezes, atual, prox, replay);
    }
    public void setMementoReplay(Memento m) throws IOException, ClassNotFoundException {
        ContextoMaqEstados maquina = (ContextoMaqEstados) m.getSnapshot();
        this.estado = maquina.estado;
        this.dados = maquina.dados;
    }
    public boolean preparaFazerUndo(int vezes){
        return dados.preparaFazerUndo(vezes);
    }

    //Gravacao
    public String getNomeGravacao(String modo){
        return dados.getNomeGravacao(modo);
    }
    public ArrayList<String> getReplay(){
        return dados.getReplay();
    }


    //adicionadas para gui
    public String getNomeJogador(int nr) {
        return dados.getNomeJogador(nr);
    }
    public boolean isHumano(int nr){
        return dados.isHumano(nr);
    }

    public int getCreditos(int nr) {
        return dados.getCreditos(nr);
    }

    public int getJogadasAteMiniJogo(int nr) {
        return dados.getJogadasAteMiniJogo(nr);
    }

    public int[][] getTabuleiro() {
        return dados.getJogo();
    }

    public String getLogGUI() {
        return dados.getLogGUI();
    }

    public int[][] getCoordenadasFinais() {
        return dados.getCoordenadasFinais();
    }

    public boolean meuTurno(int nr) {
        return dados.meuTurno(nr);
    }

    public boolean tenhoPecaEspecial() {
        return dados.tenhoPecaEspecial();
    }

    public String getPergunta() {
        return dados.getPergunta();
    }

    public int getPecasEspeciais(int nr) {
        return dados.getPecasEspeciais(nr);
    }

    public void replayMan() {
        this.estado = new Replay(dados);
    }
    public void fimReplay() {
        this.estado = new FimJogo(dados);
    }

}
