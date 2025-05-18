package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para verificar situações de cheque (check) no modelo de xadrez (ChessModel).
 * Os testes cobrem diferentes tipos de ataque ao rei, incluindo torre, bispo, e múltiplos atacantes.
 */
public class ChessModelCheckTest {

    /**
     * Executa antes de cada teste.
     * Reinicializa a instância única do modelo de xadrez para garantir estado limpo.
     */
    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    /**
     * Testa se o rei branco está em cheque quando uma torre preta está alinhada na mesma coluna.
     */
    @Test(timeout = 2000)
    public void whiteKingInCheckFromRook() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));       // Rei branco em e1
        board.setPiece(0, 4, new Rook(false));      // Torre preta em e8

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei branco deve estar em cheque pela torre preta", model.isInCheck(true));
        assertFalse("Rei preto não deve estar em cheque", model.isInCheck(false));
    }

    /**
     * Testa se o rei branco está em cheque quando um bispo preto está posicionado em uma diagonal de ataque.
     */
    @Test(timeout = 2000)
    public void whiteKingInCheckFromBishop() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 3, new King(true));        // Rei branco em d4
        board.setPiece(1, 0, new Bishop(false));     // Bispo preto em a7

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei branco deve estar em cheque pelo bispo preto", model.isInCheck(true));
        assertFalse("Rei preto não deve estar em cheque", model.isInCheck(false));
    }

    /**
     * Testa se o rei branco está em cheque duplo, ou seja, sendo atacado simultaneamente por dois adversários.
     */
    @Test(timeout = 2000)
    public void whiteKingInDoubleCheck() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new King(true));        // Rei branco em e4
        board.setPiece(0, 4, new Rook(false));       // Torre preta em e8
        board.setPiece(1, 7, new Bishop(false));     // Bispo preto em h7

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei branco deve estar em cheque duplo (torre e bispo)", model.isInCheck(true));
    }

    /**
     * Testa se o rei branco não está em cheque quando um peão aliado bloqueia a linha de ataque da torre adversária.
     */
    @Test(timeout = 2000)
    public void whiteKingProtectedByPawn() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));        // Rei branco em e1
        board.setPiece(6, 4, new Pawn(true));        // Peão branco em e2 (protege o rei)
        board.setPiece(0, 4, new Rook(false));       // Torre preta em e8

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse("Rei branco não deve estar em cheque porque o peão branco bloqueia a torre", model.isInCheck(true));
    }
}
