package jogo;

import jogo.iu.texto.IUTexto;
import jogo.logica.ContextoMaqEstados;
import jogo.logica.GestaoJogo;

public class Main {
    public static void main(String[] args) {
        //ContextoMaqEstados maqEstados = new ContextoMaqEstados();
        GestaoJogo gJ = new GestaoJogo();
        IUTexto vista = new IUTexto(gJ);
        vista.corre();
    }
}
