package model;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        setupPieces();
    }

    private void setupPieces() {
        // inicializa o tabuleiro com todas as peças
    }

    public Piece getPieceAt(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }

    public void movePiece(Position from, Position to) {
        // lógica para mover peças
    }
}
