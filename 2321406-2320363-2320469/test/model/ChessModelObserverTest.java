package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import observer.Observer;
import observer.Observable;

/**
 * Classe de teste para verificar o funcionamento do Observer no ChessModel.
 * Garante que o método notificar dos observadores é chamado quando o modelo muda.
 */
public class ChessModelObserverTest {

    private ChessModel model;
    private boolean observerNotificado;

    @Before
    public void setup() {
        ChessModel.resetInstance();
        model = ChessModel.getInstance();
        observerNotificado = false;
    }

    @Test(timeout = 2000)
    public void observerNotificadoAoMoverPeca() {
        Board board = new Board(true);
        board.clear();

        // Coloca um peão branco no tabuleiro
        board.setPiece(6, 0, new Pawn(true));  // Peão em a2

        model.setBoard(board);
        model.setWhiteTurn(true);

        // Registra um observer que só marca quando for notificado
        model.addObserver(new Observer() {
            @Override
            public void notify(Observable o) {
                observerNotificado = true;
            }
        });

        // Executa um movimento válido
        assertTrue("Peça deve ser selecionada", model.selectPiece(6, 0)); // a2
        assertTrue("Movimento deve ser válido", model.selectTargetSquare(4, 0)); // a4

        // Verifica se o observer foi notificado
        assertTrue("Observer deveria ter sido notificado após o movimento", observerNotificado);
    }

    @Test(timeout = 2000)
    public void observerNotificadoAoPromoverPeao() {
        Board board = new Board(true);
        board.clear();

        // Coloca um peão branco pronto para promover
        board.setPiece(1, 0, new Pawn(true));  // Peão em a7

        model.setBoard(board);
        model.setWhiteTurn(true);

        model.addObserver(new Observer() {
            @Override
            public void notify(Observable o) {
                observerNotificado = true;
            }
        });

        // Move o peão para a8
        assertTrue("Peça deve ser selecionada", model.selectPiece(1, 0));
        assertTrue("Movimento deve ser válido para promoção", model.selectTargetSquare(0, 0));

        // Deve ter notificado após movimento
        assertTrue("Observer deveria ter sido notificado após movimento para promoção", observerNotificado);

        // Reinicia flag
        observerNotificado = false;

        // Realiza a promoção
        assertTrue("Promoção deve ser possível", model.promotePawn("Queen"));

        // Deve ter notificado após promoção
        assertTrue("Observer deveria ter sido notificado após promoção", observerNotificado);
    }

	@Test(timeout = 2000)
    public void observerNaoNotificadoSemMudancaDeEstado() {
        Board board = new Board(true);
        board.clear();

        model.setBoard(board);
        model.setWhiteTurn(true);

        model.addObserver(new Observer() {
            @Override
            public void notify(Observable o) {
                observerNotificado = true;
            }
        });

        // Tenta selecionar casa vazia → não deve gerar notificação
        assertFalse("Seleção inválida (sem peça)", model.selectPiece(0, 0));

        // Confirma que observer não foi chamado
        assertFalse("Observer não deve ter sido notificado sem alteração de estado", observerNotificado);
    }
}
