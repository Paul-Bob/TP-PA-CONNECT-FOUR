package jogo.iu.gui.stagePrincipal;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jogo.iu.gui.resources.CSSManager;
import jogo.logica.JogoObs;

public class InfoPane extends BorderPane {
    private final JogoObs jogoObs;
    private Label texto;

    public InfoPane(JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        CSSManager.setCSS(this,"mystyles.css");
        criarLayout();
        jogoObs.addObserver(JogoObs.REGISTO, e -> atualiza());
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> atualiza());
        jogoObs.addObserver(JogoObs.UNDO, e -> atualizaUNDO());
    }

    private void criarLayout() {
        this.setWidth(Double.MAX_VALUE);
        this.setPrefHeight(125);
        this.setPadding(new Insets(5));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE,new CornerRadii(50),null)));
        texto = new Label("Bem vindo ao Quatro em linha!!");
        texto.setId("info-text");
        this.setCenter(texto);
    }

    private void atualiza() {
        texto.setText(jogoObs.getLogGUI());
    }
    private void atualizaUNDO() {
        texto.setText("Jogador " + jogoObs.getJogadorAtual() + " fez UNDO!");
    }
}
