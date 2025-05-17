package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChessModelBasicTest {

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
}
