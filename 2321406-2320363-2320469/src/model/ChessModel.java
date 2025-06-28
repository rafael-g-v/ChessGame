package model;

import observer.Observable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal que representa o modelo do jogo de xadrez (ChessModel).
 * Controla o estado do tabuleiro, o turno atual e as regras básicas de movimentação e cheque.
 */
public class ChessModel implements Observable  {
    private static ChessModel instance;
    private Board board;
    private boolean showSaveMenuRequested = false;
    private boolean showPromotionMenuRequested = false;
    private boolean whiteTurn = true;
    private Position selectedPiecePos = null;
    private Position pendingPromotionPos = null;  // se != null, há promoção pendente
    private Position enPassantTarget = null; // Posição do peão que pode ser capturado por en passant (válido apenas no turno seguinte)
    private int halfmoveClock = 0;     // contador dos 50 lances
    private int fullMoveNumber = 1;    // número completo do lance

    

    // Construtor privado (padrão Singleton). Inicializa o tabuleiro com a configuração padrão.
    private ChessModel() {
        board = new Board(false);
    }

    // Retorna a instância única do modelo. Cria uma nova se ainda não existir.
    public static ChessModel getInstance() {
        if (instance == null) {
            instance = new ChessModel();
        }
        return instance;
    }

    // Reseta a instância atual do modelo (usado principalmente em testes).
    public static void resetInstance() {
        instance = null;
    }

    // Define um tabuleiro customizado. Usado para setups específicos ou testes.
    public void setBoard(Board customBoard) {
        this.board = customBoard;
        notificarObservadores();
    }

    // Retorna o tabuleiro atual do modelo.
    public Board getBoard() {
        return board;
    }

