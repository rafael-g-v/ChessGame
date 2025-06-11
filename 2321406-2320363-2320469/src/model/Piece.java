package model;

abstract class Piece {
    protected boolean isWhite;

    // Indica se a peça já foi movida alguma vez no jogo (importante para roque e en passant)
    protected boolean hasMoved = false;
    
    // Construtor base para todas as peças, define se é branca ou preta
    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    // Retorna true se a peça for branca
    public boolean isWhite() {
        return isWhite;
    }

    // Recebe: posição inicial, posição final e o tabuleiro
    // Retorna: true se o movimento for válido segundo as regras da peça
    public abstract boolean isValidMove(Position from, Position to, Board board);
    
    // Retorna true se a peça já se moveu (usado no roque)
    public boolean hasMoved() {
        return hasMoved;
    }

    // Define o estado de "já se moveu" para a peça
    public void setHasMoved(boolean moved) {
        this.hasMoved = moved;
    }

}
