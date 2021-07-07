package jogo.iu.gui.resources;

import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageLoader {
    static HashMap<String, Image> imgCache;
    static {
        imgCache = new HashMap<>();
    }
    public static Image getImage(String name) {
        Image img = imgCache.get(name);
        if (img != null)
            return img;
        try{
            img = new Image(Resources.getResourceFileAsStream("images/" + name));
            imgCache.put(name,img);
            return img;
        } catch (Exception e){
            System.err.println("Erro a ler imagem man!");
        }
        return null;
    }
    /*
caso nova versão de imagem(nao vou usar acho eu)
    public static Image getImageFOrce(String name) {
        imgCache.remove(name);
        return getImage(name);
    }
*/
}