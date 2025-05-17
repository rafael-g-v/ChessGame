package model;

class Position {

    public final int row;
    public final int col;

    public Position(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Coordenadas inválidas: (" + row + ", " + col + ")");
        }
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }
}
