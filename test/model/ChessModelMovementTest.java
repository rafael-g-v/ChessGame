package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessModelMovementTest {

    @BeforeEach
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test
    public void testNaoPodeMoverOutraPecaSeReiEmCheque() {
        Board board = new Board();
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

    @Test
    public void testPodeBloquearChequeMovendoOutraPeca() {
        Board board = new Board();
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
}
