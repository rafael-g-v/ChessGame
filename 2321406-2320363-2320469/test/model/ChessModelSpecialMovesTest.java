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
        Position kingPosition = new Position(7, 4); // e1
        Position rookPosition = new Position(7, 7); // h1
        board.clear();

        board.setPiece(7, 4, new King(true));  // Rei branco em e1
        board.setPiece(7, 7, new Rook(true));  // Torre branca em h1

        ChessModel model = ChessModel.getInstance();
        model.setWhiteTurn(true); // se for o rei branco
        model.setBoard(board);

        assertTrue("Tentativa de roque", model.attemptCastling(kingPosition, rookPosition));
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
        Position kingPosition = new Position(7, 4); // e1

        board.setPiece(7, 4, king);            // Rei branco em e1
        board.setPiece(7, 7, new Rook(true));  // Torre branca em h1
        Position rookPosition = new Position(7, 7); // h1

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertFalse("Tentativa de roque após mover", model.attemptCastling(kingPosition, rookPosition));
        assertTrue("Rei selecionado", model.selectPiece(7, 4));
        assertFalse("Roque inválido se rei já se moveu", model.selectTargetSquare(7, 6));
    }
    
 // Testa que o roque é inválido se o rei tentar mover estando em cheque
    @Test(timeout = 2000)
    public void castlingInvalidIfPassingThroughCheck() {
        Board board = new Board(true);
        board.clear();

        King king = new King(true);
        Rook rook = new Rook(true);
        Position kingPosition = new Position(7, 4); // e1
        Position rookPosition = new Position(7, 7); // h1

        board.setPiece(7, 4, king); // Rei em e1
        board.setPiece(7, 7, rook); // Torre em h1

        // Bispo inimigo atacando b4 (7,5)
        board.setPiece(4, 1, new Bishop(false)); // Torre preta em f3

        ChessModel model = ChessModel.getInstance();
        model.setBoard(board);

        assertTrue("Rei está em cheque", model.isInCheck(true));
        assertFalse("Tentativa de roque", model.attemptCastling(kingPosition, rookPosition));
        
        model.selectPiece(7, 4);
        assertFalse("Roque deve ser inválido se o rei passar por casa atacada", model.selectTargetSquare(7, 6));
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

        // Movimento do peão preto: f7 → f5 
        Position from = new Position(1, 5); // f7
        Position to = new Position(3, 5);   // f5

        model.getBoard().getPiece(1, 5);
        model.getBoard().movePiece(from, to);
        model.getBoard().getPiece(3, 5).setHasMoved(true);
        
        model.selectPiece(3, 4); // peão branco e5
        model.setEnPassantTarget(new Position(2, 5)); // Verifica o estado de en passant

        // Movimento do peão branco: en passant captura em f6
        model.selectPiece(3, 4);  // Peão branco selecionado"
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
        model.selectPiece(3, 4);  // Seleciona o peão de brancas
        assertFalse("En passant inválido sem movimento duplo anterior", model.selectTargetSquare(2, 5));
    }
}
