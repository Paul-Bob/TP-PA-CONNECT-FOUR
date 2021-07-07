package jogo.iu.gui.stagePrincipal;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import jogo.iu.gui.resources.CSSManager;
import jogo.iu.gui.resources.FontsManager;
import jogo.logica.JogoObs;

public class PaneRegisto extends VBox {
    private final JogoObs jogoObs;
    private final Label titulo;
    private TextField tfNome;
    private RadioButton humano;
    private RadioButton maquina;
    private Button registarJogador;
    private final int nr;

    public PaneRegisto(JogoObs jogoObs, int nr) {
        this.jogoObs = jogoObs;
        this.nr = nr;
        CSSManager.setCSS(this,"mystyles.css");
        titulo = new Label("  Registo do jogador" + nr);
        titulo.setFont(FontsManager.loadFont("Pacifico.ttf",30));
        criarLayout();
        registarListeners();
    }

    private void registarListeners() {
        this.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER)
                registarJogador.fire();
            e.consume();
        });
        registarJogador.setOnAction(e->{
            if (tfNome.getText().length() < 1){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Inserir nome man!");
                alert.setTitle("Nome Jogador!");
                alert.setHeaderText("OBRIGATÓRIO");
                alert.show();
                return;
            }
            if (!humano.isSelected() && !maquina.isSelected()){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Escolher se jogador é humano ou máquina!");
                alert.setTitle("Tipo Jogador!");
                alert.setHeaderText("OBRIGATÓRIO");
                alert.show();
                return;
            }
            if (humano.isSelected())
                jogoObs.registaJogador(nr,tfNome.getText(),'H');
            if (maquina.isSelected())
                jogoObs.registaJogador(nr,tfNome.getText(),'M');
        });
    }

    private void criarLayout() {
        this.setPrefWidth(310);
        this.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(50),null)));
        this.setBorder(new Border(new BorderStroke(Color.LAVENDER,BorderStrokeStyle.DASHED,
                new CornerRadii(50),new BorderWidths(5))));
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(30);

        Label nome = new Label("Nome: ");
        nome.setId("my-text");
        tfNome = new TextField("");
        humano = new RadioButton("Humano");
        humano.setId("my-text");
        maquina = new RadioButton("Máquina");
        maquina.setId("my-text");
        ToggleGroup radioGroup = new ToggleGroup();
        radioGroup.getToggles().addAll(humano,maquina);

        registarJogador = new Button("Registar Jogador");
        registarJogador.setMaxWidth(Double.MAX_VALUE);
        registarJogador.setPrefHeight(40);

        grid.add(nome,1,2);
        grid.add(tfNome,2,2);
        grid.add(humano,1,1);
        grid.add(maquina,2,1);

        this.setPadding(new Insets(10));
        this.setSpacing(5);
        this.getChildren().addAll(titulo,grid,new Label(),new Separator(),registarJogador);
    }

    public void tornarBotaoAtivo(){
        registarJogador.setDisable(false);
    }

    public void tornarBotaoDesativo(){
        registarJogador.setDisable(true);
    }
}
