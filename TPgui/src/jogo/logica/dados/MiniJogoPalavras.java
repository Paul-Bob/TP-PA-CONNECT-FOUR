package jogo.logica.dados;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class MiniJogoPalavras implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private int pontuacao = 0;
    private final int nrPalavras;
    private final File ficheiro = new File("palavras.txt");
    Random r = new Random();
    StringBuilder sequencia = new StringBuilder();
    private long tempoInicio;

    public MiniJogoPalavras(){
        nrPalavras = countLineFast(ficheiro.getAbsolutePath());
    }


    public String getPalavras(){
        sequencia.setLength(0);
        int low = 0;
        for (int i = 0; i < 5; i++){
            int linha = r.nextInt(nrPalavras -low) + low;
            try {
                Scanner myReader = new Scanner(ficheiro);
                int atual = 0;
                while (myReader.hasNextLine() && atual < linha) {
                    myReader.nextLine();
                    atual++;
                }
                sequencia.append(myReader.nextLine());
                if (i < 4)
                    sequencia.append(" ");
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        tempoInicio = System.currentTimeMillis();
        return sequencia.toString();
    }

    public String verificaResposta(String resposta){
        StringBuilder retorno = new StringBuilder();
        long tempoFinal = System.currentTimeMillis();
        float tempoDecorrido = (tempoFinal - tempoInicio) / 1000F;
        float tempoEsperado = (float) sequencia.length() / 2;
        retorno.append("Demorou ").append(tempoDecorrido).append(" segundos a responder!\n");
        retorno.append("Tempo mínimo de resposta era ").append(tempoEsperado).append(" segundos!\n");
        if (resposta.equals(sequencia.toString())) {
            if (tempoDecorrido <= tempoEsperado)
                pontuacao = 1;
        }
        else {
                pontuacao = 0;
                retorno.append("A resposta não coincide na íntegra com a sequência apresentada!\n");
        }
        return retorno.toString();
    }

    public int getPontuacao(){return pontuacao;}
    public String getDescricao() {
        return """
                    São apresentadas 5 palavras escolhidas aleatoriamente de um ficheiro de texto com 100 ou mais
                    palavras, cada uma de 5 ou mais letras. Deve escrever essas palavras e será avaliada a
                    sua rapidez. É contabilizado o tempo que passa entre a apresentação da sequência dessas
                    palavras e a indicação de finalização da escrita. Ganha a peça especial se digitar
                    corretamente as palavras num período inferior ou igual ao número de segundos
                    que corresponde a metade do número de caracteres apresentados, incluindo os espaços em
                    branco.""";
    }
    public void resetPontuacao(){
        pontuacao = 0;
    }


    //Da internet
    //https://mkyong.com/java/how-to-get-the-total-number-of-lines-of-a-file-in-java/
    public static int countLineFast(String fileName) {

        int lines = 0;

        try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if (endsWithoutNewLine) {
                ++count;
            }
            lines = count;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
