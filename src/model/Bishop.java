package model;

class Bishop extends Piece {
    
    /**
     * Constrói um Bispo.
     * @param isWhite true para peça branca, false para preta
     */
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    /**
     * Verifica se um movimento é válido para o Bispo.
     * @param from Posição inicial
     * @param to Posição final
     * @param board Tabuleiro atual
     * @return true se o movimento é válido
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        // Calcula diferenças nas coordenadas
        int dx = Math.abs(from.row - to.row);
        int dy = Math.abs(from.col - to.col);

        // Bispo só move na diagonal (dx deve ser igual a dy)
        if (dx != dy) return false;

        // Determina direção do movimento (1 ou -1 para cada eixo)
        int rowStep = (to.row > from.row) ? 1 : -1;
        int colStep = (to.col > from.col) ? 1 : -1;

        // Verifica peças no caminho
        int currentRow = from.row + rowStep;
        int currentCol = from.col + colStep;
        
        while (currentRow != to.row) {  
            if (!board.isEmpty(currentRow, currentCol)) {
                return false;  // Caminho bloqueado
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        // Verifica peça no destino
        Piece destinationPiece = board.getPiece(to.row, to.col);
        return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
    }
}
