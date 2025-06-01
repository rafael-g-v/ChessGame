package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste para verificar os movimentos especiais de roque e en passant no modelo de xadrez.
 * Os testes incluem tanto cenários válidos quanto inválidos para esses movimentos.
 */
public class ChessModelSpecialMovesTest {

    /**
     * Executa antes de cada teste.
     * Reinicializa a instância única do modelo de xadrez para garantir estado limpo.
     */
    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    // Testa roque pequeno válido para as brancas (rei de e1 para g1)
    @Test(timeout = 2000)
    public void whiteKingsideCastlingValid() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(7, 4, new King(true));  // Rei branco em e1
        board.setPiece(7, 7, new Rook(true));  // Torre branca em h1

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Roque pequeno deve ser válido", model.selectPiece(7, 4));
        assertTrue("Rei deve poder rocar para g1", model.selectTargetSquare(7, 6));
    }

    // Testa roque inválido quando há uma peça entre rei e torre
    @Test(timeout = 2000)
    public void castlingBlockedByPiece() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(7, 4, new King(true));  // Rei branco em e1
        board.setPiece(7, 7, new Rook(true));  // Torre branca em h1
        board.setPiece(7, 5, new Knight(true)); // Cavalo bloqueando f1

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Roque não deve ser permitido com peça no caminho", model.selectPiece(7, 4));
        assertFalse("Rei não deve poder rocar com cavalo em f1", model.selectTargetSquare(7, 6));
    }

    // Testa roque inválido após o rei já ter se movido
    @Test(timeout = 2000)
    public void castlingInvalidIfKingMoved() {
        Board board = new Board(true);
        board.clear();

        King king = new King(true);
        king.setHasMoved(true); // rei já se moveu

        board.setPiece(7, 4, king);            // Rei branco em e1
        board.setPiece(7, 7, new Rook(true));  // Torre branca em h1

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei selecionado", model.selectPiece(7, 4));
        assertFalse("Roque inválido se rei já se moveu", model.selectTargetSquare(7, 6));
    }

    // Testa en passant válido: peão branco captura peão preto recém movido duas casas
    @Test(timeout = 2000)
    public void enPassantValidCapture() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(3, 4, new Pawn(true));   // Peão branco em e5
        board.setPiece(1, 5, new Pawn(false));  // Peão preto em f7

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

     // Movimento do peão preto: f7 → f5 (feito manualmente para garantir controle total)
        Position from = new Position(1, 5); // f7
        Position to = new Position(3, 5);   // f5

        assertTrue("Movimento válido para f5", model.getBoard().getPiece(1, 5).isValidMove(from, to, model.getBoard()));
        assertTrue("Rei não está em cheque após movimento", model.canMoveToEscapeCheck(from, to));

        model.getBoard().movePiece(from, to);
        model.getBoard().getPiece(3, 5).setHasMoved(true);
        model.getBoard().setPiece(2, 5, null); // garante que f6 esteja livre
        model.getBoard().setPiece(1, 5, null); // limpa origem
        model.setBoard(model.getBoard()); // atualiza board no modelo
        model.selectPiece(3, 4); // peão branco e5
        model.setEnPassantTarget(new Position(2, 5)); // manualmente registra f6 como possível en passant
        model.setWhiteTurn(true); // força turno das brancas


        // Movimento do peão branco: en passant captura em f6
        assertTrue("Peão branco selecionado", model.selectPiece(3, 4));
        assertTrue("Captura en passant válida", model.selectTargetSquare(2, 5));

        // Confirma que o peão preto foi removido da casa atrás (3,5)
        assertNull("Peão preto deve ter sido capturado en passant", board.getPiece(3, 5));
    }

    // Testa en passant inválido se peão branco tenta capturar sem o movimento anterior correto
    @Test(timeout = 2000)
    public void enPassantInvalidIfDelayed() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(3, 4, new Pawn(true));   // Peão branco em e5
        board.setPiece(3, 5, new Pawn(false));  // Peão preto já em f5 (não recém chegado)

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        // Movimento do peão branco tentando en passant
        assertTrue("Peão branco selecionado", model.selectPiece(3, 4));
        assertFalse("En passant inválido sem movimento duplo anterior", model.selectTargetSquare(2, 5));
    }

    // Testa que o peão normal captura diagonal ainda funciona (não en passant)
    @Test(timeout = 2000)
    public void pawnRegularCaptureStillValid() {
        Board board = new Board(true);
        board.clear();

        board.setPiece(3, 4, new Pawn(true));   // Peão branco em e5
        board.setPiece(2, 5, new Pawn(false));  // Peão preto em f6

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Peão branco pode capturar normalmente em f6", model.selectPiece(3, 4));
        assertTrue("Captura válida", model.selectTargetSquare(2, 5));
    }
}
