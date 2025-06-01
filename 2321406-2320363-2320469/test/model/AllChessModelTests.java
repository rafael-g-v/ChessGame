package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ChessModelBasicTest.class,
    ChessModelCheckTest.class,
    ChessModelMovementTest.class,
    CheckModelMateTest.class,
})

public class AllChessModelTests {}