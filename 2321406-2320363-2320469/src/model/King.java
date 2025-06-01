package model;

class King extends Piece {

    // Constrói um rei branco ou preto
    public King(boolean isWhite) {
        super(isWhite);
    }

    // Recebe: posição inicial, posição final e o tabuleiro
    // Retorna: true se o movimento for válido (1 casa em qualquer direção, sem capturar peça aliada)
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        int rowDiff = Math.abs(from.row - to.row);
        int colDiff = Math.abs(from.col - to.col);

        // Movimento normal do rei (1 casa em qualquer direção)
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece destinationPiece = board.getPiece(to.row, to.col);
            return destinationPiece == null || destinationPiece.isWhite() != this.isWhite;
        }

        // Roque: movimento especial do rei (2 casas na horizontal)
        if (!this.hasMoved && from.row == to.row && (to.col == 6 || to.col == 2)) {
            int rookCol = (to.col == 6) ? 7 : 0;
            Piece rookPiece = board.getPiece(from.row, rookCol);

            // Verifica se há uma torre compatível na extremidade e que ainda não se moveu
            if (rookPiece instanceof Rook && rookPiece.isWhite() == this.isWhite && !rookPiece.hasMoved()) {
                int direction = (to.col == 6) ? 1 : -1;
                int currentCol = from.col + direction;

                // Verifica se o caminho entre rei e torre está livre
                while (currentCol != rookCol) {
                    if (!board.isEmpty(from.row, currentCol)) return false;
                    currentCol += direction;
                }

                return true; // Verificação adicional de cheque ocorre fora
            }
        }

        return false;
    }

}
