package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// Testa o comportamento do tabuleiro (Board): inicialização, movimentação e verificação de estado.
public class BoardTest {

    private Board board_empty;
    private Board board_filled;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        board_filled = new Board(false);
    }

    // Testa se o tabuleiro vazio foi corretamente inicializado - Resultado esperado: todas as posições vazias
    @Test(timeout = 2000)
    public void testInitialEmptyBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertTrue("Tabuleiro deve estar vazio", board_empty.isEmpty(row, col));
            }
        }
    }

    // Testa se peças pretas estão posicionadas corretamente no tabuleiro cheio - Resultado esperado: Rook e Pawn pretos nas linhas 0 e 1
    @Test(timeout = 2000)
    public void testBlackPiecesInInitialPosition() {
        assertTrue("Torre preta em (0,0)", board_filled.getPiece(0, 0) instanceof Rook);
        assertTrue("Peão preto em (1,0)", board_filled.getPiece(1, 0) instanceof Pawn);
    }

    // Testa se peças brancas estão posicionadas corretamente no tabuleiro cheio - Resultado esperado: Queen e King brancos nas linhas 7
    @Test(timeout = 2000)
    public void testWhitePiecesInInitialPosition() {
        assertTrue("Rainha branca em (7,3)", board_filled.getPiece(7, 3) instanceof Queen);
        assertTrue("Rei branco em (7,4)", board_filled.getPiece(7, 4) instanceof King);
    }

    // Testa se a movimentação de uma peça atualiza corretamente as posições - Resultado esperado: origem vazia, destino com peça
    @Test(timeout = 2000)
    public void testMovePiece() {
        Position from = new Position(7, 0); // Torre branca
        Position to = new Position(5, 0);
        board_filled.movePiece(from, to);
        assertNull("Posição original deve ficar vazia", board_filled.getPiece(from.row, from.col));
        assertTrue("Peça movida para nova posição", board_filled.getPiece(to.row, to.col) instanceof Rook);
    }

    // Testa o método isEmpty() após a movimentação de uma peça - Resultado esperado: origem vazia, destino ocupada
    @Test(timeout = 2000)
    public void testIsEmptyAfterMove() {
        Position from = new Position(6, 0); // Peão branco
        Position to = new Position(5, 0);
        board_filled.movePiece(from, to);
        assertTrue("Posição (6,0) deve estar vazia", board_filled.isEmpty(6, 0));
        assertFalse("Posição (5,0) não deve estar vazia", board_filled.isEmpty(5, 0));
    }
}
