package jogo.iu.gui.stageMiniJogos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jogo.iu.gui.resources.CSSManager;
import jogo.logica.JogoObs;


public class StagePalavras extends Stage {
    private final JogoObs jogoObs;
    private TextField resposta;
    private Scene scene;

    public StagePalavras(Window parent, JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        createLayout();
        registerListeners();

        CSSManager.setCSS(scene.getRoot(),"mystyles.css");

        initOwner(parent);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }

    private void registerListeners() {
        scene.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    jogoObs.miniJogoVerificaResposta(0, resposta.getText());
                    jogoObs.fimMiniJogo();
                    StagePalavras.this.close();
                } catch (Exception ignored) {

                }
            }
        });
        this.setOnCloseRequest(e -> jogoObs.miniJogoVerificaResposta(0, resposta.getText()));
    }

    private void createLayout() {

        Label lblPalavras = new Label(jogoObs.getPergunta());
        lblPalavras.setPrefWidth(Double.MAX_VALUE);
        lblPalavras.setId("my-text");
        resposta = new TextField();
        resposta.setMaxWidth(Double.MAX_VALUE);

        VBox root = new VBox(10);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(lblPalavras,new Separator(),resposta);
        scene = new Scene(root, 450, 150);
        this.setScene(scene);
        this.setTitle("Mini-Jogo Palavras");
        setResizable(false);
    }
}
