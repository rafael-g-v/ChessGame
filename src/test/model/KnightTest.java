package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KnightTest {

    private Board board_empty;
    private Knight whiteKnight;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteKnight = new Knight(true);
    }

    @Test(timeout = 2000)
    public void validKnightTestMoveLShape() {
        Position from = new Position(0, 1); 
        Position to = new Position(2, 2);   // movimento em "L" válido

        assertTrue("Cavalo deve se mover em L", whiteKnight.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidKnightTestMoveStraight() {
        Position from = new Position(0, 1);
        Position to = new Position(0, 3);   // movimento reto inválido para cavalo

        assertFalse("Cavalo não pode mover-se em linha reta", whiteKnight.isValidMove(from, to, board_empty));
    }
}
