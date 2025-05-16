package model;

public class ChessModel {
private static ChessModel instance;
private Board board;
private boolean whiteTurn = true;
private Position selectedPiecePos = null;

private ChessModel() {
    board = new Board();
}

public static ChessModel getInstance() {
    if (instance == null) {
        instance = new ChessModel();
    }
    return instance;
}

public boolean selecionaPeca(int row, int col) {
    Piece piece = board.getPiece(row, col);
    if (piece != null && piece.isWhite() == whiteTurn) {
        selectedPiecePos = new Position(row, col);
        return true;
    }
    return false;
}

public boolean selecionaCasa(int row, int col) {
    if (selectedPiecePos == null) return false;
    Position target = new Position(row, col);
    Piece piece = board.getPiece(selectedPiecePos.row, selectedPiecePos.col);
    if (piece.isValidMove(selectedPiecePos, target, board)) {
        board.movePiece(selectedPiecePos, target);
        selectedPiecePos = null;
        whiteTurn = !whiteTurn;
        return true;
    }
    return false;
}

public boolean isWhiteTurn() {
    return whiteTurn;
}

}