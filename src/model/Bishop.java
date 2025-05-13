package model;

class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int dx = Math.abs(from.row - to.row);
        int dy = Math.abs(from.col - to.col);

        if (dx != dy) return false;

        int rowStep = (to.row - from.row) > 0 ? 1 : -1;
        int colStep = (to.col - from.col) > 0 ? 1 : -1;

        int currentRow = from.row + rowStep;
        int currentCol = from.col + colStep;

        while (currentRow != to.row && currentCol != to.col) {
            if (!board.isEmpty(currentRow, currentCol)) return false;
            currentRow += rowStep;
            currentCol += colStep;
        }

        Piece destinationPiece = board.getPiece(to.row, to.col);
        return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
    }
}
