package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para verificar a geração e carregamento de FEN no modelo de xadrez.
 */
public class ChessModelFENTest {

    /**
     * Executa antes de cada teste.
     * Reinicializa a instância única do modelo de xadrez para garantir estado limpo.
     */
    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    /**
     * Testa o carregamento e geração de FEN a partir de uma configuração personalizada.
     */
    @Test(timeout = 2000)
    public void testLoadAndGenerateFEN() {
        // FEN de exemplo: tabuleiro parcialmente cheio, rei branco e preto, e uma torre
        String fen = "8/8/8/8/8/8/8/R3K2r w - - 0 1";

        ChessModel model = ChessModel.getInstance();
        model.loadFEN(fen);

        Board board = model.getBoard();

        // Verifica as peças nas posições esperadas
        assertTrue("Deveria haver uma torre branca em a1", board.getPiece(7, 0) instanceof Rook);
        assertTrue("Deveria haver um rei branco em e1", board.getPiece(7, 4) instanceof King);
        assertTrue("Deveria haver uma torre preta em h1", board.getPiece(7, 7) instanceof Rook);
        assertTrue("Peça em h1 deve ser preta", !board.getPiece(7, 7).isWhite());

        // Verifica o turno
        assertTrue("Deveria ser o turno das brancas", model.isWhiteTurn());

        // Verifica se a FEN gerada bate com a original
        String generatedFEN = model.generateFEN();
        assertEquals("FEN gerada deveria ser igual à original (exceto por campos ignorados)", fen, generatedFEN);
    }
}
