import static org.junit.Assert.*;

import org.junit.Test;

public class CheckModelMateTest {

    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    /**
     * Configura um xeque-mate simples:
     * Rei branco em a1, rei preto em c3, dama preta em b2.
     * O rei branco está em cheque e não pode se mover para nenhuma casa segura.
     */
    @Test
    public void testCheckMatePosition() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(0, 0, new King(true));     // Rei branco em a1
        board.setPiece(2, 2, new King(false));    // Rei preto em c3
        board.setPiece(1, 1, new Queen(false));   // Dama preta em b2

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Deve detectar xeque-mate para o jogador branco
        assertTrue("Deve estar em xeque-mate", model.isCheckMate());
        assertFalse("Não deve estar em afogamento", model.isStalelMate());
    }

    /**
     * Configura um afogamento (stalemate):
     * Rei branco em h1, rei preto em f2, dama preta em g3.
     * O rei branco não está em cheque, mas não pode se mover.
     */
    @Test
    public void testStaleMatePosition() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(7, 7, new King(true));     // Rei branco em h1
        board.setPiece(5, 5, new King(false));    // Rei preto em f2
        board.setPiece(6, 6, new Queen(false));   // Dama preta em g3

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Deve detectar afogamento para o jogador branco
        assertFalse("Não deve estar em xeque-mate", model.isCheckMate());
        assertTrue("Deve estar em afogamento", model.isStalelMate());
    }

    /**
     * Testa uma posição comum onde o jogo ainda pode continuar.
     */
    @Test
    public void testNoMateOrStalemate() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(7, 4, new King(true));    // Rei branco em e1
        board.setPiece(0, 4, new King(false));   // Rei preto em e8
        board.setPiece(6, 4, new Pawn(true));    // Peão branco em e2

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse("Não deve estar em xeque-mate", model.isCheckMate());
        assertFalse("Não deve estar em afogamento", model.isStalelMate());
    }

}
