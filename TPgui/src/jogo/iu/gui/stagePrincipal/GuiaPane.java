package jogo.iu.gui.stagePrincipal;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import jogo.iu.gui.resources.CSSManager;
import jogo.iu.gui.stageMiniJogos.StageContas;
import jogo.iu.gui.stageMiniJogos.StagePalavras;
import jogo.logica.JogoObs;

public class GuiaPane extends BorderPane {
    private final JogoObs jogoObs;
    private Label texto;

    public GuiaPane(JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        CSSManager.setCSS(this,"mystyles.css");
        criarLayout();
        jogoObs.addObserver(JogoObs.REGISTO, e -> atualiza());
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> atualiza());
        jogoObs.addObserver(JogoObs.ESPECIAL, e -> atualiza());
        jogoObs.addObserver(JogoObs.PALAVRASMJ, e -> atualiza());
    }

    private void criarLayout() {
        this.setWidth(Double.MAX_VALUE);
        this.setPrefHeight(80);
        this.setPadding(new Insets(5));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE,new CornerRadii(50),null)));
        texto = new Label("Registar jogadores!");
        texto.setId("info-text");
        this.setCenter(texto);
    }

    private void atualiza() {
        switch (jogoObs.getSituacaoAtual()) {
            case AGUARDA_CONFIGURACAO   -> texto.setText("Registar jogadores!");
            case AGUARDA_JOGADA_HUMANA  -> texto.setText("Turno do jogador " + jogoObs.getJogadorAtual() +
                    "   Carregar numa tecla de 1 a 7!");
            case VER_JOGADA_MAQUINA     -> texto.setText("Turno da máquina " + jogoObs.getJogadorAtual() +
                    "   Carregar enter para ver jogada!");
            case AGUARDA_DECISAO_MJ     -> perguntaMiniJogo();
            case MINI_JOGO_CONTAS       -> new StageContas(getScene().getWindow(), jogoObs);
            case MINI_JOGO_PALAVRAS     -> new StagePalavras(getScene().getWindow(), jogoObs);
            case AGUARDA_DECISAO_PE     -> jogoObs.ignoraEstadoPedidoEspecial();
            case AGUARDA_JOGADA_ESPECIAL-> texto.setText("Turno ESPECIAL do jogador " + jogoObs.getJogadorAtual() +
                    "   Carregar numa tecla de 1 a 6!");
            case REPLAY-> texto.setText("REPLAY carregue enter para próxima jogada!");
            case FIM_JOGO               -> jogoObs.mostraFinal();
        }
    }

    private void perguntaMiniJogo() {
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Aceita o mini jogo?!?", sim, nao);
        alert.setTitle("MiniJogo?");
        alert.setHeaderText(jogoObs.getDescricaoMiniJogo());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();

        alert.setOnCloseRequest(dialogEvent -> {
            ButtonType resultado = alert.getResult();
            if (resultado.equals(nao))
                jogoObs.recusaMiniJogo();
            else {
                jogoObs.aceitaMiniJogo();
                atualiza();
            }
        });
    }
}
