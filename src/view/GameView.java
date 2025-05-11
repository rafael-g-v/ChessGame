package view;

public interface GameView {
    void displayBoard(Board board);
    Position askForMove();
    void showMessage(String message);
}
