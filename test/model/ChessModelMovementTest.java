package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ChessModelMovementTest {

    @BeforeClass
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test(timeout = 2000)
    public void testNaoPodeMoverOutraPecaSeReiEmCheque() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 3, new Pawn(true));
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));

        model.selecionaPeca(6, 3);
        assertFalse(model.selecionaCasa(5, 3));

        model.selecionaPeca(7, 4);
        assertTrue(model.selecionaCasa(7, 3));
    }

    @Test(timeout = 2000)
    public void testPodeBloquearChequeMovendoOutraPeca() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 3, new Bishop(true));
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));

        model.selecionaPeca(6, 3);
        assertTrue(model.selecionaCasa(5, 4));

        assertFalse(model.isInCheck(true));
    }
    
    @Test(timeout = 2000)
    public void testMovimentoColocaReiPretoEmCheque() {
        Board board = new Board(true);
        board.clear();  // limpar o tabuleiro

        // Colocar rei preto em e8 (0,4)
        board.setPiece(0, 4, new King(false));
        // Colocar dama branca em d1 (7,3)
        board.setPiece(7, 3, new Queen(true));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Seleciona a dama branca e move para e2 (6,4), alinhada com o rei preto
        model.selecionaPeca(7, 3);
        model.selecionaCasa(6, 4);

        // Verifica que o rei preto está em cheque
        assertTrue(model.isInCheck(false));
        
        // Seleciona a rei preto e move para f8 (0,5)
        model.selecionaPeca(0, 4);
        model.selecionaCasa(0, 5);

        // Verifica que o rei preto saiu do cheque
        assertFalse(model.isInCheck(false));
        
        // Seleciona a dama branca e move para f3 (5,5), alinhada com o rei preto
        model.selecionaPeca(6, 4);
        model.selecionaCasa(5, 5);

        // Verifica que o rei preto está em cheque
        assertTrue(model.isInCheck(false));
    }

}
