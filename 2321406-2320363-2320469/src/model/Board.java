package model;

// Representa o tabuleiro de xadrez, composto por uma matriz 8x8 de peças.
class Board {
    private Piece[][] board;

    // Construtor. Se o parâmetro "empty" for falso, inicializa o tabuleiro com a posição padrão.
    public Board(boolean empty) {
        board = new Piece[8][8];
        if (!empty) {
            setupInitialPosition();
        }
    }

    // Preenche o tabuleiro com a configuração inicial padrão do xadrez.
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

    // Remove todas as peças do tabuleiro, deixando todas as casas vazias.
    public void clear() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
    }

    // Retorna a peça presente na posição indicada (linha e coluna).
    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    // Move uma peça da posição "from" para a posição "to".
    // A posição de origem fica vazia após o movimento.
    public void movePiece(Position from, Position to) {
        Piece piece = board[from.row][from.col];
        board[to.row][to.col] = piece;
        board[from.row][from.col] = null;
    }

    // Coloca uma peça na posição indicada (linha e coluna).
    // Substitui qualquer peça anterior naquela casa.
    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    // Retorna true se a casa na posição indicada estiver vazia.
    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }
    
    public String toFEN() {
        StringBuilder fen = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;

            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];

                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }

                    String symbol = getFENSymbol(piece);
                    fen.append(symbol);
                }
            }

            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            if (row < 7) {
                fen.append('/');
            }
        }

        return fen.toString();
    }

    // Auxiliar
    private String getFENSymbol(Piece piece) {
        String letter;

        if (piece instanceof King) letter = "k";
        else if (piece instanceof Queen) letter = "q";
        else if (piece instanceof Rook) letter = "r";
        else if (piece instanceof Bishop) letter = "b";
        else if (piece instanceof Knight) letter = "n";
        else if (piece instanceof Pawn) letter = "p";
        else throw new IllegalArgumentException("Tipo de peça desconhecido");

        return piece.isWhite() ? letter.toUpperCase() : letter;
    }

    
    public void fromFEN(String fen) {
        clear(); // limpa o tabuleiro

        String[] rows = fen.split("/");
        if (rows.length != 8) {
            throw new IllegalArgumentException("FEN inválido: deve conter 8 linhas");
        }

        for (int row = 0; row < 8; row++) {
            int col = 0;
            for (char c : rows[row].toCharArray()) {
                if (Character.isDigit(c)) {
                    col += c - '0';
                } else {
                    boolean isWhite = Character.isUpperCase(c);
                    Piece piece = createPieceFromFENChar(Character.toLowerCase(c), isWhite);
                    setPiece(row, col, piece);
                    col++;
                }
            }

            if (col != 8) {
                throw new IllegalArgumentException("FEN inválido: linha " + row + " não tem 8 colunas");
            }
        }
    }

    // Auxiliar
    private Piece createPieceFromFENChar(char c, boolean isWhite) {
        return switch (c) {
            case 'k' -> new King(isWhite);
            case 'q' -> new Queen(isWhite);
            case 'r' -> new Rook(isWhite);
            case 'b' -> new Bishop(isWhite);
            case 'n' -> new Knight(isWhite);
            case 'p' -> new Pawn(isWhite);
            default  -> throw new IllegalArgumentException("Caractere FEN inválido: " + c);
        };
    }

}
