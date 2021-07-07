package jogo.iu.gui.stagePrincipal;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import jogo.logica.JogoObs;
import jogo.logica.JogoSituacao;


public class PaneOrganizer extends BorderPane {
    private final JogoObs jogoObs;
    private PaneJogador jogador1;
    private Tabuleiro tabuleiro;
    private PaneRegisto registoEsquerda;
    private PaneRegisto registoDireita;
    private boolean registoPrimeiroJogador;

    public PaneOrganizer(JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        jogoObs.addObserver(JogoObs.REGISTO, e -> registeiJogador());
        jogoObs.addObserver(JogoObs.FINAL, e -> fim());
        jogoObs.addObserver(JogoObs.UNDO, e -> this.requestFocus());
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> this.requestFocus());
        jogoObs.addObserver(JogoObs.CARREGA, e -> carregaInicio());
        jogoObs.addObserver(JogoObs.PREPARA, e -> preparaRegisto());
        criarLayout();
        registarListeners();
    }

    private void criarLayout() {
        VBox top = new VBox();
        tabuleiro = new Tabuleiro(jogoObs);

        GuiaPane guia = new GuiaPane(jogoObs);
        MenuBar menu = new menuBar(jogoObs);

        top.getChildren().addAll(menu, guia);

        this.setTop(top);

        registoEsquerda = new PaneRegisto(jogoObs,1);
        this.setLeft(registoEsquerda);


        this.setCenter(tabuleiro);

        registoDireita= new PaneRegisto(jogoObs,2);
        registoDireita.tornarBotaoDesativo();
        this.setRight(registoDireita);

        InfoPane infoPane = new InfoPane(jogoObs);
        this.setBottom(infoPane);
    }

    private void registarListeners() {
        this.setOnKeyTyped(e->{
            switch (e.getCharacter()){
                case "1" -> jogoObs.joga(0);
                case "2" -> jogoObs.joga(1);
                case "3" -> jogoObs.joga(2);
                case "4" -> jogoObs.joga(3);
                case "5" -> jogoObs.joga(4);
                case "6" -> jogoObs.joga(5);
                case "7" -> jogoObs.joga(6);
            }
        });

        this.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                if (jogoObs.getSituacaoAtual() == JogoSituacao.REPLAY)
                    jogoObs.next();
                else
                    jogoObs.verJogadaMaquina();
            }
        });
    }

    public void registeiJogador(){
        if (!registoPrimeiroJogador) {
            registoDireita.tornarBotaoAtivo();
            jogador1 = new PaneJogador(jogoObs,1);
            this.setLeft(jogador1);
            registoPrimeiroJogador = true;
            registoEsquerda = null;
        }
        else{
            PaneJogador jogador2 = new PaneJogador(jogoObs, 2);
            this.setRight(jogador2);
            registoDireita = null;
            registoPrimeiroJogador = false;
            this.requestFocus();
            jogador1.atualiza();
            jogador2.atualiza();
        }
    }

    private void fim(){
        jogoObs.gravaReplay();
        ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
        ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Este jogo acabou, deseja jogar outro?", sim, nao);
        alert.setTitle("Fim do jogo");
        alert.setHeaderText("NOVO JOGO?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja manter os jogadores atuais?!?", sim, nao);
        alert2.setTitle("Configuração");
        alert2.setHeaderText("Manter jogadores?");

        alert.setOnCloseRequest(dialogEvent -> {
            ButtonType resultado = alert.getResult();
            if (resultado.equals(nao))
                Platform.exit();
            else {
                alert2.show();
                alert2.setOnCloseRequest(dialogEvent1 -> {
                    ButtonType resultado1 = alert2.getResult();
                    if (resultado1.equals(nao)) {
                        jogoObs.novoJogo();
                    }
                    else
                        jogoObs.comecaJogo();
                });
            }
        });
    }

    private void preparaRegisto(){
        tabuleiro.atualiza();
        registoEsquerda = new PaneRegisto(jogoObs,1);
        registoDireita = new PaneRegisto(jogoObs,2);
        registoDireita.tornarBotaoDesativo();
        this.setLeft(registoEsquerda);
        this.setRight(registoDireita);
    }

    public void carregaInicio(){
        registoPrimeiroJogador = false;
        preparaRegisto();
    }
}
