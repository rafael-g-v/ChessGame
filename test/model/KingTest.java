package model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

// Testa os movimentos válidos e inválidos do rei (King), incluindo limites do tabuleiro.
public class KingTest {

    private Board board_empty;
    private King whiteKing;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteKing = new King(true);
    }

    // Testa movimento válido: 1 casa para baixo
    @Test(timeout = 2000)
    public void testMoveDown() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 3);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido: 1 casa para cima
    @Test(timeout = 2000)
    public void testMoveUp() {
        Position from = new Position(3, 3);
        Position to = new Position(2, 3);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido: 1 casa para direita
    @Test(timeout = 2000)
    public void testMoveRight() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 4);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido: 1 casa para esquerda
    @Test(timeout = 2000)
    public void testMoveLeft() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 2);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido: 1 casa na diagonal
    @Test(timeout = 2000)
    public void testMoveDiagonal() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento inválido: 2 casas em linha reta
    @Test(timeout = 2000)
    public void testMoveTwoStepsInvalid() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 3);
        assertFalse(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa movimento válido na borda do tabuleiro
    @Test(timeout = 2000)
    public void testCornerMoveValid() {
        Position from = new Position(7, 7);
        Position to = new Position(7, 6);
        assertTrue(whiteKing.isValidMove(from, to, board_empty));
    }

    // Testa se criar uma posição com coordenadas inválidas gera IllegalArgumentException
    @Test(expected = IllegalArgumentException.class, timeout = 2000)
    public void testInvalidPositionCoordinates() {
        new Position(5, 8); // coluna 8 é inválida (fora do intervalo 0–7)
    }
}
