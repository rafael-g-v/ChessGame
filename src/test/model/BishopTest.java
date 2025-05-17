package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BishopTest {
    private Board board_empty;
    private Board board_filled;
    private Bishop whiteBishop;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
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

    @Test(timeout = 2000)
    public void validBishopTestCaptureOpponent() {
        Position from = new Position(1, 1);
        Position to = new Position(3, 3);
        // Posição 3,3 ocupada por peça adversária
        Piece opponentPawn = new Pawn(false);
        board_filled.movePiece(to.row, to.col, opponentPawn);
        assertTrue("Bispo pode capturar peça adversária", whiteBishop.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidBishopTestBlockedPath() {
        Position from = new Position(0, 0);
        Position to = new Position(2, 2);
        // Bloqueio no caminho
        Piece blockPawn = new Pawn(true);
        board_filled.movePiece(1, 1, blockPawn);
        assertFalse("Bispo não pode pular sobre peças", whiteBishop.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidBishopTestCaptureOwnPiece() {
        Position from = new Position(2, 2);
        Position to = new Position(4, 4);
        // Peça aliada na posição final
        Piece ownPawn = new Pawn(true);
        board_filled.movePiece(to.row, to.col, ownPawn);
        assertFalse("Bispo não pode capturar peça aliada", whiteBishop.isValidMove(from, to, board_filled));
    }
}
