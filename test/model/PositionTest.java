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

    @Test(expected = IllegalArgumentException.class, timeout = 2000)
    public void testInvalidPosition() {
        new Position(-1, 8);
    }
}
