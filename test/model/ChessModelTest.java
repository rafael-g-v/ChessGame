package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessModelTest {

    @BeforeEach
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test
    public void testReiNaoEstaEmChequeInicialmente() {
        ChessModel model = ChessModel.getInstance();
        assertFalse(model.isInCheck(true));  // Rei branco não está em cheque
        assertFalse(model.isInCheck(false)); // Rei preto não está em cheque
    }

    @Test
    public void testReiBrancoEmChequePorTorre() {
        Board board = new Board();

        // Limpa o tabuleiro
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                board.setPiece(row, col, null);

        // Rei branco em e1 (7,4)
        board.setPiece(7, 4, new King(true));

        // Torre preta em e8 (0,4)
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));  // Rei branco está em cheque
        assertFalse(model.isInCheck(false)); // Rei preto não está
    }

    @Test
    public void testReiProtegidoPorPeao() {
        Board board = new Board();

        // Limpa o tabuleiro
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                board.setPiece(row, col, null);

        // Rei branco em e1 (7,4)
        board.setPiece(7, 4, new King(true));
        // Peão branco em e2 (6,4)
        board.setPiece(6, 4, new Pawn(true));
        // Torre preta em e8 (0,4)
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse(model.isInCheck(true));  // Rei branco protegido, não está em cheque
    }
}
