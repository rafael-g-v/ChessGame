package controller;

public class GameController {
    private Board board;
    private GameView view;
    private String currentPlayer;

    public GameController(Board board, GameView view) {
        this.board = board;
        this.view = view;
        this.currentPlayer = "white";
    }

    public void startGame() {
        while (!isGameOver()) {
            view.displayBoard(board);
            Position from = view.askForMove();
            Position to = view.askForMove();
            
            if (tryMove(from, to)) {
                switchPlayer();
            } else {
                view.showMessage("Movimento inválido. Tente novamente.");
            }
        }

        view.showMessage("Fim de jogo!");
    }

    private boolean tryMove(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece != null && piece.getColor().equals(currentPlayer)) {
            List<Position> moves = piece.getPossibleMoves(board);
            if (moves.contains(to)) {
                board.movePiece(from, to);
                return true;
            }
        }
        return false;
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
    }

    private boolean isGameOver() {
        // lógica para verificar xeque-mate ou empate
        return false;
    }
}
