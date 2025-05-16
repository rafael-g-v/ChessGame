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

    @Test(timeout = 2000)
    public void validQueenTestCaptureOpponent() {
        Position from = new Position(0, 0);
        Position to = new Position(0, 3);
        // Posição 0,3 ocupada por peça adversária
        Piece opponentRook = new Rook(false);
        board_filled.movePiece(to.row, to.col, opponentRook);
        assertTrue("Rainha pode capturar peça adversária", whiteQueen.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidQueenTestBlockedPath() {
        Position from = new Position(1, 1);
        Position to = new Position(1, 4);
        // Bloqueio no caminho
        Piece blockPawn = new Pawn(true);
        board_filled.movePiece(1, 2, blockPawn);
        assertFalse("Rainha não pode pular sobre peças", whiteQueen.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidQueenTestCaptureOwnPiece() {
        Position from = new Position(2, 2);
        Position to = new Position(2, 5);
        // Peça aliada na posição final
        Piece ownBishop = new Bishop(true);
        board_filled.movePiece(to.row, to.col, ownBishop);
        assertFalse("Rainha não pode capturar peça aliada", whiteQueen.isValidMove(from, to, board_filled));
    }
}
