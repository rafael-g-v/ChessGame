package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal que representa o modelo do jogo de xadrez (ChessModel).
 * Controla o estado do tabuleiro, o turno atual e as regras básicas de movimentação e cheque.
 */
public class ChessModel {
    private static ChessModel instance;
    private Board board;
    private boolean whiteTurn = true;
    private Position selectedPiecePos = null;
    private Position pendingPromotionPos = null;  // se != null, há promoção pendente
    private Position enPassantTarget = null; // Posição do peão que pode ser capturado por en passant (válido apenas no turno seguinte)
    

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

            // Verifica promoção pendente
            if (piece instanceof Pawn) {
                if ((piece.isWhite() && target.row == 0) || (!piece.isWhite() && target.row == 7)) {
                    board.movePiece(selectedPiecePos, target);  // Move peão para a última linha
                    pendingPromotionPos = target;
                    selectedPiecePos = null;
                    return true;
                }
            }
            
            // Move a peça principal
            board.movePiece(selectedPiecePos, target);

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

    public String generateFEN() {
        StringBuilder fen = new StringBuilder();

        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;

            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);

                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }

                    char symbol = getFENSymbol(piece);
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

        fen.append(' ');
        fen.append(whiteTurn ? 'w' : 'b');

        // Por simplicidade: sem roque disponível, sem en passant, contadores zerados
        fen.append(" - - 0 1");

        return fen.toString();
    }

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

    public void loadFEN(String fen) {
        String[] parts = fen.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("FEN inválido: " + fen);
        }

        String boardPart = parts[0];
        String turnPart = parts[1];
        // Campos adicionais podem ser usados no futuro:
        // String castlingRights = (parts.length > 2) ? parts[2] : "-";
        // String enPassantTarget = (parts.length > 3) ? parts[3] : "-";

        board.clear();
        int row = 0, col = 0;

        for (char ch : boardPart.toCharArray()) {
            if (ch == '/') {
                row++;
                col = 0;
            } else if (Character.isDigit(ch)) {
                col += Character.getNumericValue(ch);
            } else {
                boolean isWhite = Character.isUpperCase(ch);
                Piece piece = switch (Character.toLowerCase(ch)) {
                    case 'k' -> new King(isWhite);
                    case 'q' -> new Queen(isWhite);
                    case 'r' -> new Rook(isWhite);
                    case 'b' -> new Bishop(isWhite);
                    case 'n' -> new Knight(isWhite);
                    case 'p' -> new Pawn(isWhite);
                    default -> throw new IllegalArgumentException("Peça desconhecida no FEN: " + ch);
                };
                board.setPiece(row, col, piece);
                col++;
            }
        }

        this.whiteTurn = turnPart.equals("w");
    }
}
/*
 * Observado: ter funcoes para gerenciar a lista de remover e adiconar da lista de eventos
 * Chamar um metodo no facade de adicionar observador
 * Observador vai notificar o sistema de eventos
 * Criar arquivos de observador e observado talvez ate em um pacote deles tudo publico
 */