package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChessModelBasicTest {

    @Before
    public void setup() {
        ChessModel.resetInstance();
    }

    @Test(timeout = 2000)
    public void testKingNotInCheckInitially() {
        ChessModel model = ChessModel.getInstance();
        assertFalse(model.isInCheck(true));  // Rei branco não está em cheque
        assertFalse(model.isInCheck(false)); // Rei preto não está em cheque
    }
}
