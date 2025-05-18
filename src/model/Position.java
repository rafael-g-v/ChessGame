package model;

class Position {
    public final int row;
    public final int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @param obj Objeto a ser comparado
     * @return true se as posições têm as mesmas coordenadas
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }
}
