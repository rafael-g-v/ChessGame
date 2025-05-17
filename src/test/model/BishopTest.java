package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BishopTest {
    private Board board_empty;
    private Bishop whiteBishop;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteBishop = new Bishop(true);
    }

    @Test(timeout = 2000)
    public void validBishopTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5); // Movimento diagonal válido
        assertTrue("Bispo deve mover-se na diagonal", whiteBishop.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidBishopTestStraightMove() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 5); // Movimento reto inválido
        assertFalse("Bispo não pode mover-se em linha reta", whiteBishop.isValidMove(from, to, board_empty));
    }
}
