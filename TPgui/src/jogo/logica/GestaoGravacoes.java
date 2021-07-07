package jogo.logica;

import java.io.*;

public class GestaoGravacoes {
    //Gravações
    public static void escreveParaFicheiro(String nomeFich, Object objeto) {
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(nomeFich));
            out.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Erro na escrita para ficheiro");
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o ficheiro");
                }
            }
        }
    }

    public static void escreveParaFicheiroDAGUI(File fich, Object objeto) {
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(fich));
            out.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Erro na escrita para ficheiro");
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o ficheiro");
                }
            }
        }
    }

    public static Object lerDoFicheiro(String nomeFich) {
        try {
            ObjectInputStream in = new ObjectInputStream((new FileInputStream(nomeFich)));
            return in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao encontrar o ficheiro");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler do ficheiro");
        }
        return null;
    }

    public static Object lerDoFicheiroDAGUI(File fich) {
        try {
            ObjectInputStream in = new ObjectInputStream((new FileInputStream(fich)));
            return in.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao encontrar o ficheiro");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler do ficheiro");
        }
        return null;
    }

}
