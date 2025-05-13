package model;

class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int dx = Math.abs(from.row - to.row);
        int dy = Math.abs(from.col - to.col);

        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            Piece destinationPiece = board.getPiece(to.row, to.col);
            return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
        }

        return false;
    }
}
