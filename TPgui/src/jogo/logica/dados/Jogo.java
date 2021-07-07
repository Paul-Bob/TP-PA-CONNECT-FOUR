package jogo.logica.dados;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Jogo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //utils
    StringBuilder log = new StringBuilder();
    StringBuilder logGUI = new StringBuilder();
    StringBuilder auxReplay = new StringBuilder();
    ArrayList<String> replay = new ArrayList<>();
    Random random = new Random();

    //jogadores
    private Jogador jogadorAtual, proximoJogador, vencedor;

    //tabuleiro
    private final int[][] jogo = new int[6][7];
    private final int[][] coordenadasFinais = new int[4][2];


    //miniJogos
    MiniJogoContas miniJogoContas = new MiniJogoContas();
    MiniJogoPalavras miniJogoPalavras = new MiniJogoPalavras();

    //----------------------------------------------Métodos----------------------------------------------------

    //------------------------------------------pedidos dos estados--------------------------------------------
    public void vaiJogarPecaEspecial(){
        log.append("\nTurno de JOGADA ESPECIAL do jogador ").append(getJogadorAtual()).append("\n\n");
        log.append(getTabuleiro());
    }
    public void vaiJogarHumano(){
        if (jogadorAtual != null) //BCAPbug corrigido a pedreiro por causa do memento implementado em cima da hora...(apenas para retorno ficar bonito na interface)
            log.append("\nTurno do jogador ").append(getJogadorAtual()).append("\n\n");
        log.append(getTabuleiro());
    }
    public void aguardaDecisao(){
        log.append(getTabuleiro());
    }
    public void registo(int nr, String nome, char tipo){
        switch (nr) {
            case 1 -> {
                jogadorAtual = new Jogador(nome, tipo, 1);
                log.append("Jogador '").append(nome).append("' (");
                if(tipo == 'H')
                    log.append("Humano)");
                else
                    log.append("Máquina)");
                log.append(" foi registado com sucesso!");
                logGUI.append(getLog());
            }
            case 2 -> {
                if(nome.equals(jogadorAtual.getNome())) {
                    log.append("\nNome '").append(nome).append("' já existe.\n");
                    nome = nome + "_2";
                    log.append("Foi automaticamente alterado para '").append(nome).append("'\n");
                }
                proximoJogador = new Jogador(nome, tipo, 2);
                log.append("Jogador '").append(nome).append("' (");
                if(tipo == 'H')
                    log.append("Humano)");
                else
                    log.append("Máquina)");
                log.append(" foi registado com sucesso!\n");
                sortearPrimeiroJogador();
                logGUI.append(getLog());
            }
        }
    }
    public void joga(int coluna){
        if(colunaCheia(coluna)) {
            log.append("Coluna ").append(coluna+1).append(" já está cheia.\nJogada inválida.\n");
            logGUI.append("Coluna ").append(coluna+1).append(" já está cheia.\nJogada inválida.\n");
            return;
        }
        jogadorAtual.incrementaJogadas();
        adicionaNaColuna(coluna);
        auxReplay.append(getJogadorAtual()).append(" jogou na coluna ").append(coluna+1);
        logGUI.append(getJogadorAtual()).append(" jogou na coluna ").append(coluna+1);
        auxReplay.append("\n").append(getTabuleiro());
        addToReplay(auxReplay);
        verificaJogo();
    }
    public void jogaMaquina(){
        int min = 0;
        int max = 6;
        int coluna;
        do{
            coluna = random.nextInt((max-min)+1) + min;
        } while(colunaCheia(coluna));
        log.append("\n\nJogador ").append(getJogadorAtual()).append(" (máquina) jogou na coluna ").append(coluna+1).append("\n\n");
        joga(coluna);
       log.append(getTabuleiro());
       log.append("\n|>--------------------->N*E*X*T<---------------------<|\n");
    }
    public void preparaInicio(){
        log.setLength(0);
        vencedor = null;
        limpaTabuleiro();
        jogadorAtual.resetJogadas();
        proximoJogador.resetJogadas();
        jogadorAtual.resetPecaEspecial();
        proximoJogador.resetPecaEspecial();
    }
    public void preparaReInicio(){
        preparaInicio();
        sortearPrimeiroJogador();
    }
    //->MiniJogos
    public void trocaMiniJogo(){
        jogadorAtual.trocaMiniJogo();
    }
    public void miniJogoInicia(){
        log.append("\nBoa Sorte!!!!!\n");
        if (jogadorAtual.getMiniJogoAtual().equals("contas")) {
            auxReplay.append("Jogador ").append(getJogadorAtual()).append(" aceitou mini jogo das contas!");
            addToReplay(auxReplay);
            log.append("Operação: ");
            log.append(miniJogoContas.inicia());
            log.append(" = ");
        }
        else {
            log.append("Sequência -> ").append(miniJogoPalavras.getPalavras());
            auxReplay.append("Jogador ").append(getJogadorAtual()).append(" aceitou mini jogo das palavras!");
            addToReplay(auxReplay);
        }
    }
    public void miniJogoContasVerificaResposta(float resposta){
        String resultado = miniJogoContas.verificaResposta(resposta);
        log.append(resultado);
        logGUI.append(resultado);
        if(miniJogoContas.ativo()) {
            log.append("\nOperação: ").append(miniJogoContas.operacao());
            log.append(" = ");
        }
    }
    public void miniJogoPalavrasVerificaResposta(String resposta){
            log.append(miniJogoPalavras.verificaResposta(resposta));
            logGUI.append(miniJogoPalavras.verificaResposta(resposta));
            jogadorAtual.trocaMiniJogo();
            if (miniJogoPalavras.getPontuacao() != 0){
                log.append("Ganhou uma peça especial!\n");
                logGUI.append("Ganhou uma peça especial!\n");
                jogadorAtual.incrementaPecaEspecial();
                miniJogoPalavras.resetPontuacao();
                replay.add(log.toString());
                return;
            }
            log.append("Perdeu a vez de jogar...\n");
            logGUI.append("Perdeu a vez de jogar...\n");
            log.append("\n|>--------------------->N*E*X*T<---------------------<|\n\n");
            mudaJogador();
            miniJogoPalavras.resetPontuacao();
            replay.add(log.toString());
    }
    public void verificaFimMiniJogo() {
        jogadorAtual.trocaMiniJogo();
        log.append("\nO tempo acabou\nPontuação: ").append(miniJogoContas.getPontuacao());
        getLogGUI();
        logGUI.append("O tempo acabou\nPontuação: ").append(miniJogoContas.getPontuacao());
        if (miniJogoContas.getPontuacao() >= 3) { //ALTERAR PARA 5 NA ENTREGA!!!!!!!!!!!!!!! <------------------------------
            log.append("\nGanhou uma peça especial!\n\n");
            logGUI.append("\nGanhou uma peça especial!\n\n");
            jogadorAtual.incrementaPecaEspecial();
            miniJogoContas.resetPontuacao();
            replay.add(log.toString());
            return;
        }
        log.append("\nInfelizmente não obteve pontuação suficiente para ganhar a peça especial.");
        log.append("\nPerdeu a vez de jogar...\n\n");
        logGUI.append("\nInfelizmente não obteve pontuação suficiente para ganhar a peça especial.");
        logGUI.append("\nPerdeu a vez de jogar...\n\n");
        log.append("|>--------------------->N*E*X*T<---------------------<|\n\n");
        mudaJogador();
        miniJogoContas.resetPontuacao();
        replay.add(log.toString());

    }
    //->PecaEspecial
    public void jogaPecaEspecial(int coluna) {
        for (int linha = 0; linha < 6; linha++)
            jogo[linha][coluna] = 0;
        jogadorAtual.usaPecaEspecial();
        auxReplay.append("Jogador ").append(getJogadorAtual()).append(" jogou peça especial na coluna ").append(coluna+1).append("\n");
        logGUI.append("Jogador ").append(getJogadorAtual()).append(" jogou peça especial na coluna ").append(coluna+1).append("\n");
        auxReplay.append(getTabuleiro());
        addToReplay(auxReplay);
        mudaJogador();
    }


    //---------------------------------------funções internas--------------------------------------------------
    private void sortearPrimeiroJogador(){
        if(random.nextBoolean()) {
            Jogador aux = jogadorAtual;
            jogadorAtual = proximoJogador;
            proximoJogador = aux;
        }
        log.append("\nPor sorteio o primeiro a jogar é o jogador '").append(jogadorAtual.getNome()).append("'");
        log.append(" (").append(jogadorAtual.getNumero()).append(")");
        if(jogadorAtualIsHumano())
            log.append(" (humano) !");
        else
            log.append(" (máquina) !");
        log.append("\nBoa Sorte aos dois!\n\n");
    }
    private String getTabuleiro(){
        StringBuilder resultado = new StringBuilder();
        resultado.append("       C O L U N A S\n");
        resultado.append("       1 2 3 4 5 6 7\n");
        resultado.append("       -------------\n");

        for (int i = 0; i < 6; i++) {
            switch (i){
                case 0 -> resultado.append(" L 1 | ");
                case 1 -> resultado.append(" I 2 | ");
                case 2 -> resultado.append(" N 3 | ");
                case 3 -> resultado.append(" H 4 | ");
                case 4 -> resultado.append(" A 5 | ");
                case 5 -> resultado.append(" S 6 | ");
            }
            for (int j = 0; j < 7; j++)
                resultado.append(jogo[i][j]).append(" ");
            resultado.append('\n');
        }
        return resultado.toString();
    }
    private boolean colunaCheia(int coluna){
        return jogo[0][coluna] != 0;
    }
    private void adicionaNaColuna(int coluna){
        for (int linha = 5; linha >= 0; linha--)
            if (jogo[linha][coluna] == 0) {
                jogo[linha][coluna] = jogadorAtual.getNumero();
                break;
            }
    }

    public int[][] getCoordenadasFinais() {
        return coordenadasFinais;
    }

    private void verificaJogo(){
        int pecaVencedora;
        int quatro;

        //verifica linhas
        for (int linha = 0; linha < 6; linha ++){
            for (int coluna = 0; coluna < 4; coluna++){
                if (jogo[linha][coluna] != 0) {
                    pecaVencedora = jogo[linha][coluna];
                    quatro = 1;
                    for (int subColuna = coluna + 1; subColuna < coluna + 4; subColuna++)
                        if (jogo[linha][subColuna] == pecaVencedora) {
                            if (++quatro == 4){
                                setVencedor(jogadorAtual, "linha", linha+1,coluna+1,linha+1,subColuna+1);
                                coordenadasFinais[0][0] = linha;
                                coordenadasFinais[0][1] = coluna;
                                coordenadasFinais[1][0] = linha;
                                coordenadasFinais[1][1] = coluna+1;
                                coordenadasFinais[2][0] = linha;
                                coordenadasFinais[2][1] = coluna+2;
                                coordenadasFinais[3][0] = linha;
                                coordenadasFinais[3][1] = subColuna;
                                return;
                            }
                        } else
                            break;
                }
            }
        }

        //verifica colunas
        for (int coluna = 0; coluna < 7; coluna++)
            for (int linha = 0; linha < 3; linha ++)
                if (jogo[linha][coluna] != 0){
                    pecaVencedora = jogo[linha][coluna];
                    quatro = 1;
                    for (int subLinha = linha + 1; subLinha < linha + 4; subLinha++)
                        if (jogo[subLinha][coluna] == pecaVencedora) {
                            if (++quatro == 4){
                                setVencedor(jogadorAtual, "coluna" , linha+1,coluna+1,subLinha+1,coluna+1);
                                coordenadasFinais[0][0] = linha;
                                coordenadasFinais[0][1] = coluna;
                                coordenadasFinais[1][0] = linha+1;
                                coordenadasFinais[1][1] = coluna;
                                coordenadasFinais[2][0] = linha+2;
                                coordenadasFinais[2][1] = coluna;
                                coordenadasFinais[3][0] = subLinha;
                                coordenadasFinais[3][1] = coluna;
                                return;
                            }
                        } else
                            break;
                }

        //verifica diagonal (Canto superior esquerdo -> canto superior direito)
        for (int linha = 0; linha < 3; linha++)
            for (int coluna = 0; coluna < 4; coluna++)
                if (jogo[linha][coluna] != 0) {
                    pecaVencedora = jogo[linha][coluna];
                    quatro = 1;
                    int subLinha = linha + 1;
                    int subColuna = coluna +1;
                    for (int proxima = 0; proxima < 3; proxima++, subColuna++, subLinha++)
                        if (jogo[subLinha][subColuna] == pecaVencedora) {
                        if (++quatro == 4){
                            setVencedor(jogadorAtual,"diagonal \\",linha+1,coluna+1,subLinha+1,subColuna+1);
                            coordenadasFinais[0][0] = linha;
                            coordenadasFinais[0][1] = coluna;
                            coordenadasFinais[1][0] = linha+1;
                            coordenadasFinais[1][1] = coluna+1;
                            coordenadasFinais[2][0] = linha+2;
                            coordenadasFinais[2][1] = coluna+2;
                            coordenadasFinais[3][0] = subLinha;
                            coordenadasFinais[3][1] = subColuna;
                            return;
                        }
                    } else
                        break;
                }

        //verifica diagonal (Canto superior direito -> canto inferior esquerdo)
        for (int linha = 0; linha < 3; linha ++)
            for (int coluna = 6; coluna > 2; coluna--)
                if (jogo[linha][coluna] != 0) {
                    pecaVencedora = jogo[linha][coluna];
                    quatro = 1;
                    int subLinha = linha + 1;
                    int subColuna = coluna - 1;
                    for (int proxima = 0; proxima < 3; proxima++, subColuna--, subLinha++) {
                        if (jogo[subLinha][subColuna] == pecaVencedora) {
                            if (++quatro == 4) {
                                setVencedor(jogadorAtual, "diagonal /", linha + 1, coluna + 1, subLinha + 1, subColuna + 1);
                                coordenadasFinais[0][0] = linha;
                                coordenadasFinais[0][1] = coluna;
                                coordenadasFinais[1][0] = linha+1;
                                coordenadasFinais[1][1] = coluna-1;
                                coordenadasFinais[2][0] = linha+2;
                                coordenadasFinais[2][1] = coluna-2;
                                coordenadasFinais[3][0] = subLinha;
                                coordenadasFinais[3][1] = subColuna;
                                return;
                            }
                        } else
                            break;
                    }
                }
        mudaJogador();
    }
    private void mudaJogador(){
        Jogador aux;
        aux = jogadorAtual;
        jogadorAtual = proximoJogador;
        proximoJogador = aux;
    }
    private void setVencedor(Jogador jogadorAtual, String modo, int a, int b, int c, int d){
        getLog();
        vencedor = jogadorAtual;
        log.append("Jogador ").append(vencedor.getNome()).append(" ganhou na ").append(modo).append("!\n");
        log.append("                                                                              [ Linha , Coluna ]\n");
        log.append("Inicio da combinação de 4 peças [      ").append(a);
        log.append("       ,      ").append(b).append("         ]\n");
        log.append("Final  da combinação de 4 peças  [      ").append(c);
        log.append("       ,      ").append(d).append("         ]\n\n");
        logGUI.append(getLog());
        if (vencedor.isHumano())
            log.append(getTabuleiro());
        replay.add(log.toString());
    }
    private void limpaTabuleiro(){
        for (int linha = 0; linha < 6; linha++)
            for (int coluna = 0; coluna < 7; coluna++)
                jogo[linha][coluna] = 0;
    }
    private void addToReplay(StringBuilder auxReplay){
        replay.add(auxReplay.toString());
        auxReplay.setLength(0);
    }


    //---------------------------------------retornos para estados---------------------------------------------
    public boolean jogadorAtualIsHumano(){
        return jogadorAtual.isHumano();
    }
    public boolean existeVencedor() {
        return vencedor != null;
    }
    public boolean empate(){
        for (int coluna = 0; coluna < 7; coluna++)
            if (jogo[0][coluna] == 0)
                return false;
        if (vencedor == null) {
            log.append("Empate!");
            logGUI.append("Empate!");
            return true;
        }
        return false;
    }
    public boolean jogadorAtualMiniJogo(){
        return jogadorAtual.miniJogo();
    }
    public boolean jogadorAtualTemPecaEspecial() {
        return jogadorAtual.temPecaEspecial();
    }
    public boolean miniJogoAtualContas(){
        return jogadorAtual.getMiniJogoAtual().equals("contas");
    }

    //---------------------------------------retornos para interface-------------------------------------------
    public String getJogadorAtual(){
        StringBuilder resultado = new StringBuilder();
        resultado.append("'").append(jogadorAtual.getNome()).append("' (").append(jogadorAtual.getNumero());
        resultado.append(")");
        return resultado.toString();
    }
    public String getLog(){
        String LOG = log.toString();
        log.setLength(0);
        return LOG;
    }
    //->MiniJogos
    public boolean miniJogoAtivo(){
        if (jogadorAtual.getMiniJogoAtual().equals("contas"))
            return miniJogoContas.ativo();
        else return false;
    }
    public String getMiniJogoAtualDescricao(){
        if (jogadorAtual.getMiniJogoAtual().equals("contas"))
            return miniJogoContas.getDescricao();
        else
            return miniJogoPalavras.getDescricao();
    }


    //UNDO
    public boolean preparaFazerUndo(int vezes){
        if (!jogadorAtual.preparaUndo(vezes)) {
            log.append("Não tem créditos para fazer Undo.");
            return false;
        }
        return true;
    }
    public Jogador manterAtual() {
        return jogadorAtual;
    }
    public Jogador manterProximo() {
        return proximoJogador;
    }
    public void setAtual(int vezes, Jogador atual, Jogador proximo, ArrayList<String> replay) {
        log.append("\nUndo feito!\n");
        log.append(getTabuleiro());

        proximoJogador = proximo;
        jogadorAtual = atual;

        this.replay = replay;
        auxReplay.append("Jogador ").append(getJogadorAtual()).append(" fez undo ").append(vezes);
        auxReplay.append(" vezes!\n").append(getTabuleiro());
        addToReplay(auxReplay);

        jogadorAtual.fezUndo(vezes,atual);
    }

    //Gravação
    public String getNomeGravacao(String modo){
        StringBuilder g = new StringBuilder();
        g.append(modo);
        g.append(jogadorAtual.getNome()).append("_VS_").append(proximoJogador.getNome()).append("_");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        g.append(dtf.format(now));
        log.append("Nome da gravação: ").append(g);
        return g.toString();
    }
    public ArrayList<String> getReplay(){
        return replay;
    }


    //add para gui
    public String getNomeJogador(int nr) {
        if(jogadorAtual == null && proximoJogador == null)
            return "";
        if (jogadorAtual.getNumero() == nr)
            return jogadorAtual.getNome();
        else
            return proximoJogador.getNome();
    }
    public boolean isHumano(int nr){
        if (jogadorAtual.getNumero() == nr)
            return jogadorAtual.isHumano();
        else
            return proximoJogador.isHumano();
    }

    public int getCreditos(int nr) {
        if (jogadorAtual.getNumero() == nr)
            return jogadorAtual.getCreditos();
        else
            return proximoJogador.getCreditos();
    }

    public int getJogadasAteMiniJogo(int nr) {
        if (jogadorAtual.getNumero() == nr)
            return jogadorAtual.getJogadasAteMiniJogo();
        else
            return proximoJogador.getJogadasAteMiniJogo();
    }

    public int[][] getJogo() {
        return jogo;
    }

    public String getLogGUI() {
        String LOG = logGUI.toString();
        logGUI.setLength(0);
        return LOG;
    }

    public boolean meuTurno(int nr) {
        return jogadorAtual.getNumero() == nr;
    }

    public boolean tenhoPecaEspecial() {
        return jogadorAtual.temPecaEspecial();
    }

    public String getPergunta() {
        if (jogadorAtual.getMiniJogoAtual().equals("contas") && miniJogoContas.ativo())
            return miniJogoContas.operacao();
        else
            return miniJogoPalavras.getPalavras();
    }

    public int getPecasEspeciais(int nr) {
        if (jogadorAtual.getNumero() == nr)
            return jogadorAtual.getPecasEspeciais();
        else
            return proximoJogador.getPecasEspeciais();
    }
}
