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

        board.clear();  // limpa o tabuleiro

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
    public void testReiBrancoEmChequePorBispo() {
        Board board = new Board();
        board.clear();  // limpa o tabuleiro

        // Rei branco em d4 (4,3)
        board.setPiece(4, 3, new King(true));

        // Bispo preto em a7 (1,0) que ataca d4 diagonalmente
        board.setPiece(1, 0, new Bishop(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
        assertFalse(model.isInCheck(false));
    }

    @Test
    public void testReiBrancoEmChequeDuplo() {
        Board board = new Board();
        board.clear();  // limpa o tabuleiro

        // Rei branco em e4 (4,4)
        board.setPiece(4, 4, new King(true));

        // Torre preta em e8 (0,4)
        board.setPiece(0, 4, new Rook(false));
        // Bispo preto em h7 (1,7), ataca diagonal para e4
        board.setPiece(1, 7, new Bishop(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));
    }

    @Test
    public void testReiProtegidoPorPeao() {
        Board board = new Board();
        board.clear();  // limpa o tabuleiro

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

    @Test
    public void testNaoPodeMoverOutraPecaSeReiEmCheque() {
        Board board = new Board();
        board.clear();  // limpa o tabuleiro

        // Rei branco em e1
        board.setPiece(7, 4, new King(true));
        
        // Peão branco em d2
        board.setPiece(6, 3, new Pawn(true));
        
        // Torre preta em e8 ameaçando rei pela coluna
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Rei branco está em cheque
        assertTrue(model.isInCheck(true));

        // Tentativa de mover peão branco que não tira cheque
        model.selecionaPeca(6, 3);
        assertFalse(model.selecionaCasa(5, 3)); // tentativa de mover para d3 

        // Pode mover o rei para fora do cheque
        model.selecionaPeca(7, 4);
        assertTrue(model.selecionaCasa(7, 3));
    }

    @Test
    public void testPodeBloquearChequeMovendoOutraPeca() {
        Board board = new Board();
        board.clear();  // limpa o tabuleiro

        // Rei branco em e1 (7,4)
        board.setPiece(7, 4, new King(true));
        // Bispo branco em d2 (6,3) - para mover e bloquear o cheque
        board.setPiece(6, 3, new Bishop(true));
        // Torre preta em e8 (0,4)
        board.setPiece(0, 4, new Rook(false));

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue(model.isInCheck(true));

        model.selecionaPeca(6, 3);
        assertTrue(model.selecionaCasa(5, 4));  // mover bispo para e3 para bloquear cheque

        assertFalse(model.isInCheck(true)); // agora o cheque está bloqueado
    }
    
}
