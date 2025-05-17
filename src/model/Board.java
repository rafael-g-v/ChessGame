package model;

class Board {
    private Piece[][] board;

    public Board(boolean empty) {
        board = new Piece[8][8];
        if (!empty) {
            setupInitialPosition();
        }
    }
    
    private void setupInitialPosition() {
        // Peões
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(false); // pretos
            board[6][i] = new Pawn(true);  // brancos
        }

        // Torres
        board[0][0] = new Rook(false);
        board[0][7] = new Rook(false);
        board[7][0] = new Rook(true);
        board[7][7] = new Rook(true);

        // Cavalos
        board[0][1] = new Knight(false);
        board[0][6] = new Knight(false);
        board[7][1] = new Knight(true);
        board[7][6] = new Knight(true);

        // Bispos
        board[0][2] = new Bishop(false);
        board[0][5] = new Bishop(false);
        board[7][2] = new Bishop(true);
        board[7][5] = new Bishop(true);

        // Rainhas
        board[0][3] = new Queen(false);
        board[7][3] = new Queen(true);

        // Reis
        board[0][4] = new King(false);
        board[7][4] = new King(true);
    }

    
    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void movePiece(Position from, Position to) {
        Piece piece = board[from.row][from.col];
        board[to.row][to.col] = piece;
        board[from.row][from.col] = null;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }
}
