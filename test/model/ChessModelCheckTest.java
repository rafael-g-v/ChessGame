package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessModelCheckTest {

    @BeforeEach
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test
    public void testReiBrancoEmChequePorTorre() {
        Board board = new Board();
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(0, 4, new Rook(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
        assertFalse(model.isInCheck(false));
    }

    @Test
    public void testReiBrancoEmChequePorBispo() {
        Board board = new Board();
        board.clear();
        board.setPiece(4, 3, new King(true));
        board.setPiece(1, 0, new Bishop(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
        assertFalse(model.isInCheck(false));
    }

    @Test
    public void testReiBrancoEmChequeDuplo() {
        Board board = new Board();
        board.clear();
        board.setPiece(4, 4, new King(true));
        board.setPiece(0, 4, new Rook(false));
        board.setPiece(1, 7, new Bishop(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
    }

    @Test
    public void testReiProtegidoPorPeao() {
        Board board = new Board();
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 4, new Pawn(true));
        board.setPiece(0, 4, new Rook(false));
        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse(model.isInCheck(true));
    }
}
