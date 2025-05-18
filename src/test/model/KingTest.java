package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para a peça Rei (King).
 * Verifica as regras de movimento do rei, que incluem:
 * - Movimento de apenas uma casa em qualquer direção
 * - Não pode se mover duas casas
 * - Pode capturar peças adversárias
 * - Não pode capturar peças aliadas
 * - Movimentos válidos nas bordas do tabuleiro
 */
public class KingTest {
    private Board board_empty;
    private Board board_filled;
    private King whiteKing;

    /**
     * Configuração inicial para os testes:
     * - Cria um tabuleiro vazio e um tabuleiro com peças padrão
     * - Inicializa um rei branco para teste
     */
    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteKing = new King(true);
    }

    /**
     * Testa movimentos válidos de uma casa em todas as direções.
     * Verifica movimentos para baixo, cima, direita, esquerda e diagonal.
     */
    @Test(timeout = 2000)
    public void validKingTestSingleStep() {
        Position from = new Position(3, 3);
        Position toDown = new Position(4, 3);
        Position toUp = new Position(2, 3);
        Position toRight = new Position(3, 4);
        Position toLeft = new Position(3, 2);
        Position toDiagonal = new Position(4, 4);

        assertTrue("Rei deve mover-se 1 casa para baixo", whiteKing.isValidMove(from, toDown, board_empty));
        assertTrue("Rei deve mover-se 1 casa para cima", whiteKing.isValidMove(from, toUp, board_empty));
        assertTrue("Rei deve mover-se 1 casa para direita", whiteKing.isValidMove(from, toRight, board_empty));
        assertTrue("Rei deve mover-se 1 casa para esquerda", whiteKing.isValidMove(from, toLeft, board_empty));
        assertTrue("Rei deve mover-se 1 casa na diagonal", whiteKing.isValidMove(from, toDiagonal, board_empty));
    }

    /**
     * Testa um movimento inválido de duas casas.
     * Verifica se o rei não pode se mover de (3,3) para (5,3).
     */
    @Test(timeout = 2000)
    public void invalidKingTestTwoSteps() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 3);
        assertFalse("Rei não pode mover-se 2 casas", whiteKing.isValidMove(from, to, board_empty));
    }

    /**
     * Testa a captura válida de uma peça adversária.
     * Verifica se o rei pode capturar um peão preto em (4,4).
     */
    @Test(timeout = 2000)
    public void validKingTestCaptureOpponent() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        Piece opponentPawn = new Pawn(false);
        board_filled.movePiece(to.row, to.col, opponentPawn);
        assertTrue("Rei pode capturar peça adversária", whiteKing.isValidMove(from, to, board_filled));
    }

    /**
     * Testa uma tentativa inválida de capturar peça aliada.
     * Verifica se o rei não pode capturar um peão branco em (4,4).
     */
    @Test(timeout = 2000)
    public void invalidKingTestCaptureOwnPiece() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 4);
        Piece ownPawn = new Pawn(true);
        board_filled.movePiece(to.row, to.col, ownPawn);
        assertFalse("Rei não pode capturar peça aliada", whiteKing.isValidMove(from, to, board_filled));
    }

    /**
     * Testa casos extremos de movimento nas bordas do tabuleiro.
     * Verifica movimentos válidos e inválidos a partir do canto (7,7).
     */
    @Test(timeout = 2000)
    public void edgeCaseKingTestCornerMove() {
        Position fromCorner = new Position(7, 7);
        Position toValid = new Position(7, 6);
        Position toInvalid = new Position(5, 7);

        assertTrue("Rei deve mover-se na borda", whiteKing.isValidMove(fromCorner, toValid, board_empty));
        assertFalse("Rei não pode mover-se 2 casas na borda", whiteKing.isValidMove(fromCorner, toInvalid, board_empty));
    }
}