    // Seleciona uma peça com base nas coordenadas (linha e coluna).
    // Só permite selecionar se for uma peça da vez (branca ou preta conforme o turno).
    public boolean selectPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.isWhite() == whiteTurn) {
            selectedPiecePos = new Position(row, col);
            return true;
        }
        return false;
    }

    // Tenta mover a peça selecionada para a casa de destino informada (linha e coluna).
    // Só realiza o movimento se for válido e se o rei não ficar em cheque após isso.
    public boolean selectTargetSquare(int row, int col) {
        if (selectedPiecePos == null) return false;

        Position target = new Position(row, col);
        Piece piece = board.getPiece(selectedPiecePos.row, selectedPiecePos.col);

        if (piece.isValidMove(selectedPiecePos, target, board)) {
            if (!canMoveToEscapeCheck(selectedPiecePos, target)) {
                return false;
            }

            // Trata movimento especial: en passant (remoção do peão capturado)
            if (piece instanceof Pawn) {
                if (target.equals(enPassantTarget) && board.isEmpty(target.row, target.col)) {
                    int capturedRow = whiteTurn ? target.row + 1 : target.row - 1;
                    board.setPiece(capturedRow, target.col, null); // Remove o peão capturado
                }
            }


            // Trata movimento especial: roque (movimenta a torre também)
            if (piece instanceof King && Math.abs(target.col - selectedPiecePos.col) == 2) {
                int rookCol   = (target.col > selectedPiecePos.col) ? 7 : 0;
                Position rook = new Position(selectedPiecePos.row, rookCol);

                // Valida sem alterar o jogo
                if (!attemptCastling(selectedPiecePos, rook)) return false;

                // Faz o movimento – primeiro rei, depois torre
                int rookTargetCol = (rookCol == 7) ? 5 : 3;
                board.movePiece(selectedPiecePos, target);
                board.movePiece(rook, new Position(selectedPiecePos.row, rookTargetCol));

                selectedPiecePos = null;
                pendingPromotionPos = null;
                enPassantTarget = null;
                whiteTurn = !whiteTurn;
                return true;
            }
            

            // Atualiza a posição de en passant, se for um peão que se moveu duas casas
            if (piece instanceof Pawn) {
                if (Math.abs(target.row - selectedPiecePos.row) == 2) {
                    enPassantTarget = new Position((target.row + selectedPiecePos.row) / 2, target.col);
                } else {
                    enPassantTarget = null; // Limpa se não for jogada válida para en passant
                }
            } else {
                enPassantTarget = null; // Limpa se não for um peão
            }
            
            // Se for avanço de peão ou captura, zera clock dos 50 lances
            if (piece instanceof Pawn || (board.getPiece(target.row, target.col) != null)) {
                halfmoveClock = 0;
            } else {
                halfmoveClock++;
            }

            // Se foi a vez das pretas, incrementa fullMoveNumber
            if (!whiteTurn) {
                fullMoveNumber++;
            }

            // Verifica promoção pendente
            if (piece instanceof Pawn) {
                if ((piece.isWhite() && target.row == 0) || (!piece.isWhite() && target.row == 7)) {
                    board.movePiece(selectedPiecePos, target);  // Move peão para a última linha
                    notificarObservadores();
                    pendingPromotionPos = target;
                    selectedPiecePos = null;
                    return true;
                }
            }
            
            // Move a peça principal
            board.movePiece(selectedPiecePos, target);
            notificarObservadores();
            selectedPiecePos = null;
            whiteTurn = !whiteTurn;
            
            return true;
        }
        return false;
    }



    // Verifica se o rei da cor indicada está em cheque.
    // A função percorre o tabuleiro procurando por ameaças ao rei.
    public boolean isInCheck(boolean isWhite) {
        Position kingPos = findKingPosition(isWhite);
        if (kingPos == null) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.isWhite() != isWhite) {
                    Position from = new Position(row, col);
                    if (piece.isValidMove(from, kingPos, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Procura e retorna a posição do rei da cor especificada.
    // Retorna null se o rei não for encontrado (teoricamente nunca deve acontecer).
    private Position findKingPosition(boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    // Verifica se mover uma peça de uma posição para outra remove o rei do cheque.
    // Faz o movimento de forma temporária, verifica o estado, e depois desfaz tudo.
    public boolean canMoveToEscapeCheck(Position from, Position to) {
        Piece piece = board.getPiece(from.row, from.col);
        boolean isWhite = piece.isWhite();
        Piece captured = board.getPiece(to.row, to.col);
        boolean movedBefore = piece.hasMoved();
        boolean capturedMoved = captured != null && captured.hasMoved();

        board.movePiece(from, to);
        boolean stillInCheck = isInCheck(isWhite);
        board.movePiece(to, from);
        board.setPiece(to.row, to.col, captured);

        piece.setHasMoved(movedBefore);
        if (captured != null) captured.setHasMoved(capturedMoved);
        
        return !stillInCheck;
    }

    // Retorna verdadeiro se for a vez das peças brancas jogarem.
    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    /**
     * Retorna o código da peça localizada em uma determinada posição do tabuleiro.
     * O código é composto por uma letra indicando a cor ('w' para branco, 'b' para preto)
     * seguida pela letra inicial do tipo da peça em minúsculo.
     * Exemplo: "wp" para peão branco, "bk" para rei preto.
     * A letra do cavalo (Knight) é representada por 'n' para evitar conflito com o rei (King).
     */
    public String getPieceCode(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece == null) return null;

        String cor = piece.isWhite() ? "w" : "b";
        String tipo = piece.getClass().getSimpleName().toLowerCase().substring(0, 1);

        if (piece.getClass().getSimpleName().equals("Knight")) {
            tipo = "n";
        }

        return cor + tipo;
    }

    /**
     * Verifica se o jogador da vez está em situação de xeque-mate.
     * Para isso, primeiro verifica se o rei está em cheque.
     * Se estiver, e se não houver nenhum movimento legal que possa livrar o jogador do cheque,
     * então a partida terminou em xeque-mate.
     */
    public boolean isCheckMate() {
        boolean isWhite = whiteTurn;
        if (!isInCheck(isWhite)) {
            return false;
        }
        return !hasAnyLegalMove(isWhite);
    }

    /**
     * Verifica se o jogador da vez está em situação de afogamento (stalemate).
     * Isso ocorre quando o jogador não está em cheque, mas também não possui
     * nenhum movimento legal possível — o que caracteriza um empate.
     */
    public boolean isStalelMate() {
        boolean isWhite = whiteTurn;
        if (isInCheck(isWhite)) {
            return false;
        }
        return !hasAnyLegalMove(isWhite);
    }

    /**
     * Verifica se o jogador da vez possui ao menos um movimento legal disponível.
     * Percorre todas as peças do jogador no tabuleiro, testando todos os movimentos possíveis.
     * Se existir ao menos um movimento válido que não coloque o próprio rei em cheque,
     * então o jogador ainda pode jogar.
     */
    private boolean hasAnyLegalMove(boolean isWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.isWhite() == isWhite) {
                    Position from = new Position(row, col);
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Position to = new Position(toRow, toCol);
                            if (piece.isValidMove(from, to, board)) {
                                if (canMoveToEscapeCheck(from, to)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Realiza a promoção de um peão que alcançou a última linha do tabuleiro.
     * A nova peça é escolhida com base no tipo passado como argumento.
     * Após a promoção, a posição de promoção pendente é limpa e o turno muda para o outro jogador.
     * Caso o tipo de peça fornecido seja inválido, uma exceção é lançada.
     */
    public boolean promotePawn(String pieceType) {
        if (pendingPromotionPos == null) return false;

        boolean isWhite = board.getPiece(pendingPromotionPos.row, pendingPromotionPos.col).isWhite();
        Piece newPiece;

        String type = pieceType.toLowerCase();

        if (type.equals("queen")) {
            newPiece = new Queen(isWhite);
        } else if (type.equals("rook")) {
            newPiece = new Rook(isWhite);
        } else if (type.equals("bishop")) {
            newPiece = new Bishop(isWhite);
        } else if (type.equals("knight")) {
            newPiece = new Knight(isWhite);
        } else {
            throw new IllegalArgumentException("Peça inválida para promoção: " + pieceType);
        }

        board.setPiece(pendingPromotionPos.row, pendingPromotionPos.col, newPiece);
        notificarObservadores();
        pendingPromotionPos = null;
        whiteTurn = !whiteTurn;
        return true;
    }

    /**
     * Verifica se há uma promoção de peão pendente.
     * Isso indica que o jogador deve escolher uma peça para substituir o peão promovido.
     */
    public boolean hasPendingPromotion() {
        return pendingPromotionPos != null;
    }

    /**
     * Retorna a lista de movimentos válidos para uma peça em uma determinada posição.
     * Considera tanto as regras individuais de movimentação da peça quanto a necessidade
     * de o movimento não deixar o próprio rei em cheque.
     * Caso a posição não contenha uma peça válida da vez, a lista retornada estará vazia.
     */
    public List<int[]> getValidMovesForPiece(int row, int col) {
        List<int[]> validMoves = new ArrayList<>();

        Piece piece = board.getPiece(row, col);
        if (piece == null || piece.isWhite() != whiteTurn) {
            return validMoves;
        }

        Position from = new Position(row, col);
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position to = new Position(r, c);
                if (piece.isValidMove(from, to, board) && canMoveToEscapeCheck(from, to)) {
                    validMoves.add(new int[]{r, c});
                }
            }
        }

        return validMoves;
    }
    
    /**  Retorna true se o roque é legal; NÃO mexe no tabuleiro  */
    public boolean attemptCastling(Position kingPos, Position rookPos) {
        Piece king = board.getPiece(kingPos.row, kingPos.col);
        Piece rook = board.getPiece(rookPos.row, rookPos.col);

        if (!(king instanceof King) || !(rook instanceof Rook)) return false;
        if (king.isWhite() != whiteTurn)                     return false;
        if (king.hasMoved() || rook.hasMoved())              return false;
        if (kingPos.row != rookPos.row)                      return false;

        int dir = (rookPos.col > kingPos.col) ? 1 : -1;

        // casas entre rei e torre devem estar vazias
        for (int c = kingPos.col + dir; c != rookPos.col; c += dir)
            if (!board.isEmpty(kingPos.row, c)) return false;

        // rei não pode estar em cheque
        if (isInCheck(king.isWhite())) return false;

        // nem pode atravessar casas atacadas
        for (int i = 1; i <= 2; i++) {
            Position step = new Position(kingPos.row, kingPos.col + i * dir);
            if (!canMoveToEscapeCheck(kingPos, step)) return false;
        }
        return true;   // ←  só diz se pode
    }


    
    // Retorna a posição atual válida para en passant, ou null se não houver
    public Position getEnPassantTarget() {
        return enPassantTarget;
    }
    
    /**
     * Define manualmente o alvo de en passant (usado principalmente para testes).
     */
    public void setEnPassantTarget(Position pos) {
        this.enPassantTarget = pos;
    }

    /**
     * Força a definição do turno (branco ou preto). Usado apenas para testes.
     */
    public void setWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
    }

    private boolean canCastle(char color, boolean kingside) {
        int row = (color == 'w') ? 7 : 0;
        Piece king = board.getPiece(row, 4);
        Piece rook = kingside ? board.getPiece(row, 7) : board.getPiece(row, 0);

        if (!(king instanceof King) || !(rook instanceof Rook)) return false;
        if (king.isWhite() != (color == 'w') || rook.isWhite() != (color == 'w')) return false;
        if (king.hasMoved() || rook.hasMoved()) return false;

        return true;
    }

    
    public String generateFEN() {
        StringBuilder fen = new StringBuilder();

        // Parte 1: tabuleiro
        for (int row = 0; row < 8; row++) {
            int empty = 0;
            for (int col = 0; col < 8; col++) {
                Piece p = board.getPiece(row, col);
                if (p == null) {
                    empty++;
                } else {
                    if (empty > 0) {
                        fen.append(empty);
                        empty = 0;
                    }
                    fen.append(getFENSymbol(p));
                }
            }
            if (empty > 0) {
                fen.append(empty);
            }
            if (row < 7) {
                fen.append('/');
            }
        }

        // Parte 2: turno
        fen.append(' ').append(whiteTurn ? 'w' : 'b');

        // Parte 3: direitos de roque
        StringBuilder castling = new StringBuilder();
        if (canCastle('w', true)) castling.append('K');
        if (canCastle('w', false)) castling.append('Q');
        if (canCastle('b', true)) castling.append('k');
        if (canCastle('b', false)) castling.append('q');
        if (castling.length() == 0) castling.append('-');
        fen.append(' ').append(castling);

        // Parte 4: en passant
        fen.append(' ');
        if (enPassantTarget != null) {
            char file = (char) ('a' + enPassantTarget.col);
            char rank = (char) ('8' - enPassantTarget.row);
            fen.append(file).append(rank);
        } else {
            fen.append('-');
        }

        // Parte 5: meio-lances
        fen.append(' ').append(halfmoveClock);

        // Parte 6: número de lance
        fen.append(' ').append(fullMoveNumber);

        return fen.toString();
    }
    
 // Método auxiliar que converte uma peça em seu símbolo FEN correspondente.
 //recebe: piece Peça a ser convertida
 //retorna: a peça na notação FEN
    private char getFENSymbol(Piece piece) {
        char symbol;

        if (piece instanceof King) symbol = 'k';
        else if (piece instanceof Queen) symbol = 'q';
        else if (piece instanceof Rook) symbol = 'r';
        else if (piece instanceof Bishop) symbol = 'b';
        else if (piece instanceof Knight) symbol = 'n';
        else if (piece instanceof Pawn) symbol = 'p';
        else symbol = '?';

        return piece.isWhite() ? Character.toUpperCase(symbol) : symbol;
    }

 //Carrega um estado de jogo a partir de uma string FEN.
 //recebe: fen String FEN contendo o estado do jogo a ser carregado
 //throws: IllegalArgumentException se o formato FEN for inválido
    public void loadFEN(String fen) {
        String[] parts = fen.split(" ");
        if (parts.length < 4) {
            throw new IllegalArgumentException("FEN inválido: " + fen);
        }

        String boardPart = parts[0];
        String turnPart = parts[1];
        String castlingPart = parts[2];
        String enPassantPart = parts[3];

        this.board.clear();

        int row = 0, col = 0;
        for (char ch : boardPart.toCharArray()) {
            if (ch == '/') {
                row++;
                col = 0;
            } else if (Character.isDigit(ch)) {
                col += ch - '0';
            } else {
                boolean isWhite = Character.isUpperCase(ch);
                Piece piece = switch (Character.toLowerCase(ch)) {
                    case 'k' -> new King(isWhite);
                    case 'q' -> new Queen(isWhite);
                    case 'r' -> new Rook(isWhite);
                    case 'b' -> new Bishop(isWhite);
                    case 'n' -> new Knight(isWhite);
                    case 'p' -> new Pawn(isWhite);
                    default -> throw new IllegalArgumentException("Peça desconhecida: " + ch);
                };
                board.setPiece(row, col, piece);
                col++;
            }
        }

        notificarObservadores();
        this.whiteTurn = turnPart.equals("w");

        // castling rights → set hasMoved false se direito existe
        if (castlingPart.contains("K")) {
            Piece r = board.getPiece(7,7);
            Piece k = board.getPiece(7,4);
            if (r instanceof Rook) r.setHasMoved(false);
            if (k instanceof King) k.setHasMoved(false);
        } else if (board.getPiece(7,7) instanceof Rook) {
            board.getPiece(7,7).setHasMoved(true);
        }

        if (castlingPart.contains("Q")) {
            Piece r = board.getPiece(7,0);
            Piece k = board.getPiece(7,4);
            if (r instanceof Rook) r.setHasMoved(false);
            if (k instanceof King) k.setHasMoved(false);
        } else if (board.getPiece(7,0) instanceof Rook) {
            board.getPiece(7,0).setHasMoved(true);
        }

        if (castlingPart.contains("k")) {
            Piece r = board.getPiece(0,7);
            Piece k = board.getPiece(0,4);
            if (r instanceof Rook) r.setHasMoved(false);
            if (k instanceof King) k.setHasMoved(false);
        } else if (board.getPiece(0,7) instanceof Rook) {
            board.getPiece(0,7).setHasMoved(true);
        }

        if (castlingPart.contains("q")) {
            Piece r = board.getPiece(0,0);
            Piece k = board.getPiece(0,4);
            if (r instanceof Rook) r.setHasMoved(false);
            if (k instanceof King) k.setHasMoved(false);
        } else if (board.getPiece(0,0) instanceof Rook) {
            board.getPiece(0,0).setHasMoved(true);
        }

        // en passant
        if (!enPassantPart.equals("-")) {
            int colEp = enPassantPart.charAt(0) - 'a';
            int rowEp = '8' - enPassantPart.charAt(1);
            this.enPassantTarget = new Position(rowEp, colEp);
        } else {
            this.enPassantTarget = null;
        }

        // opcional: meio-lances e fullMoveNumber
        this.halfmoveClock = (parts.length > 4) ? Integer.parseInt(parts[4]) : 0;
        this.fullMoveNumber = (parts.length > 5) ? Integer.parseInt(parts[5]) : 1;
    }

    //Solicita a exibição do menu de salvamento do jogo.
    public void requestShowSaveMenu() {
        this.showSaveMenuRequested = true;
        notificarObservadores();
    }
    
     //Verifica se há uma solicitação pendente para exibir o menu de salvamento.
     //returna: true se o menu de salvamento deve ser exibido, false caso contrário
    public boolean isShowSaveMenuRequested() {
        return showSaveMenuRequested;
    }

    //Limpa a solicitação de exibição do menu de salvamento.
    public void clearShowSaveMenuRequest() {
        this.showSaveMenuRequested = false;
    }

    //Solicita a exibição do menu de promoção de peão.
    public void requestShowPromotionMenu() {
        this.showPromotionMenuRequested = true;
        notificarObservadores();
    }

    //Verifica se há uma solicitação pendente para exibir o menu de promoção.
    //retorna: true se o menu de promoção deve ser exibido, false caso contrário
    public boolean isShowPromotionMenuRequested() {
        return showPromotionMenuRequested;
    }

    //Limpa a solicitação de exibição do menu de promoção.
    public void clearShowPromotionMenuRequest() {
        this.showPromotionMenuRequested = false;
    }
}
