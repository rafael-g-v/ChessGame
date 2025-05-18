package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionTest {
    
    // Testa se duas posições com as mesmas coordenadas são consideradas iguais - Resultado esperado: true
    @Test(timeout = 2000)
    public void testEqualPositionsAreEqual() {
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 4);
        assertTrue("Posições iguais devem ser consideradas iguais", pos1.equals(pos2));
    }

    // Testa se duas posições com coordenadas diferentes são consideradas diferentes - Resultado esperado: false
    @Test(timeout = 2000)
    public void testDifferentPositionsAreNotEqual() {
        Position pos1 = new Position(3, 4);
        Position pos3 = new Position(4, 3);
        assertFalse("Posições diferentes não devem ser iguais", pos1.equals(pos3));
    }

    // Testa se o construtor de Position lança exceção ao receber coordenadas inválidas (fora dos limites) - Resultado esperado: IllegalArgumentException
    @Test(expected = IllegalArgumentException.class, timeout = 2000)
    public void testInvalidPosition() {
        new Position(-1, 8); // linha negativa e coluna fora do tabuleiro (0–7)
    }
}
