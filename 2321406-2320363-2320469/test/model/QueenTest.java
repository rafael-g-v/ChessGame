package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Testa os movimentos válidos e inválidos da rainha (Queen) nas direções permitidas.
public class QueenTest {

    private Board board_empty;
    private Queen whiteQueen;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteQueen = new Queen(true);
    }

    // Testa movimento válido vertical da rainha - Resultado esperado: true
    @Test(timeout = 2000)
    public void testQueenMoveVertical() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 3);
        assertTrue(whiteQueen.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido horizontal da rainha - Resultado esperado: true
    @Test(timeout = 2000)
    public void testQueenMoveHorizontal() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        assertTrue(whiteQueen.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido na diagonal da rainha - Resultado esperado: true
    @Test(timeout = 2000)
    public void testQueenMoveDiagonal() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        assertTrue(whiteQueen.isValidMove(from, to, board_empty));
    }

    // Testa movimento inválido da rainha (não reto nem diagonal) - Resultado esperado: false
    @Test(timeout = 2000)
    public void testQueenInvalidPattern() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 5);
        assertFalse(whiteQueen.isValidMove(from, to, board_empty));
    }
}
