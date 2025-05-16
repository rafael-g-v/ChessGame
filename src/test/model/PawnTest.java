package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PawnTest {

    private Board board;
    private Pawn whitePawn;

    @Before
    public void setUp() {
        board = new Board();
        whitePawn = new Pawn(true);
    }

    @Test(timeout = 2000)
    public void validPawnTestMoveOneHouseFront() 
    {
        Position from = new Position(6, 3); // posição inicial do peão branco
        Position to = new Position(5, 3);   // avançar uma casa para frente (válido)

        assertTrue("Peão branco deve avançar uma casa para frente", whitePawn.isValidMove(from, to, board));
    }

    @Test(timeout = 2000)
    public void invalidPawnTestMoveOneHouseBack()
    {
        Position from = new Position(6, 3); // posição inicial do peão branco
        Position to = new Position(7, 3);   // tentar mover para trás, inválido

        assertFalse("Peão não pode mover para trás", whitePawn.isValidMove(from, to, board));
    }
}
