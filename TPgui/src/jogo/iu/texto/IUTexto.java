package jogo.iu.texto;

import jogo.logica.GestaoJogo;

import java.util.ArrayList;
import java.util.Scanner;

public class IUTexto {
    private boolean corre = true;
    GestaoJogo jogo;
    Scanner sc = new Scanner(System.in).useDelimiter("\\n");

    public IUTexto(GestaoJogo jogo){
        this.jogo = jogo;
    }

    public void corre(){
        System.out.println("Bem-vindo(s) ao jogo 4 em linha!");
        System.out.println("Digite 'sair' a qualquer altura para abandonar o jogo.");
        System.out.println("Digite 'undo' a qualquer altura para fazer undo. (caso tenha creditos)");
        System.out.println("Enter para continuar.");
        while (corre){
            switch (jogo.getSituacaoAtual()) {
                case AGUARDA_CONFIGURACAO   -> IUConfiguracao();
                case AGUARDA_JOGADA_HUMANA  -> IUJogadaHumano();
                case VER_JOGADA_MAQUINA     -> IUVerJogadaMaquina();
                case AGUARDA_DECISAO_MJ     -> IUPerguntaMiniJogo();
                case MINI_JOGO_CONTAS       -> IUMiniJogoContas();
                case MINI_JOGO_PALAVRAS     -> IUMiniJogoPalavras();
                case AGUARDA_DECISAO_PE     -> IUPerguntaPecaEspecial();
                case AGUARDA_JOGADA_ESPECIAL-> IUJogadaEspecial();
                case FIM_JOGO               -> IUFim();
            }
        }
        System.out.println(jogo.getLog());
        System.out.println("Espero que se tenha/m divertido.\nAté a próxima!\n");
    }

