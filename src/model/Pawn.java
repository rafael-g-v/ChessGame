package model;

class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        if (from.col == to.col) {
            if (to.row == from.row + direction && board.isEmpty(to.row, to.col)) {
                return true;
            }
            if (from.row == startRow && to.row == from.row + 2 * direction && board.isEmpty(to.row, to.col)) {
                return true;
            }
        }
        
        // Ataque diagonal
        if (Math.abs(from.col - to.col) == 1 && to.row == from.row + direction) {
            Piece target = board.getPiece(to.row, to.col);
            return target != null && target.isWhite() != isWhite;
        }

        return false;
    }
}
