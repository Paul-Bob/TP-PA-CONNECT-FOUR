package jogo.iu.gui.stagePrincipal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jogo.iu.gui.resources.CSSManager;
import jogo.iu.gui.resources.FontsManager;
import jogo.iu.gui.resources.ImageLoader;
import jogo.logica.JogoObs;

public class PaneJogador extends VBox {
    private final JogoObs jogoObs;
    private final int nr;
    private int creditosAtuais;
    private Label nome;
    private Label creditos;
    private Label undosFeitos;
    private Label jogadasAteMiniJogo;
    private Label pecasEspeciais;
    private Button jogadaEspecial, undo;
    private boolean isHumano;

    public PaneJogador(JogoObs jogoObs, int nr) {
        this.nr = nr;
        this.jogoObs = jogoObs;
        CSSManager.setCSS(this,"mystyles.css");
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> atualiza());
        criarLayout();
        atualiza();
        this.setBorder(new Border(new BorderStroke(Color.LAVENDER,BorderStrokeStyle.DASHED,
                new CornerRadii(50),new BorderWidths(5))));
        jogadaEspecial.setDisable(true);
        undo.setDisable(true);
        registarListeners();
    }

    private void registarListeners() {
        jogadaEspecial.setOnAction(e-> jogoObs.aceitaPecaEspecial());
        undo.setOnAction(e->{
            jogoObs.undo();
            if(creditosAtuais == 0)
                undo.setDisable(true);
        });
    }

    private void criarLayout() {
        this.setPrefWidth(300);

        isHumano = jogoObs.isHumano(nr);
        Label titulo;
        if(isHumano)
            titulo = new Label("Jogador Humano!");
        else
            titulo = new Label("Jogador Máquina!");
        titulo.setFont(FontsManager.loadFont("Pacifico.ttf",32));


        nome = new Label(jogoObs.getNomeJogador(nr));
        nome.setFont(FontsManager.loadFont("Pacifico.ttf",26));
        nome.setMaxWidth(Double.MAX_VALUE);
        nome.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(50),null)));
        creditos = new Label();
        creditos.setId("my-text");
        undosFeitos = new Label();
        undosFeitos.setId("my-text");
        jogadasAteMiniJogo = new Label();
        jogadasAteMiniJogo.setId("my-text");
        pecasEspeciais = new Label();
        pecasEspeciais.setId("my-text");

        Label lblCor = new Label("Peças: ");
        lblCor.setId("my-text");
        lblCor.setPadding(new Insets(3,0, 0,0));

        Circle cor;
        if(nr == 1) {
            cor = new Circle(0, 15, 15, Color.YELLOW);
            cor.setStroke(Color.YELLOW.darker());
        }
        else {
            cor = new Circle(0, 15, 15, Color.RED);
            cor.setStroke(Color.RED.darker());
        }
        cor.setStrokeWidth(3);

        HBox corPeca = new HBox();
        corPeca.setSpacing(5);
        corPeca.getChildren().addAll(lblCor, cor);


        this.getChildren().addAll(titulo,nome, corPeca, creditos, undosFeitos, jogadasAteMiniJogo, pecasEspeciais);
        if (!isHumano) {
            Image imgAI = ImageLoader.getImage("ai.png");
            ImageView viewAI = new ImageView(imgAI);
            viewAI.setFitWidth(280);
            viewAI.setPreserveRatio(true);
            this.getChildren().addAll(viewAI);
        }
        else {
            Image imgAI = ImageLoader.getImage("human.png");
            ImageView viewAI = new ImageView(imgAI);
            viewAI.setFitWidth(280);
            viewAI.setPreserveRatio(true);
            this.getChildren().addAll(new Label(),new Label(),viewAI);
        }

        jogadaEspecial = new Button("Fazer jogada especial");
        undo = new Button("Undo");

        jogadaEspecial.setPrefWidth(Double.MAX_VALUE);
        undo.setPrefWidth(Double.MAX_VALUE);
        jogadaEspecial.setPrefHeight(50);
        undo.setPrefHeight(50);
        this.getChildren().addAll(new Separator() , new Label(), jogadaEspecial, new Label(), undo);
    }

    public void atualiza(){
        nome = new Label(jogoObs.getNomeJogador(nr));
        if (isHumano) {
            creditosAtuais = jogoObs.getCreditos(nr);
            creditos.setText("Créditos        : " + creditosAtuais);
            undosFeitos.setText("Undos feitos : " + (5 - creditosAtuais));
            jogadasAteMiniJogo.setText("Jogadas até mini jogo  -> " + jogoObs.getJogadasAteMiniJogo(nr));
            pecasEspeciais.setText("Nrº de peças especiais -> " + jogoObs.getPecasEspeciais(nr));
            if (jogoObs.meuTurno(nr)) {
                this.setBorder(new Border(new BorderStroke(Color.LAVENDER.darker(),BorderStrokeStyle.DASHED,
                        new CornerRadii(50),new BorderWidths(5))));
                if (jogoObs.tenhoPecaEspecial())
                    jogadaEspecial.setDisable(false);
                if(creditosAtuais > 0)
                    undo.setDisable(false);
            }
            else{
                this.setBorder(new Border(new BorderStroke(Color.LAVENDER,BorderStrokeStyle.DASHED,
                        new CornerRadii(50),new BorderWidths(5))));
                jogadaEspecial.setDisable(true);
                undo.setDisable(true);
            }
        }
        else {
            if (jogoObs.meuTurno(nr))
                this.setBorder(new Border(new BorderStroke(Color.LAVENDER.darker(), BorderStrokeStyle.DASHED,
                        new CornerRadii(50), new BorderWidths(5))));
            else
                this.setBorder(new Border(new BorderStroke(Color.LAVENDER,BorderStrokeStyle.DASHED,
                        new CornerRadii(50),new BorderWidths(5))));
        }
    }
}
