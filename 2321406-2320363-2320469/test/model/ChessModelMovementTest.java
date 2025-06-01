package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * Classe de teste para movimentos no modelo de xadrez (ChessModel).
 * Verifica o comportamento do jogo em situações envolvendo cheque, bloqueio e movimentação de peças.
 */
public class ChessModelMovementTest {

    /**
     * Executa antes de cada teste.
     * Reinicializa a instância única do modelo de xadrez para garantir estado limpo.
     */
    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    /**
     * Testa que não é possível mover outra peça enquanto o rei está em cheque.
     * Apenas o próprio rei pode se mover para sair do cheque.
     */
    @Test(timeout = 2000)
    public void cannotMoveOtherPieceWhileKingInCheck() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));      // Rei branco
        board.setPiece(6, 3, new Pawn(true));      // Peão branco
        board.setPiece(0, 4, new Rook(false));     // Torre preta

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei branco deveria estar em cheque", model.isInCheck(true));

        model.selectPiece(6, 3); // Seleciona peão
        assertFalse("Não deveria ser possível mover outra peça enquanto o rei está em cheque", model.selectTargetSquare(5, 3));

        model.selectPiece(7, 4); // Seleciona rei
        assertTrue("Rei deve poder mover-se para sair do cheque", model.selectTargetSquare(7, 3));
    }

    /**
     * Testa que é possível sair do cheque bloqueando o ataque com outra peça.
     * Verifica que o modelo permite movimentos que defendem o rei.
     */
    @Test(timeout = 2000)
    public void canBlockCheckWithAnotherPiece() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));       // Rei branco
        board.setPiece(6, 3, new Bishop(true));     // Bispo branco
        board.setPiece(0, 4, new Rook(false));      // Torre preta

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei branco deveria estar em cheque", model.isInCheck(true));

        model.selectPiece(6, 3); // Seleciona bispo
        assertTrue("Deveria ser possível mover o bispo para bloquear o cheque", model.selectTargetSquare(5, 4));

        assertFalse("Rei branco não deveria mais estar em cheque após bloqueio", model.isInCheck(true));
    }

    /**
     * Testa movimentações que colocam o rei preto em cheque e o removem da ameaça.
     * Também verifica se o cheque é corretamente detectado após nova ameaça.
     */
    @Test(timeout = 2000)
    public void blackKingGoesInAndOutOfCheck() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(0, 4, new King(false));     // Rei preto
        board.setPiece(7, 3, new Queen(true));     // Dama branca

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        model.selectPiece(7, 3); // Seleciona dama branca
        model.selectTargetSquare(6, 4); // Move dama para alinhar com o rei

        assertTrue("Rei preto deveria estar em cheque após movimento da dama", model.isInCheck(false));

        model.selectPiece(0, 4); // Seleciona rei preto
        model.selectTargetSquare(0, 5); // Move rei preto para sair do cheque

        assertFalse("Rei preto não deveria mais estar em cheque após mover-se", model.isInCheck(false));

        model.selectPiece(6, 4); // Seleciona dama branca novamente
        model.selectTargetSquare(5, 5); // Move para nova linha de ataque

        assertTrue("Rei preto deveria estar em cheque novamente após novo movimento da dama", model.isInCheck(false));
    }
    
    
    /**
     * Testa que um cavalo branco no centro do tabuleiro pode se mover para 8 posições diferentes.
     */
    @Test(timeout = 2000)
    public void testKnightMovesWithoutObstacles() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new Knight(true));   // Cavalo branco no centro
        board.setPiece(7, 7, new King(true));     // Rei branco
        board.setPiece(0, 0, new King(false));    // Rei preto

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        List<Position> moves = model.getValidMovesForPiece(new Position(4, 4));

        // Espera-se 8 movimentos possíveis para o cavalo no centro
        assertEquals("Cavalo no centro deve ter 8 movimentos possíveis", 8, moves.size());

        // Verifica algumas posições específicas
        assertTrue(containsPosition(moves, 2, 3));
        assertTrue(containsPosition(moves, 2, 5));
        assertTrue(containsPosition(moves, 3, 2));
        assertTrue(containsPosition(moves, 3, 6));
        assertTrue(containsPosition(moves, 5, 2));
        assertTrue(containsPosition(moves, 5, 6));
        assertTrue(containsPosition(moves, 6, 3));
        assertTrue(containsPosition(moves, 6, 5));
    }


    /**
     * Testa que uma torre não pode se mover se houver uma peça aliada à sua frente.
     */
    @Test(timeout = 2000)
    public void testBlockedPieceHasNoMoves() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new Rook(true));     // Torre branca
        board.setPiece(4, 5, new Pawn(true));     // Peão branco bloqueando à direita
        board.setPiece(7, 7, new King(true));     // Rei branco
        board.setPiece(0, 0, new King(false));    // Rei preto

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        List<Position> moves = model.getValidMovesForPiece(new Position(4, 4));

        // Deve ser menor que 14 (porque está bloqueada à direita)
        assertTrue("Torre com peão aliado ao lado deve ter menos que 14 movimentos", moves.size() < 14);
        assertFalse("Torre não deve poder capturar peça aliada", containsPosition(moves, 4, 5));
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

    
    /**
     * Função utilitária para verificar se uma lista de posições contém uma posição específica.
     */
    private boolean containsPosition(List<Position> positions, int row, int col) {
        for (Position pos : positions) {
            if (pos.row == row && pos.col == col) {
                return true;
            }
        }
        return false;
    }
    
}
