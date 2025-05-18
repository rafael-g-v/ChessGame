package model;

class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    /**
     * @param from Posição atual (linha, coluna)
     * @param to Posição desejada (linha, coluna)
     * @param board Tabuleiro atual
     * @return true se o movimento é válido
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        // Movimento deve ser horizontal OU vertical
        if (from.row != to.row && from.col != to.col) {
            return false;
        }

        // Determina direção do movimento (1, 0 ou -1 para cada eixo)
        int rowStep = Integer.compare(to.row, from.row);
        int colStep = Integer.compare(to.col, from.col);

        // Verifica peças no caminho
        int currentRow = from.row + rowStep;
        int currentCol = from.col + colStep;

        while (currentRow != to.row || currentCol != to.col) {
            if (!board.isEmpty(currentRow, currentCol)) {
                return false; // Caminho bloqueado
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        // Verifica peça no destino
        Piece destinationPiece = board.getPiece(to.row, to.col);
        return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
    }
}
