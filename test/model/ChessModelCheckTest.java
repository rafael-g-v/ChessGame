package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class ChessModelCheckTest {

    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test(timeout = 2000)
    public void testWhiteKingInCheckFromTower() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(0, 4, new Rook(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
        assertFalse(model.isInCheck(false));
    }

    @Test(timeout = 2000)
    public void testWhiteKingInCheckFromBishop() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 3, new King(true));
        board.setPiece(1, 0, new Bishop(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
        assertFalse(model.isInCheck(false));
    }

    @Test(timeout = 2000)
    public void testWhiteKingInDoubleCheck() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new King(true));
        board.setPiece(0, 4, new Rook(false));
        board.setPiece(1, 7, new Bishop(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
    }

    @Test(timeout = 2000)
    public void testKingProtectedByPawn() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 4, new Pawn(true));
        board.setPiece(0, 4, new Rook(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse(model.isInCheck(true));
    }
}
