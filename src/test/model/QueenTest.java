package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueenTest {
    private Board board_empty;
    private Board board_filled;
    private Queen whiteQueen;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteQueen = new Queen(true);
    }

    @Test(timeout = 2000)
    public void validQueenTestStraightMove() {
        Position from = new Position(3, 3);
        Position toVertical = new Position(5, 3); // Vertical
        Position toHorizontal = new Position(3, 5); // Horizontal
        assertTrue("Rainha deve mover-se verticalmente", whiteQueen.isValidMove(from, toVertical, board_empty));
        assertTrue("Rainha deve mover-se horizontalmente", whiteQueen.isValidMove(from, toHorizontal, board_empty));
    }

    @Test(timeout = 2000)
    public void validQueenTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5); // Diagonal
        assertTrue("Rainha deve mover-se na diagonal", whiteQueen.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidQueenTestInvalidPattern() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 5); // Movimento inválido (não reto/diagonal)
        assertFalse("Rainha não pode mover-se em padrão inválido", whiteQueen.isValidMove(from, to, board_empty));
    }
}
