package jogo.iu.gui.resources;

import javafx.scene.text.Font;

public class FontsManager {
    private FontsManager() {}

    public static Font loadFont(String name, double size) {
        return Font.loadFont(Resources.getResourceFileAsStream("fonts/"+name),size);
    }
}
