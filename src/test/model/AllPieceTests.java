package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    KnightTest.class,
    RookTest.class,
    PawnTest.class,
    BishopTest.class,
    QueenTest.class,
    KingTest.class,
    PieceTest.class,
    BoardTest.class,
    PositionTest.class
})
public class AllPieceTests {}
