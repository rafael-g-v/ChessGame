package model;

class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    /**
     * Verifica se o movimento do Peão é válido.
     * @param from Posição atual
     * @param to Posição desejada
     * @param board Tabuleiro atual
     * @return true se o movimento é válido
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        // Direção do movimento (brancos sobem, pretos descem)
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        // Movimento para frente
        if (from.col == to.col) {
            if (to.row == from.row + direction && board.isEmpty(to.row, to.col)) {
                return true;
            }
            // Primeiro movimento (2 casas)
            if (from.row == startRow && to.row == from.row + 2 * direction 
                && board.isEmpty(to.row, to.col) 
                && board.isEmpty(from.row + direction, to.col)) {
                return true;
            }
        }
        
        // Captura diagonal
        if (Math.abs(from.col - to.col) == 1 && to.row == from.row + direction) {
            Piece target = board.getPiece(to.row, to.col);
            // Só pode capturar peça adversária
            return target != null && target.isWhite() != isWhite;
        }

        return false;
    }
}