    private void IUConfiguracao(){
        //Supostamente criar mais um estado e nova interface (SEM TEMPO)
        if (IUGetResposta("Continuar jogo a partir de alguma gravação!? [(S)im/(N)ão] ")){
            System.out.println("\nGravações disponíveis:");
            System.out.println(jogo.listarGravacoes("gravacao_"));
            System.out.println("(Sugestão: Copy Paste)");
            System.out.print("Nome da gravação que deseja carregar: ");
            jogo.carrega(sc.next());
            System.out.println();
            return;
        }
        //Supostamente criar mais um estado e nova interface (SEM TEMPO)
        if (IUGetResposta("Ver replay de algum dos 5 últimos jogos?! [(S)im/(N)ão] ")){
            System.out.println("\nReplays disponíveis:");
            System.out.println(jogo.listarGravacoes("replay_"));
            System.out.println("(Sugestão: Copy Paste)");
            System.out.print("Nome do gravação que deseja carregar: ");
            jogo.carrega(sc.next());
            System.out.println();
            ArrayList<String> replay = jogo.getReplay();
            for(String jogada : replay) {
                System.out.print("Prime enter para continuar e ver a jogada. ");
                System.out.println();
                try{        System.in.read();}catch(Exception e){	e.printStackTrace();}
                System.out.println(jogada);
            }
            jogo.getLog();
            corre = false;
            return;
        }

        for (int i = 1; i <= 2; i++) {
            String nome;
            do {
                System.out.print("\nNome Jogador" + i + ": ");
                nome = sc.next();
                if (nome.equals("sair")) {
                    corre = false;
                    return;
                }
            } while (nome.length() < 1);
            sc.nextLine();
            String tipo;
            while(true){
                System.out.print("(H)umano ou (M)áquina?! ");
                tipo = sc.nextLine().trim().toUpperCase();
                if(tipo.equals("H") || tipo.equals("M")){
                    jogo.registaJogador(i, nome, tipo.charAt(0));
                    break;
                }
            }
            System.out.println(jogo.getLog());
        }
    }
    private void IUJogadaHumano(){
        System.out.println(jogo.getLog());
        System.out.print("Inserir coluna onde deseja jogar [1,7]: ");
        int coluna = IUGetColuna();
        if(coluna == -1)
            return;
        System.out.println("\n|>--------------------->N*E*X*T<---------------------<|");
        jogo.joga(coluna-1);

    }
    private void IUVerJogadaMaquina(){
        System.out.print("Máquina vai jogar!\nPrime enter para continuar e ver a jogada. ");
        try{        System.in.read();}catch(Exception e){	e.printStackTrace();}
        jogo.verJogadaMaquina();
        System.out.println(jogo.getLog());
    }
    private void IUFim(){
        System.out.println(jogo.getLog());
        jogo.gravaReplay();
        if (!IUGetResposta("Novo jogo? [(S)im/(N)ão] ")) {
            corre = false;
            return;
        }

        if (IUGetResposta("Manter jogadores? [(S)im/(N)ão] "))
            jogo.comecaJogo();
        else
            jogo.novoJogo();
    }
    private void IUPerguntaMiniJogo(){
        System.out.println(jogo.getLog());
        System.out.println("\nA cada 4 jogadas efetuadas a quinta pode escolher jogar um mini jogo.");
        System.out.println("Descrição do mini-jogo:\n");
        System.out.println(jogo.getMiniJogoAtualDescricao());
        System.out.println("\nCaso ganhe, ganha uma peça especial caso perca, perde a vez de jogar.");
        if (IUGetResposta(jogo.getJogadorAtual() + " aceita jogar o mini jogo? [(S)im/(N)ão] "))
            jogo.aceitaMiniJogo();
        else
            jogo.recusaMiniJogo();
    }
    private void IUPerguntaPecaEspecial(){
        System.out.println(jogo.getLog());
        System.out.println("Tem pelo menos uma peça especial.");
        System.out.println("A peça especial tem o efeito de fazer desaparecer todas as peças da coluna onde é jogada.");
        if (IUGetResposta("Deseja jogar uma peça especial? [(S)im/(N)ão] "))
            jogo.aceitaPecaEspecial();
        else
            jogo.recusaPecaEspecial();
    }
    private void IUMiniJogoContas(){
        float resposta;
        while (jogo.miniJogoAtivo()) {
            System.out.print(jogo.getLog());
            if(sc.hasNextFloat()) {
                resposta = sc.nextFloat();
                jogo.miniJogoVerificaResposta(resposta,"" );
            }
            else{
                System.out.println("Input inválido.");
                sc.next();
            }
        }
        jogo.fimMiniJogo();
    }
    private void IUMiniJogoPalavras(){
        String resposta;
        System.out.println(jogo.getLog());
        System.out.print("Resposta  -> ");
        resposta = sc.next();
        jogo.miniJogoVerificaResposta(0,resposta);
        System.out.println(jogo.getLog());
    }
    private void IUJogadaEspecial(){
        System.out.println(jogo.getLog());
        System.out.print("Inserir coluna que deseja limpar [1,7]: ");
        int coluna = IUGetColuna();
        if(coluna == -1)
            return;
        jogo.jogaPecaEspecial(coluna-1);
        System.out.println("\n|>--------------------->N*E*X*T<---------------------<|");
    }

    //úteis
    private int IUGetColuna(){
        int coluna = 0;
        String comando = "";
        while(sc.hasNext() ){
            if (sc.hasNextInt()){
                coluna = sc.nextInt();
                if (coluna > 0 && coluna < 8)
                    break;
            } else
                comando = sc.next();
            switch (comando){
                case "sair" -> {
                    if(IUGetResposta("Deseja gravar jogo atual?! [(S)im/(N)ão] "))
                        jogo.grava();
                    corre = false;
                    return -1;
                }
                case "undo" -> {
                    int vezes = 0;
                    do{
                        System.out.println("Quantas vezes [1,5] ?");
                        if (sc.hasNextInt()) {
                            vezes = sc.nextInt();
                        } else
                            clearBuffer(sc);
                    }  while(vezes < 1 || vezes > 5);
                    jogo.undo(vezes);
                    return -1;
                }
            }
        }
        return coluna;
    }
    private boolean IUGetResposta(String pergunta) {
        String resposta;
        while (true) {
            clearBuffer(sc);
            System.out.print(pergunta);
            resposta = sc.next().trim().toUpperCase();
            if (resposta.equals("S"))
                return true;
            if (resposta.equals("N"))
                return false;
        }
    }
    private static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}

