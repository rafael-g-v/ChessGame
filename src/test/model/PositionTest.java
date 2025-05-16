package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionTest {
    @Test(timeout = 2000)
    public void testPositionEquality() {
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 4);
        Position pos3 = new Position(4, 3);
        assertTrue("Posições iguais devem ser consideradas iguais", pos1.equals(pos2));
        assertFalse("Posições diferentes não devem ser iguais", pos1.equals(pos3));
    }

    @Test(timeout = 2000)
    public void testInvalidPosition() {
        // Testa criação de posição com coordenadas inválidas (deve lançar exceção?)
        // (Depende da implementação. Se Position não validar, este teste é opcional)
        try {
            new Position(-1, 8);
            fail("Deveria lançar exceção para coordenadas inválidas");
        } catch (IllegalArgumentException e) {
            // Comportamento esperado
        }
    }
}
