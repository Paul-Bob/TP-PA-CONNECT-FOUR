package jogo.logica.memento;

public interface ICareTaker {
    void saveMemento();
    boolean undo(int vezes);
}
