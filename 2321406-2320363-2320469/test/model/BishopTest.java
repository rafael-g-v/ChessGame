package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

//Testa movimentos válidos na diagonal e inválidos em linha reta do bispo (Bishop).
public class BishopTest {

    private Board board_empty;
    private Bishop whiteBishop;

    @Before
    public void prepare() {
        board_empty = new Board(true);
        whiteBishop = new Bishop(true);
    }

    // Testa se o bispo pode se mover na diagonal (movimento válido) - Resultado esperado: true
    @Test(timeout = 2000)
    public void validBishopTestDiagonalMove() {
        Position from = new Position(3, 3);
        Position to = new Position(5, 5);
        assertTrue("Bispo deve mover-se na diagonal", whiteBishop.isValidMove(from, to, board_empty));
    }

    // Testa se o bispo tenta se mover em linha reta, o que é inválido - Resultado esperado: false
    @Test(timeout = 2000)
    public void invalidBishopTestStraightMove() {
        Position from = new Position(3, 3);
        Position to = new Position(3, 5);
        assertFalse("Bispo não pode mover-se em linha reta", whiteBishop.isValidMove(from, to, board_empty));
    }
    
    /**
     * Testa a promoção de um peão branco ao atingir a última linha.
     * Verifica que a promoção pendente é detectada e concluída corretamente com a peça escolhida.
     */
    @Test(timeout = 2000)
    public void pawnPromotionToKnight() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(1, 0, new Pawn(true));   // Peão branco em a7
        board.setPiece(7, 4, new King(true));   // Rei branco (para evitar erros de ausência de rei)
        board.setPiece(0, 4, new King(false));  // Rei preto

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Move peão para a8
        assertTrue("Peão branco deve ser selecionável", model.selectPiece(1, 0));
        assertTrue("Movimento do peão até a última linha deve ser válido", model.selectTargetSquare(0, 0));

        assertTrue("Deveria haver promoção pendente após o peão alcançar a última linha", model.hasPendingPromotion());

        // Promove o peão para cavalo
        assertTrue("Promoção para cavalo deveria ser possível", model.promotePawn("knight"));

        String promotedCode = model.getPieceCode(0, 0);
        assertEquals("wn", promotedCode);  // w = white, n = knight
    }
}
