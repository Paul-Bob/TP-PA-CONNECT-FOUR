package jogo.logica.memento;

import java.io.IOException;

public interface IMementoOriginator {
    Memento getMemento() throws IOException;
    void setMemento(Memento m, int vezes) throws IOException, ClassNotFoundException;
}
