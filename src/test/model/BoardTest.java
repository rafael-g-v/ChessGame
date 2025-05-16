package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board board_empty;
    private Board board_filled;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
    }

    @Test(timeout = 2000)
    public void testInitialEmptyBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertTrue("Tabuleiro deve estar vazio", board_empty.isEmpty(row, col));
            }
        }
    }

    @Test(timeout = 2000)
    public void testInitialFilledBoard() {
        // Verifica peças pretas na linha 0 e 1
        assertTrue("Torre preta em (0,0)", board_filled.getPiece(0, 0) instanceof Rook);
        assertTrue("Peão preto em (1,0)", board_filled.getPiece(1, 0) instanceof Pawn);

        // Verifica peças brancas na linha 6 e 7
        assertTrue("Rainha branca em (7,3)", board_filled.getPiece(7, 3) instanceof Queen);
        assertTrue("Rei branco em (7,4)", board_filled.getPiece(7, 4) instanceof King);
    }

    @Test(timeout = 2000)
    public void testMovePiece() {
        Position from = new Position(7, 0); // Torre branca
        Position to = new Position(5, 0);
        board_filled.movePiece(from, to);
        assertNull("Posição original deve ficar vazia", board_filled.getPiece(from.row, from.col));
        assertTrue("Peça movida para nova posição", board_filled.getPiece(to.row, to.col) instanceof Rook);
    }

    @Test(timeout = 2000)
    public void testIsEmptyAfterMove() {
        Position from = new Position(6, 0); // Peão branco
        Position to = new Position(5, 0);
        board_filled.movePiece(from, to);
        assertTrue("Posição (6,0) deve estar vazia", board_filled.isEmpty(6, 0));
        assertFalse("Posição (5,0) não deve estar vazia", board_filled.isEmpty(5, 0));
    }
}
