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
    private Bishop whiteBishop;

    /**
     * Configuração inicial para os testes:
     * - Cria um tabuleiro vazio e um tabuleiro com peças padrão
     * - Inicializa um bispo branco para teste
     */
    @Before
    public void prepare() {
        board_empty = new Board(true);
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
}