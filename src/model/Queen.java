package model;

class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(from.row - to.row);
        int colDiff = Math.abs(from.col - to.col);

        int rowStep = Integer.compare(to.row, from.row);
        int colStep = Integer.compare(to.col, from.col);

        if (rowDiff == colDiff || rowDiff == 0 || colDiff == 0) {
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

        return false;
    }

}
