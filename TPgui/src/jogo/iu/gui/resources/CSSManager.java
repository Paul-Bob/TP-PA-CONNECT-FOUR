package jogo.iu.gui.resources;

import javafx.scene.Parent;

public class CSSManager {
    private CSSManager() {}

    public static void setCSS(Parent parent, String name) {
        parent.getStylesheets().add(Resources.getResourceFilename("css/"+name));
    }
}
