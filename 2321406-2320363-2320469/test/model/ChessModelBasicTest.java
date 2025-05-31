package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de teste básica para o modelo de xadrez (ChessModel).
 * Verifica o estado inicial do jogo, garantindo que nenhum rei está em cheque ao iniciar a partida.
 */
public class ChessModelBasicTest {

    /**
     * Executa antes de cada teste.
     * Reinicializa a instância única do modelo de xadrez para garantir estado limpo.
     */
    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    /**
     * Testa se os reis branco e preto não estão em cheque no início do jogo padrão.
     */
    @Test(timeout = 2000)
    public void kingIsNotInCheckAtStart() {
        ChessModel model = ChessModel.getInstance();

        assertFalse("Rei branco não deve estar em cheque no início do jogo", model.isInCheck(true));
        assertFalse("Rei preto não deve estar em cheque no início do jogo", model.isInCheck(false));
    }
}
