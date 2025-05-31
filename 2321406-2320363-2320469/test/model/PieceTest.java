package model;

import org.junit.Test;
import static org.junit.Assert.*;

//Testa a identificação da cor (branco/preto) de uma peça.
public class PieceTest {

    // Testa se uma peça branca retorna true para isWhite()
    @Test(timeout = 2000)
    public void testWhitePieceIsWhite() {
        Piece whitePawn = new Pawn(true);
        assertTrue("Peça branca deve retornar isWhite() = true", whitePawn.isWhite());
    }

    // Testa se uma peça preta retorna false para isWhite()
    @Test(timeout = 2000)
    public void testBlackPieceIsNotWhite() {
        Piece blackBishop = new Bishop(false);
        assertFalse("Peça preta deve retornar isWhite() = false", blackBishop.isWhite());
    }
}
