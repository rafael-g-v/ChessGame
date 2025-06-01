package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class ChessModelMovementTest {

    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test(timeout = 2000)
    public void cannotMoveOtherPieceWhileKingInCheck() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 3, new Pawn(true));
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));

        model.selectPiece(6, 3);
        assertFalse(model.selectTargetSquare(5, 3));

        model.selectPiece(7, 4);
        assertTrue(model.selectTargetSquare(7, 3));
    }

    @Test(timeout = 2000)
    public void canBlockCheckWithAnotherPiece() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(7, 4, new King(true));
        board.setPiece(6, 3, new Bishop(true));
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));

        model.selectPiece(6, 3);
        assertTrue(model.selectTargetSquare(5, 4));

        assertFalse(model.isInCheck(true));
    }

    @Test(timeout = 2000)
    public void blackKingGoesInAndOutOfCheck() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(0, 4, new King(false));
        board.setPiece(7, 3, new Queen(true));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        model.selectPiece(7, 3);
        model.selectTargetSquare(6, 4);

        assertTrue(model.isInCheck(false));

        model.selectPiece(0, 4);
        assertTrue(model.selectTargetSquare(0, 5));

        assertFalse(model.isInCheck(false));

        model.selectPiece(6, 4);
        model.selectTargetSquare(5, 5);

        assertTrue(model.isInCheck(false));
    }

    @Test(timeout = 2000)
    public void testKnightMovesWithoutObstacles() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new Knight(true));
        board.setPiece(7, 7, new King(true));
        board.setPiece(0, 0, new King(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        List<int[]> moves = model.getValidMovesForPiece(4, 4);

        assertEquals(8, moves.size());

        assertTrue(containsPosition(moves, 2, 3));
        assertTrue(containsPosition(moves, 2, 5));
        assertTrue(containsPosition(moves, 3, 2));
        assertTrue(containsPosition(moves, 3, 6));
        assertTrue(containsPosition(moves, 5, 2));
        assertTrue(containsPosition(moves, 5, 6));
        assertTrue(containsPosition(moves, 6, 3));
        assertTrue(containsPosition(moves, 6, 5));
    }

    @Test(timeout = 2000)
    public void testBlockedPieceHasNoMoves() {
        Board board = new Board(true);
        board.clear();
        board.setPiece(4, 4, new Rook(true));
        board.setPiece(4, 5, new Pawn(true));
        board.setPiece(7, 7, new King(true));
        board.setPiece(0, 0, new King(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        List<int[]> moves = model.getValidMovesForPiece(4, 4);

        assertTrue(moves.size() < 14);
        assertFalse(containsPosition(moves, 4, 5));
    }

    @Test(timeout = 2000)
    public void pawnPromotionToKnight() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(1, 0, new Pawn(true));
        board.setPiece(7, 4, new King(true));
        board.setPiece(0, 4, new King(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.selectPiece(1, 0));
        assertTrue(model.selectTargetSquare(0, 0));

        assertTrue(model.hasPendingPromotion());

        assertTrue(model.promotePawn("knight"));

        String promotedCode = model.getPieceCode(0, 0);
        assertEquals("wn", promotedCode);
    }

    private boolean containsPosition(List<int[]> positions, int row, int col) {
        for (int[] pos : positions) {
            if (pos[0] == row && pos[1] == col) {
                return true;
            }
        }
        return false;
    }
}