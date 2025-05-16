package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class PieceTest {
    @Test(timeout = 2000)
    public void testPieceColor() {
        Piece whitePawn = new Pawn(true);
        Piece blackBishop = new Bishop(false);
        assertTrue("Peça branca deve retornar isWhite() = true", whitePawn.isWhite());
        assertFalse("Peça preta deve retornar isWhite() = false", blackBishop.isWhite());
    }
}
