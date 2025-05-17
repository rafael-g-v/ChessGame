package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RookTest {

    private Board board_empty;
    private Board board_filled;
    private Rook whiteRook;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteRook = new Rook(true);
    }

    @Test(timeout = 2000)
    public void validRookTestMoveStraightLine() {
        Position from = new Position(0, 0); // canto do tabuleiro
        Position to = new Position(0, 5);   // mesma linha, movimento valido

        assertTrue("Torre deve se mover horizontalmente em linha reta", whiteRook.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidRookTestMoveDiagonally() {
        Position from = new Position(0, 0);
        Position to = new Position(3, 3);   // movimento na diagonal, invalido

        assertFalse("Torre não pode se mover na diagonal", whiteRook.isValidMove(from, to, board_empty));
    }
    
    @Test(timeout = 2000)
    public void invalidRookTestObstructedMovement() {
    	Position from = new Position(0, 0); // canto do tabuleiro
        Position to = new Position(0, 5);   // mesma linha, com pecas no caminho, invalido

        assertFalse("Peca na frente da torre", whiteRook.isValidMove(from, to, board_filled));
    }
}
