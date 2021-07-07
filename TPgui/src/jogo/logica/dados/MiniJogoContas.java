package jogo.logica.dados;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class MiniJogoContas implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long fim;
    private float resultado;
    private int pontuacao = 0;
    Random r = new Random();

    public MiniJogoContas(){}

    public String inicia(){
        long tempo = System.currentTimeMillis();
        fim = tempo +30000;
        return operacao();
    }

    public boolean ativo(){
        return (System.currentTimeMillis() < fim);
    }

    public String operacao(){
        int low = 1;
        int high = 100;
        int a = r.nextInt(high-low) + low;
        int b = r.nextInt(high-low) + low;

        String chars = "+-*/";
        char operador = chars.charAt(r.nextInt(chars.length()));

        switch (operador) {
            case '+' -> resultado = a + b;
            case '-' -> resultado = a - b;
            case '*' -> resultado = a * b;
            case '/' -> resultado = (float) a / b;
        }

        return String.valueOf(a) + operador + b;
    }

    public String verificaResposta(float resposta){
        if ( resposta == resultado ) {
            pontuacao++;
            return "Acertou!\n";
        }
        else
            return "Errou... \nA resposta correta era " + resultado + '\n';
    }

    public int getPontuacao(){return pontuacao;}
    public String getDescricao() {
        return """
                    Serão propostos cálculos matemáticos simples usando os operadores básicos (+,-,/,x) e dois
                    inteiros positivos (de 1 ou 2 dígitos cada um). Os números e os operadores a usar são sorteados.
                    Caso o jogador acerte em 5 cálculos em menos de 30 segundos ganha a peça especial.
                    Utilize virgula para decimais""";
    }
    public void resetPontuacao(){
        pontuacao = 0;
    }
}
