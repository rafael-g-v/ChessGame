package main;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        GameView view = new ConsoleView();
        GameController controller = new GameController(board, view);
        controller.startGame();
    }
}

