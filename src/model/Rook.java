package model;

class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        if (from.row != to.row && from.col != to.col) return false;

        int rowStep = Integer.compare(to.row, from.row);
        int colStep = Integer.compare(to.col, from.col);

        int currentRow = from.row + rowStep;
        int currentCol = from.col + colStep;

        while (currentRow != to.row || currentCol != to.col) {
            if (!board.isEmpty(currentRow, currentCol)) return false;
            currentRow += rowStep;
            currentCol += colStep;
        }

        Piece destinationPiece = board.getPiece(to.row, to.col);
        return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
    }
}
