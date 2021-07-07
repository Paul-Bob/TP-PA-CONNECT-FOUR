package jogo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jogo.iu.gui.stagePrincipal.PaneOrganizer;
import jogo.logica.GestaoJogo;
import jogo.logica.JogoObs;

public class MainJFX extends Application {
    @Override
    public void start(Stage stage){
        GestaoJogo gJ = new GestaoJogo();
        JogoObs jO = new JogoObs(gJ);
        PaneOrganizer root = new PaneOrganizer(jO);
        Scene scene = new Scene(root, 1345,855);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("4 em Linha");

        stage.show();
    }
}
