package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KingTest {
    private Board board_empty;
    private Board board_filled;
    private King whiteKing;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteKing = new King(true);
    }

    @Test(timeout = 2000)
    public void validKingTestSingleStep() {
        Position from = new Position(3, 3);
        Position toDown = new Position(4, 3);    // 1 para baixo
        Position toUp = new Position(2, 3);      // 1 para cima
        Position toRight = new Position(3, 4);   // 1 para direita
        Position toLeft = new Position(3, 2);    // 1 para esquerda
        Position toDiagonal = new Position(4, 4);// 1 na diagonal

        assertTrue("Rei deve mover-se 1 casa para baixo", whiteKing.isValidMove(from, toDown, board_empty));
        assertTrue("Rei deve mover-se 1 casa para cima", whiteKing.isValidMove(from, toUp, board_empty));
        assertTrue("Rei deve mover-se 1 casa para direita", whiteKing.isValidMove(from, toRight, board_empty));
        assertTrue("Rei deve mover-se 1 casa para esquerda", whiteKing.isValidMove(from, toLeft, board_empty));
        assertTrue("Rei deve mover-se 1 casa na diagonal", whiteKing.isValidMove(from, toDiagonal, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidKingTestTwoSteps() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 3); // Movimento inválido de 2 casas
        assertFalse("Rei não pode mover-se 2 casas", whiteKing.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void validKingTestCaptureOpponent() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        // Coloca peça adversária no destino
        Piece opponentPawn = new Pawn(false);
        board_filled.movePiece(to.row, to.col, opponentPawn); // Assume-se que movePiece posiciona a peça
        assertTrue("Rei pode capturar peça adversária", whiteKing.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidKingTestCaptureOwnPiece() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        // Coloca peça aliada no destino
        Piece ownPawn = new Pawn(true);
        board_filled.movePiece(to.row, to.col, ownPawn);
        assertFalse("Rei não pode capturar peça aliada", whiteKing.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void edgeCaseKingTestCornerMove() {
        Position fromCorner = new Position(7, 7);
        Position toValid = new Position(7, 6);   // Movimento válido na borda
        Position toInvalid = new Position(5, 7); // Movimento inválido na borda

        assertTrue("Rei deve mover-se na borda", whiteKing.isValidMove(fromCorner, toValid, board_empty));
        assertFalse("Rei não pode mover-se 2 casas na borda", whiteKing.isValidMove(fromCorner, toInvalid, board_empty));
    }
}
