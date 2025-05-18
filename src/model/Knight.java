package model;

class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    /**
     * Verifica se o movimento do Cavalo é válido.
     * @param from Posição atual
     * @param to Posição desejada
     * @param board Tabuleiro atual
     * @return true se o movimento é válido
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int dx = Math.abs(from.row - to.row);
        int dy = Math.abs(from.col - to.col);

        // Padrão de movimento em "L" (2x1 ou 1x2)
        boolean isLShape = (dx == 2 && dy == 1) || (dx == 1 && dy == 2);

        if (isLShape) {
            Piece destinationPiece = board.getPiece(to.row, to.col);
            // Pode mover se estiver vazio ou for peça adversária
            return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
        }

        return false;
    }
}
