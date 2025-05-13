package model;

public abstract class Piece {
    protected String color;
    protected Position position;

    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }

    public abstract List<Position> getPossibleMoves(Board board);

    public String getColor() { return color; }
    public Position getPosition() { return position; }
    public void setPosition(Position newPosition) { this.position = newPosition; }
}

