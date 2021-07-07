package jogo.logica;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class JogoObs {
    public static final String REGISTO = "registo";
    public static final String TABULEIRO = "tabuleiro";
    public static final String FIM = "fim";
    public static final String FINAL = "final";
    public static final String ESPECIAL = "especial";
    public static final String UNDO = "undo";
    public static final String PALAVRASMJ = "mini-jogo-palavras";
    public static final String CARREGA = "carrega";
    public static final String PREPARA = "prepara";
    private boolean interrompido = false;

    GestaoJogo jogo;
    PropertyChangeSupport pcs;

    public JogoObs(GestaoJogo jogo) {
        this.jogo = jogo;
        pcs = new PropertyChangeSupport(jogo);
    }

    public void addObserver(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public boolean miniJogoAtivo(){
        return jogo.miniJogoAtivo();
    }

    public void aceitaMiniJogo() {
        jogo.aceitaMiniJogo();
    }

    public void miniJogoVerificaResposta(float resposta, String respostaPalavras){
        jogo.miniJogoVerificaResposta(resposta,respostaPalavras);
    }

    public void fimMiniJogo() {
        jogo.fimMiniJogo();
        pcs.firePropertyChange(TABULEIRO, null, null);
    }

    public void aceitaPecaEspecial() {
        jogo.aceitaPecaEspecial();
        pcs.firePropertyChange(ESPECIAL, null, null);
    }

    public String getJogadorAtual() {
        return jogo.getJogadorAtual();
    }

    public void registaJogador(int nr, String n, char t) {
        jogo.registaJogador(nr, n, t);
        pcs.firePropertyChange(REGISTO, null, null);
    }
    public void novoJogo(){
        jogo.novoJogo();
        pcs.firePropertyChange(TABULEIRO, null, null);
        pcs.firePropertyChange(PREPARA, null, null);

    }
    public void comecaJogo(){
        jogo.comecaJogo();
        pcs.firePropertyChange(TABULEIRO, null, null);
    }
    public void verJogadaMaquina() {
        if (interrompido) return;
        jogo.verJogadaMaquina();
        pcs.firePropertyChange(TABULEIRO, null, null);
    }
    public void undo() {
        if (jogo.undo(1)){
            pcs.firePropertyChange(TABULEIRO, null, null);
            pcs.firePropertyChange(UNDO, null, null);
        }
    }
    public void joga(int coluna) {
        if (interrompido) return;
        jogo.joga(coluna);
        pcs.firePropertyChange(TABULEIRO, null, null);
    }

    public String getLogGUI() {
        return jogo.getLogGUI();
    }

    //add para gui
    public String getNomeJogador(int nr) {
        return jogo.getNomeJogador(nr);
    }

    public boolean isHumano(int nr) {
        return jogo.isHumano(nr);
    }

    public int getCreditos(int nr) {
        return jogo.getCreditos(nr);
    }

    public int getJogadasAteMiniJogo(int nr) {
        return jogo.getJogadasAteMiniJogo(nr);
    }

    public int[][] getTabuleiro() {
        return jogo.getTabuleiro();
    }

    public JogoSituacao getSituacaoAtual() {
        return jogo.getSituacaoAtual();
    }

    public int[][] getCoordenadasFinais() {
        return jogo.getCoordenadasFinais();
    }

    public void mostraFinal() {
        pcs.firePropertyChange(FIM, null, null);
    }

    public void configuraFinal() {
            pcs.firePropertyChange(FINAL, null, null);
    }

    public void interromperListeners(){
        interrompido = true;
    }

    public void conectarListeners(){
        interrompido = false;
    }

    public void ignoraEstadoPedidoEspecial() {
        jogo.recusaPecaEspecial();
        pcs.firePropertyChange(ESPECIAL, null, null);
    }

    public boolean meuTurno(int nr) {
        return jogo.meuTurno(nr);
    }

    public boolean tenhoPecaEspecial() {
        return jogo.tenhoPecaEspecial();
    }

    public String getPergunta(){
        return jogo.getPergunta();
    }

    public String getDescricaoMiniJogo() {
        return jogo.getMiniJogoAtualDescricao();
    }

    public void recusaMiniJogo() {
        jogo.recusaMiniJogo();
        pcs.firePropertyChange(TABULEIRO, null, null);
    }

    public int getPecasEspeciais(int nr) {
        return jogo.getPecasEspeciais(nr);
    }

    public void carregaGUI(File fich) {
        if (jogo.getSituacaoAtual() == JogoSituacao.AGUARDA_CONFIGURACAO) {
            jogo.carregaGUI(fich);
            pcs.firePropertyChange(CARREGA, null, null);
            char tipo;
            if (isHumano(1))
                tipo = 'H';
            else
                tipo = 'M';
            registaJogador(1,getNomeJogador(1),tipo);
            if (isHumano(2))
                tipo = 'H';
            else
                tipo = 'M';
            registaJogador(2,getNomeJogador(2),tipo);
        }
        else
            jogo.carregaGUI(fich);
        pcs.firePropertyChange(TABULEIRO, null, null);
    }

    public void gravaGUI(File fich){
       jogo.gravaGUI(fich);
    }

    public void gravaReplay() {
        jogo.gravaReplay();
    }

    public void carregaREPLAY(File hFile) {
        if (jogo.getSituacaoAtual() == JogoSituacao.AGUARDA_CONFIGURACAO) {
            jogo.carregaGUIREPLAY(hFile);
            pcs.firePropertyChange(CARREGA, null, null);
            char tipo;
            if (isHumano(1))
                tipo = 'H';
            else
                tipo = 'M';
            registaJogador(1,getNomeJogador(1),tipo);
            if (isHumano(2))
                tipo = 'H';
            else
                tipo = 'M';
            registaJogador(2,getNomeJogador(2),tipo);
        }
        else
            jogo.carregaGUIREPLAY(hFile);
        pcs.firePropertyChange(TABULEIRO, null, null);
    }

    public void next() {
        jogo.next();
        pcs.firePropertyChange(TABULEIRO, null, null);
    }
}