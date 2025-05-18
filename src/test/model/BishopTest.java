package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para a peça Bispo (Bishop).
 * Verifica as regras de movimento do bispo, que incluem:
 * - Movimento apenas nas diagonais
 * - Não pode pular sobre outras peças
 * - Pode capturar peças adversárias
 * - Não pode capturar peças aliadas
 */
public class BishopTest {
    private Board board_empty;
    private Board board_filled;
    private Bishop whiteBishop;

    /**
     * Configuração inicial para os testes:
     * - Cria um tabuleiro vazio e um tabuleiro com peças padrão
     * - Inicializa um bispo branco para teste
     */
    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
        whiteBishop = new Bishop(true);
    }

    /**
     * Testa um movimento diagonal válido para o bispo.
     * Verifica se o bispo pode se mover de (3,3) para (5,5).
     */
    @Test(timeout = 2000)
    public void validBishopTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        assertTrue("Bispo deve mover-se na diagonal", whiteBishop.isValidMove(from, to, board_empty));
    }

    /**
     * Testa um movimento reto inválido para o bispo.
     * Verifica se o bispo não pode se mover de (3,3) para (3,5).
     */
    @Test(timeout = 2000)
    public void invalidBishopTestStraightMove() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        assertFalse("Bispo não pode mover-se em linha reta", whiteBishop.isValidMove(from, to, board_empty));
    }

    /**
     * Testa a captura válida de uma peça adversária pelo bispo.
     * Verifica se o bispo pode capturar uma peça preta em (3,3).
     */
    @Test(timeout = 2000)
    public void validBishopTestCaptureOpponent() {
        Position from = new Position(1, 1);
        Position to = new Position(3, 3);
        Piece opponentPawn = new Pawn(false);
        board_filled.movePiece(to.row, to.col, opponentPawn);
        assertTrue("Bispo pode capturar peça adversária", whiteBishop.isValidMove(from, to, board_filled));
    }

    /**
     * Testa um movimento inválido com caminho bloqueado.
     * Verifica se o bispo não pode pular sobre uma peça aliada em (1,1).
     */
    @Test(timeout = 2000)
    public void invalidBishopTestBlockedPath() {
        Position from = new Position(0, 0);
        Position to = new Position(2, 2);
        Piece blockPawn = new Pawn(true);
        board_filled.movePiece(1, 1, blockPawn);
        assertFalse("Bispo não pode pular sobre peças", whiteBishop.isValidMove(from, to, board_filled));
    }

    /**
     * Testa uma tentativa inválida de capturar peça aliada.
     * Verifica se o bispo não pode capturar um peão branco em (4,4).
     */
    @Test(timeout = 2000)
    public void invalidBishopTestCaptureOwnPiece() {
        Position from = new Position(2, 2);
        Position to = new Position(4, 4);
        Piece ownPawn = new Pawn(true);
        board_filled.movePiece(to.row, to.col, ownPawn);
        assertFalse("Bispo não pode capturar peça aliada", whiteBishop.isValidMove(from, to, board_filled));
    }
}
