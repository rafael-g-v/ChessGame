package model;

class King extends Piece {
    /**
     * Constrói um Rei.
     * @param isWhite true para peça branca, false para preta
     */
    public King(boolean isWhite) {
        super(isWhite);
    }

    /**
     * Verifica se o movimento do Rei é válido.
     * @param from Posição atual
     * @param to Posição desejada
     * @param board Tabuleiro atual
     * @return true se o movimento é válido
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(from.row - to.row);
        int colDiff = Math.abs(from.col - to.col);
        
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece destinationPiece = board.getPiece(to.row, to.col);
            // Destino deve estar vazio ou conter peça adversária
            return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
        }
        return false;
    }
}
