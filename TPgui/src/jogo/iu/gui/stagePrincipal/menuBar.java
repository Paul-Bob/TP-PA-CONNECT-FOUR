package jogo.iu.gui.stagePrincipal;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import jogo.logica.JogoObs;

import java.io.File;

public class menuBar extends MenuBar {
    private final JogoObs jogoObs;
    private MenuItem mnNovo;
    private MenuItem mnCarrega;
    private MenuItem mnGrava;

    public menuBar(JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        jogoObs.addObserver(JogoObs.REGISTO, e -> atualiza());
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> atualiza());
        criarLayout();
        registarListeners();
    }

    private void criarLayout() {
        Menu jogo = new Menu("Jogo");
        {
            mnNovo = new MenuItem("_Novo");
            {
                mnNovo.setDisable(true);
                mnNovo.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
                mnNovo.setOnAction(e -> {
                    ButtonType sim = new ButtonType("Sim", ButtonBar.ButtonData.OK_DONE);
                    ButtonType nao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION,
                            "Deseja manter os jogadores atuais?!?", sim, nao);
                    alert2.setTitle("Configuração");
                    alert2.setHeaderText("Manter jogadores?");
                    alert2.show();
                    alert2.setOnCloseRequest(dialogEvent -> {
                        ButtonType resultado = alert2.getResult();
                        if (resultado.equals(nao)) {
                            jogoObs.novoJogo();
                        } else
                            jogoObs.comecaJogo();
                    });
                });
            }
            mnCarrega = new MenuItem("_Carrega");
            {
                mnCarrega.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
                mnCarrega.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Carrega jogo...");
                    fileChooser.setInitialDirectory(new File("gravacoes"));
                    File hFile = fileChooser.showOpenDialog(menuBar.this.getScene().getWindow());
                    if (hFile != null)
                        jogoObs.carregaGUI(hFile);
                });
            }
            mnGrava = new MenuItem("_Grava");
            {
                mnGrava.setDisable(true);
                mnGrava.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
                mnGrava.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Grava jogo...");
                    fileChooser.setInitialDirectory(new File("gravacoes"));
                    File hFile = fileChooser.showSaveDialog(menuBar.this.getScene().getWindow());
                    if (hFile != null)
                        jogoObs.gravaGUI(hFile);
                });
            }
            MenuItem mnReplay = new MenuItem("Replay");
            {
                mnReplay.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
                mnReplay.setOnAction(e -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Ver replay...");
                    fileChooser.setInitialDirectory(new File("replays"));
                    File hFile = fileChooser.showOpenDialog(menuBar.this.getScene().getWindow());
                    if (hFile != null)
                        jogoObs.carregaREPLAY(hFile);
                });
            }

            MenuItem mnSair = new MenuItem("_Sair");
            mnSair.setOnAction(e-> Platform.exit());

            jogo.getItems().addAll(mnNovo,mnCarrega,mnGrava,new SeparatorMenuItem(), mnReplay,new SeparatorMenuItem(), mnSair);
        }

        this.getMenus().addAll(jogo);
    }

    private void registarListeners() {

    }

    private void atualiza(){
        switch (jogoObs.getSituacaoAtual()) {
            case AGUARDA_CONFIGURACAO   -> {
                mnNovo.setDisable(true);
                mnGrava.setDisable(true);
                mnCarrega.setDisable(false);
            }
            case AGUARDA_JOGADA_HUMANA, VER_JOGADA_MAQUINA -> {
                mnNovo.setDisable(false);
                mnGrava.setDisable(false);
                mnCarrega.setDisable(false);
            }

            case FIM_JOGO               -> {
                mnNovo.setDisable(true);
                mnGrava.setDisable(false);
                mnCarrega.setDisable(false);
            }
        }
    }

}
