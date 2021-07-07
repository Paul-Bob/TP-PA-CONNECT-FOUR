package jogo.logica;

import jogo.logica.memento.ICareTaker;
import jogo.logica.memento.Memento;

import java.io.*;
import java.util.*;

public class GestaoJogo implements ICareTaker, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ContextoMaqEstados originator;
    private boolean replay = false;
    private boolean lastUndo = false;

    public GestaoJogo(){
        originator = new ContextoMaqEstados();
    }

    //Camada Redirecionadora para a maquina de estados
    public JogoSituacao getSituacaoAtual() {
        return originator.getSituacaoAtual();
    }
    public void registaJogador(int nr, String n, char t){
        originator.registaJogador(nr,n,t);
    }
    public void joga(int coluna) {
        lastUndo = false;
        saveMemento();
        originator.joga(coluna);
    }
    public void verJogadaMaquina(){
        lastUndo = false;
        saveMemento();
        originator.verJogadaMaquina();
    }
    public void novoJogo(){
        lastUndo = false;
        originator.novoJogo();
    }
    public void comecaJogo(){
        lastUndo = false;
        originator.comecaJogo();
    }
    public void aceitaMiniJogo() {
        originator.aceitaMiniJogo();
    }
    public void recusaMiniJogo() {
        originator.recusaMiniJogo();
    }
    public void jogaPecaEspecial(int coluna) {
        lastUndo = false;
        saveMemento();
        originator.jogaPecaEspecial(coluna);
    }
    public void aceitaPecaEspecial() {
        originator.aceitaPecaEspecial();
    }
    public void recusaPecaEspecial() {
        originator.recusaPecaEspecial();
    }
    public void miniJogoVerificaResposta(float resposta, String respostaPalavras){
        originator.miniJogoVerificaResposta(resposta,respostaPalavras);
    }
    public void fimMiniJogo() {
        originator.fimMiniJogo();
    }

    //dados
    public String getJogadorAtual(){
        return originator.getJogadorAtual();
    }
    public String getLog(){
        return originator.getLog();
    }
    //->MiniJogos
    public boolean miniJogoAtivo(){
        return originator.miniJogoAtivo();
    }
    public String getPergunta(){
        return originator.getPergunta();
    }
    public String getMiniJogoAtualDescricao() {
        return originator.getMiniJogoAtualDescricao();
    }


    //MEMENTO
    private Deque<Memento> stackHist = new ArrayDeque<>();
    private Deque<Memento> stackHistReplay = new ArrayDeque<>();
    Iterator<Memento> it;

    @Override
    public void saveMemento() {
        try{
            stackHist.push(originator.getMemento());
        } catch(IOException ex) {
            System.err.println("gravaMemento: " + ex);
            stackHist.clear();
        }
        saveMementoREPLAY();
    }

    public void saveMementoREPLAY() {
        try{
            stackHistReplay.push(originator.getMemento());
        } catch(IOException ex) {
            System.err.println("gravaMemento: " + ex);
            stackHistReplay.clear();
        }
    }

    @Override
    public boolean undo(int vezes) {
        if (stackHist.isEmpty() || !originator.preparaFazerUndo(vezes))
            return false;
        if (!lastUndo)
            saveMementoREPLAY();
        try {
            Memento anterior = stackHist.pop();
            for (int i = 1; i < vezes; i++) {
                if (stackHist.isEmpty())
                    return false;
                anterior = stackHist.pop();
            }
            Deque<Memento> stackHistReplayAUX = stackHistReplay;
            originator.setMemento(anterior,vezes);
            stackHistReplay = stackHistReplayAUX;
            saveMementoREPLAY();
            lastUndo = true;
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println("undo: " + ex);
            stackHist.clear();
        }
        return true;
    }


    //Gravações
    public void grava(){
       GestaoGravacoes.escreveParaFicheiro(originator.getNomeGravacao("gravacao_"),this);
    }
    public void gravaGUI(File fich){
        GestaoGravacoes.escreveParaFicheiroDAGUI(fich,this);
    }
    public void carrega(String nomeGravacao) {
        GestaoJogo gravacao = (GestaoJogo) GestaoGravacoes.lerDoFicheiro(nomeGravacao);
        if (gravacao != null) {
            this.originator = gravacao.originator;
            this.stackHist = gravacao.stackHist;
            originator.carregaGravacao();
        }
        else
            originator.novoJogo();
    }

    public void carregaGUI(File fich) {
        GestaoJogo gravacao = (GestaoJogo) GestaoGravacoes.lerDoFicheiroDAGUI(fich);
        if (gravacao != null) {
            this.originator = gravacao.originator;
            this.stackHist = gravacao.stackHist;
            this.stackHistReplay = gravacao.stackHistReplay;
            originator.carregaGravacao();
        }
        else
            originator.novoJogo();
    }
    public String listarGravacoes(String modo){
        File f = new File(".");

        String[] gravacoes;
        gravacoes = Arrays.stream(f.list()).filter(nome -> nome.startsWith(modo)).toArray(String[]::new);
        ArrayList<String> gravacoesNomes = new ArrayList<>();

        Collections.addAll(gravacoesNomes, gravacoes);

        StringBuilder lista = new StringBuilder();
        for (String nome: gravacoesNomes)
            lista.append(nome).append("\n");
        return lista.toString();
    }
    public void gravaReplay(){
        if (new File("./replays").list().length >= 5)
            purgeReplayFiles(new File("./replays"));
        if (replay)
            return;
        saveMemento();
        GestaoGravacoes.escreveParaFicheiro(originator.getNomeGravacao("./replays/replay_"),this);
    }

    private void purgeReplayFiles(File replayDir){
        File[] logFiles = replayDir.listFiles();
        long oldestDate = Long.MAX_VALUE;
        File oldestFile = null;
        if( logFiles != null && logFiles.length > 4){
            //delete oldest files after theres more than 500 log files
            for(File f: logFiles){
                if(f.lastModified() < oldestDate){
                    oldestDate = f.lastModified();
                    oldestFile = f;
                }
            }

            if(oldestFile != null){
                oldestFile.delete();
            }
        }
    }
    public ArrayList<String> getReplay(){
        return originator.getReplay();
    }


    //adicionadas para gui
    public String getNomeJogador(int nr) {
        return originator.getNomeJogador(nr);
    }
    public boolean isHumano(int nr){
        return originator.isHumano(nr);
    }

    public int getCreditos(int nr) {
        return originator.getCreditos(nr);
    }

    public int getJogadasAteMiniJogo(int nr) {
        return originator.getJogadasAteMiniJogo(nr);
    }

    public int[][] getTabuleiro() {
        return originator.getTabuleiro();
    }

    public String getLogGUI() {
        return originator.getLogGUI();
    }

    public int[][] getCoordenadasFinais() {
        return originator.getCoordenadasFinais();
    }

    public boolean meuTurno(int nr) {
        return originator.meuTurno(nr);
    }

    public boolean tenhoPecaEspecial() {
        return originator.tenhoPecaEspecial();
    }

    public int getPecasEspeciais(int nr) {
        return originator.getPecasEspeciais(nr);
    }

    public void carregaGUIREPLAY(File hFile) {
        carregaGUI(hFile);
        it = stackHistReplay.descendingIterator();
        try {
            Memento inicio = it.next();
            originator.setMementoReplay(inicio);
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println("undo: " + ex);
            stackHistReplay.clear();
        }
        originator.replayMan();
        replay = true;
    }

    public void next() {
        if(!it.hasNext()){
            originator.fimReplay();
            return;
        }
        try {
            Memento next = it.next();
            originator.setMementoReplay(next);
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println("undo: " + ex);
            stackHistReplay.clear();
        }
        originator.replayMan();
    }
}
