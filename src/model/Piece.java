package model;

abstract class Piece {
    protected boolean isWhite;

    /**
     * Construtor base para todas as peças.
     * @param isWhite true para peça branca, false para preta
     */
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }
    
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * @param from Posição de origem
     * @param to Posição de destino
     * @param board Tabuleiro atual
     * @return true se o movimento é válido para a peça
     */
    public abstract boolean isValidMove(Position from, Position to, Board board);
}
