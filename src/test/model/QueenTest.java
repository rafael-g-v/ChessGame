package model;

/**
 * Classe que representa a peça Rainha (Queen) no jogo de xadrez.
 * 
 * A Rainha é a peça mais poderosa do tabuleiro, combinando os movimentos
 * da Torre (movimentos retos) e do Bispo (movimentos diagonais).
 * 
 * Regras de Movimento:
 * - Pode mover-se qualquer número de casas em linha reta (horizontal/vertical)
 * - Pode mover-se qualquer número de casas nas diagonais
 * - Não pode pular sobre outras peças
 * - Captura peças adversárias ocupando a casa de destino
 * - Não pode capturar peças da mesma cor
 * - Movimento inválido se o destino for uma casa fora dos padrões acima
 */
public class Queen extends Piece {

    /**
     * Construtor da classe Queen.
     * 
     * @param white Define a cor da peça. 
     *        true = peça branca (pertence ao jogador branco)
     *        false = peça preta (pertence ao jogador preto)
     */
    public Queen(boolean white) {
        super(white);
    }

    /**
     * Verifica se um movimento é válido para a Rainha.
     * 
     * @param from Posição de origem no tabuleiro (linha, coluna)
     * @param to   Posição de destino no tabuleiro (linha, coluna)
     * @param board Tabuleiro atual com o estado das peças
     * @return true se o movimento é válido, false caso contrário
     * 
     * Lógica de validação:
     * 1. Movimento deve ser em linha reta (horizontal/vertical) OU diagonal
     * 2. Não pode haver peças bloqueando o caminho (exceto no destino)
     * 3. Se houver peça no destino:
     *    a. Pode capturar se for adversária
     *    b. Não pode capturar se for aliada
     */
    @Override
    public boolean isValidMove(Position from, Position to, Board board) {
        // Movimento na mesma posição é inválido
        if (from.equals(to)) {
            return false;
        }

        // Calcula diferenças nas coordenadas
        int rowDiff = Math.abs(to.row - from.row);
        int colDiff = Math.abs(to.col - from.col);

        // Verifica padrão de movimento (reta ou diagonal)
        boolean isStraight = (rowDiff == 0) || (colDiff == 0); // Movimento reto
        boolean isDiagonal = (rowDiff == colDiff);             // Movimento diagonal

        // Padrão inválido se não for reto nem diagonal
        if (!isStraight && !isDiagonal) {
            return false;
        }

        // Verifica se há peças no caminho (exceto no destino)
        if (hasPiecesInPath(from, to, board)) {
            return false;
        }

        // Verifica a peça no destino (se houver)
        Piece destinationPiece = board.getPiece(to);
        if (destinationPiece != null) {
            // Não pode capturar peças da mesma cor
            if (destinationPiece.isWhite() == this.isWhite()) {
                return false;
            }
            // Captura peça adversária
            return true;
        }

        // Movimento válido para casa vazia
        return true;
    }

    /**
     * Verifica se há peças bloqueando o caminho entre 'from' e 'to'.
     * 
     * @param from Posição de origem
     * @param to   Posição de destino
     * @param board Tabuleiro atual
     * @return true se houver peças no caminho, false caso contrário
     * 
     * Observações:
     * - Não verifica a posição final (to)
     * - Assume que o movimento já foi validado como reto/diagonal
     */
    private boolean hasPiecesInPath(Position from, Position to, Board board) {
        int rowStep = Integer.compare(to.row, from.row); // Direção vertical (-1, 0, 1)
        int colStep = Integer.compare(to.col, from.col); // Direção horizontal (-1, 0, 1)

        int currentRow = from.row + rowStep;
        int currentCol = from.col + colStep;

        // Percorre o caminho até chegar logo antes do destino
        while (currentRow != to.row || currentCol != to.col) {
            if (board.getPiece(new Position(currentRow, currentCol)) != null) {
                return true; // Peça encontrada no caminho
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return false; // Caminho livre
    }

    /**
     * Representação textual da Rainha.
     * 
     * @return "Q" (Rainha branca) ou "q" (Rainha preta)
     */
    @Override
    public String toString() {
        return isWhite() ? "Q" : "q";
    }
}
