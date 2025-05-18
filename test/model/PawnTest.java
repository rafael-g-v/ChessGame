package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PawnTest {

    private Board board;
    private Pawn whitePawn;

    @Before
    public void prepare() {
        board = new Board(true);
        whitePawn = new Pawn(true);
    }

    // Testa se o peão branco pode avançar uma casa para frente - Resultado esperado: true
    @Test(timeout = 2000)
    public void validPawnTestMoveOneHouseFront() {
        Position from = new Position(6, 3); // posição inicial do peão branco
        Position to = new Position(5, 3);   // avançar uma casa para frente (válido)

        assertTrue("Peão branco deve avançar uma casa para frente", whitePawn.isValidMove(from, to, board));
    }

    // Testa se o peão branco tenta mover para trás, o que é inválido - Resultado esperado: false
    @Test(timeout = 2000)
    public void invalidPawnTestMoveOneHouseBack() {
        Position from = new Position(6, 3); // posição inicial do peão branco
        Position to = new Position(7, 3);   // tentar mover para trás, inválido

        assertFalse("Peão não pode mover para trás", whitePawn.isValidMove(from, to, board));
    }
}
