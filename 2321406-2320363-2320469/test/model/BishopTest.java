package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

//Testa movimentos válidos na diagonal e inválidos em linha reta do bispo (Bishop).
public class BishopTest {

    private Board board_empty;
    private Bishop whiteBishop;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteBishop = new Bishop(true);
    }

    // Testa se o bispo pode se mover na diagonal (movimento válido) - Resultado esperado: true
    @Test(timeout = 2000)
    public void validBishopTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        assertTrue("Bispo deve mover-se na diagonal", whiteBishop.isValidMove(from, to, board_empty));
    }

    // Testa se o bispo tenta se mover em linha reta, o que é inválido - Resultado esperado: false
    @Test(timeout = 2000)
    public void invalidBishopTestStraightMove() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        assertFalse("Bispo não pode mover-se em linha reta", whiteBishop.isValidMove(from, to, board_empty));
    }
}
