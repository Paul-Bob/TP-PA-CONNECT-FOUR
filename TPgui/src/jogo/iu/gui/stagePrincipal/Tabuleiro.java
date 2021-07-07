package jogo.iu.gui.stagePrincipal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import jogo.iu.gui.resources.CSSManager;
import jogo.iu.gui.resources.ImageLoader;
import jogo.iu.gui.resources.MusicPlayer;
import jogo.logica.JogoObs;

public class Tabuleiro extends GridPane {
    JogoObs jogoObs;
    private int[][] tabuleiro = new int[6][7];
    private int[][] coordenadasFinais = new int[4][2];

    public Tabuleiro(JogoObs jogoObs) {
        this.jogoObs = jogoObs;
        CSSManager.setCSS(this,"mystyles.css");
        criarLayout();
        registarListeners();
        jogoObs.addObserver(JogoObs.TABULEIRO, e -> atualiza());
        jogoObs.addObserver(JogoObs.FIM, e -> mostraFinal());
    }

    private void criarLayout() {
        this.setBackground(new Background(new BackgroundImage(ImageLoader.getImage("tabuleiro.png")
            , BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT)));
        this.setPadding(new Insets(0, 10, 10, -1));
        this.setAlignment(Pos.CENTER);
        this.setHgap(13);
        this.setVgap(13);
        atualiza();
    }

    public void atualiza() {
        tabuleiro = jogoObs.getTabuleiro();
        for (int coluna = 0; coluna < 7; coluna++)
            if (tabuleiro[0][coluna] == 0) {
                Circle circle = new Circle(0, 43.2, 43.2, Color.ANTIQUEWHITE);
                Text text = new Text(""+(coluna+1));
                text.setId("numeros");
                text.setBoundsType(TextBoundsType.VISUAL);
                StackPane stack = new StackPane();
                stack.getChildren().addAll(circle, text);
                add(stack, coluna, 0);
            }
            else if (tabuleiro[0][coluna] == 1)
                add(new Circle(0,43.2,43.2,Color.YELLOW),coluna,0);
            else
                add(new Circle(0,43.2,43.2,Color.RED),coluna,0);

        for (int linha = 1; linha < 6; linha++)
            for (int coluna = 0; coluna < 7; coluna++) {
                if (tabuleiro[linha][coluna] == 0)
                    add(new Circle(0,43.2,43.2,Color.ANTIQUEWHITE),coluna,linha);
                else if (tabuleiro[linha][coluna] == 1)
                    add(new Circle(0,43.2,43.2,Color.YELLOW),coluna,linha);
                else
                    add(new Circle(0,43.2,43.2,Color.RED),coluna,linha);
            }
    }

    public void mostraFinal() {
        MusicPlayer.playMusic("final.mp3");
        coordenadasFinais = jogoObs.getCoordenadasFinais();
        final Color corAtual;
        if (tabuleiro[coordenadasFinais[0][0]][coordenadasFinais[0][1]] == 1)
            corAtual = Color.YELLOW;
        else
            corAtual = Color.RED;

        Timeline timeline = new Timeline();
        KeyFrame interromper,primeiro,segundo,terceiro,quarto,quinto,sexto,setimo,oitavo,nono,decimo,conectar;
        interromper = new KeyFrame(Duration.seconds(0),   e-> jogoObs.interromperListeners());
        primeiro    = new KeyFrame(Duration.seconds(0.5),   e-> alteraCores(corAtual));
        segundo     = new KeyFrame(Duration.seconds(1),     e-> alteraCores(Color.ANTIQUEWHITE));
        terceiro    = new KeyFrame(Duration.seconds(1.5),   e-> alteraCores(corAtual));
        quarto      = new KeyFrame(Duration.seconds(2),     e-> alteraCores(Color.ANTIQUEWHITE));
        quinto      = new KeyFrame(Duration.seconds(2.5),   e-> alteraCores(corAtual));
        sexto       = new KeyFrame(Duration.seconds(3),     e-> alteraCores(Color.ANTIQUEWHITE) );
        setimo      = new KeyFrame(Duration.seconds(3.5),   e-> alteraCores(corAtual));
        oitavo      = new KeyFrame(Duration.seconds(4),   e-> alteraCores(Color.ANTIQUEWHITE));
        nono        = new KeyFrame(Duration.seconds(4.5),   e-> alteraCores(corAtual));
        conectar    = new KeyFrame(Duration.seconds(4.7),   e-> jogoObs.conectarListeners());
        decimo      = new KeyFrame(Duration.seconds(4.6),   e-> jogoObs.configuraFinal());
        timeline.getKeyFrames().addAll(interromper,primeiro,segundo,terceiro,quarto,quinto,sexto,setimo,oitavo,nono,conectar,decimo);
        timeline.play();

    }

    private void alteraCores(Color cor){
        Circle primeiro,segundo,terceiro,quarto;
        primeiro = new Circle(0,41,41,cor);
        primeiro.setStrokeWidth(5);
        primeiro.setStroke(cor.darker());
        segundo = new Circle(0,41,41,cor);
        segundo.setStrokeWidth(5);
        segundo.setStroke(cor.darker());
        terceiro = new Circle(0,41,41,cor);
        terceiro.setStrokeWidth(5);
        terceiro.setStroke(cor.darker());
        quarto = new Circle(0,41,41,cor);
        quarto.setStrokeWidth(5);
        quarto.setStroke(cor.darker());
        add(primeiro,coordenadasFinais[0][1],coordenadasFinais[0][0]);
        add(segundo,coordenadasFinais[1][1],coordenadasFinais[1][0]);
        add(terceiro,coordenadasFinais[2][1],coordenadasFinais[2][0]);
        add(quarto,coordenadasFinais[3][1],coordenadasFinais[3][0]);
    }

    private void registarListeners() {

    }

}
