package model;

class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(from.row - to.row);
        int colDiff = Math.abs(from.col - to.col);
        
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece destinationPiece = board.getPiece(to.row, to.col);
            return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
        }
        return false;
    }
    
    // Adicionar ROQUE
}
