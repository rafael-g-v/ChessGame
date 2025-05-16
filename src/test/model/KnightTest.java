package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KnightTest {

    private Board board_empty;
    private Board board_filled;
    private Knight whiteKnight;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteKnight = new Knight(true);
    }

    @Test(timeout = 2000)
    public void validKnightTestMoveLShape() {
        Position from = new Position(0, 1); 
        Position to = new Position(2, 2);   // movimento em "L" válido

        assertTrue("Cavalo deve se mover em L", whiteKnight.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void invalidKnightTestMoveStraight() {
        Position from = new Position(0, 1);
        Position to = new Position(0, 3);   // movimento reto inválido para cavalo

        assertFalse("Cavalo não pode mover-se em linha reta", whiteKnight.isValidMove(from, to, board_empty));
    }

    @Test(timeout = 2000)
    public void validKnightTestCaptureOpponent() {
        Position from = new Position(0, 1);
        Position to = new Position(2, 2);

        // Caso a posição 2,2 está ocupada por peça adversária
        Piece opponentPawn = new Pawn(false); // peça preta inimiga
        board_filled.movePiece(2, 2, opponentPawn);

        assertTrue("Cavalo pode capturar peça adversária", whiteKnight.isValidMove(from, to, board_filled));
    }

    @Test(timeout = 2000)
    public void invalidKnightTestCaptureOwnPiece() {
        Position from = new Position(0, 1);
        Position to = new Position(2, 2);

        // Caso a posição 2,2 tenha peça aliada
        Piece ownPawn = new Pawn(true); 
        board_filled.movePiece(2, 2, ownPawn);

        assertFalse("Cavalo não pode capturar peça da mesma cor", whiteKnight.isValidMove(from, to, board_filled));
    }
}
