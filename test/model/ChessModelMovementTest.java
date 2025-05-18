package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

        model.selecionaPeca(6, 3); // Seleciona peão
        assertFalse("Não deveria ser possível mover outra peça enquanto o rei está em cheque", model.selecionaCasa(5, 3));

        model.selecionaPeca(7, 4); // Seleciona rei
        assertTrue("Rei deve poder mover-se para sair do cheque", model.selecionaCasa(7, 3));
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

        model.selecionaPeca(6, 3); // Seleciona bispo
        assertTrue("Deveria ser possível mover o bispo para bloquear o cheque", model.selecionaCasa(5, 4));

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

        model.selecionaPeca(7, 3); // Seleciona dama branca
        model.selecionaCasa(6, 4); // Move dama para alinhar com o rei

        assertTrue("Rei preto deveria estar em cheque após movimento da dama", model.isInCheck(false));

        model.selecionaPeca(0, 4); // Seleciona rei preto
        model.selecionaCasa(0, 5); // Move rei preto para sair do cheque

        assertFalse("Rei preto não deveria mais estar em cheque após mover-se", model.isInCheck(false));

        model.selecionaPeca(6, 4); // Seleciona dama branca novamente
        model.selecionaCasa(5, 5); // Move para nova linha de ataque

        assertTrue("Rei preto deveria estar em cheque novamente após novo movimento da dama", model.isInCheck(false));
    }
}
