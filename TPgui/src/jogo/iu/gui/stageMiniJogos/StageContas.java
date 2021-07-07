package jogo.iu.gui.stageMiniJogos;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jogo.iu.gui.resources.CSSManager;
import jogo.logica.JogoObs;


public class StageContas extends Stage {
    private JogoObs jogoObs;
    private Label lblConta,lblResultado;
    private TextField resposta;
    private Scene scene;

    public StageContas(Window parent, JogoObs jogoObs) {
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
                    jogoObs.miniJogoVerificaResposta(Float.parseFloat(resposta.getText()), "");
                    resposta.setText("");
                } catch (Exception ignored) {

                }
                if (!jogoObs.miniJogoAtivo()) {
                    jogoObs.fimMiniJogo();
                    StageContas.this.close();
                } else
                    atualiza();
            }
        });
        this.setOnCloseRequest(e -> jogoObs.fimMiniJogo());
    }

    private void atualiza() {
        lblConta.setText(jogoObs.getPergunta() + " =");
        lblResultado.setText(jogoObs.getLogGUI());
    }

    private void createLayout() {
        HBox jogo = new HBox();
        lblConta = new Label(jogoObs.getPergunta() + " =");
        lblConta.setPrefWidth(100);
        lblConta.setId("my-text");
        resposta = new TextField();
        resposta.setMaxWidth(90);
        jogo.getChildren().addAll(lblConta,resposta);
        lblResultado = new Label();
        lblResultado.setId("my-text");
        VBox root = new VBox(10);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(jogo,new Separator(),lblResultado);
        scene = new Scene(root, 250, 150);
        this.setScene(scene);
        this.setTitle("Mini-Jogo Contas");
        setResizable(false);
    }
}
