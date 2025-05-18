package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para a peça Rainha (Queen).
 * Verifica as regras de movimento da rainha, que incluem:
 * - Movimento em linhas retas (horizontal/vertical) e diagonais
 * - Não pode pular sobre outras peças
 * - Pode capturar peças adversárias
 * - Não pode capturar peças aliadas
 */
public class QueenTest {
    private Board board_empty;
    private Queen whiteQueen;

    /**
     * Configuração inicial para os testes:
     * - Cria um tabuleiro vazio e um tabuleiro com peças padrão
     * - Inicializa uma rainha branca para teste
     */
    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteQueen = new Queen(true);
    }

    /**
     * Testa movimentos retos válidos para a rainha.
     * Verifica movimentos vertical (3,3)->(5,3) e horizontal (3,3)->(3,5).
     */
    @Test(timeout = 2000)
    public void validQueenTestStraightMove() {
        Position from = new Position(3, 3);
        Position toVertical = new Position(5, 3);
        Position toHorizontal = new Position(3, 5);
        assertTrue("Rainha deve mover-se verticalmente", whiteQueen.isValidMove(from, toVertical, board_empty));
        assertTrue("Rainha deve mover-se horizontalmente", whiteQueen.isValidMove(from, toHorizontal, board_empty));
    }

    /**
     * Testa um movimento diagonal válido para a rainha.
     * Verifica se a rainha pode se mover de (3,3) para (5,5).
     */
    @Test(timeout = 2000)
    public void validQueenTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        assertTrue("Rainha deve mover-se na diagonal", whiteQueen.isValidMove(from, to, board_empty));
    }

    /**
     * Testa um movimento inválido (padrão não reto/diagonal).
     * Verifica se a rainha não pode se mover de (3,3) para (4,5).
     */
    @Test(timeout = 2000)
    public void invalidQueenTestInvalidPattern() {
        Position from = new Position(3, 3);
        Position to = new Position(4, 5);
        assertFalse("Rainha não pode mover-se em padrão inválido", whiteQueen.isValidMove(from, to, board_empty));
    }
}
