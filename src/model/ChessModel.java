package model;

public class ChessModel {
	private static ChessModel instance;
	private Board board;
	private boolean whiteTurn = true;
	private Position selectedPiecePos = null;
	
	private ChessModel() {
	    board = new Board();
	}
	
	public static ChessModel getInstance() {
	    if (instance == null) {
	        instance = new ChessModel();
	    }
	    return instance;
	}
	
	public boolean selecionaPeca(int row, int col) {
	    Piece piece = board.getPiece(row, col);
	    if (piece != null && piece.isWhite() == whiteTurn) {
	        selectedPiecePos = new Position(row, col);
	        return true;
	    }
	    return false;
	}
	
	public boolean selecionaCasa(int row, int col) {
	    if (selectedPiecePos == null) return false;
	    Position target = new Position(row, col);
	    Piece piece = board.getPiece(selectedPiecePos.row, selectedPiecePos.col);
	    
	    if (piece.isValidMove(selectedPiecePos, target, board)) {
	    	Piece captured = board.getPiece(target.row, target.col);

	        board.movePiece(selectedPiecePos, target);	    	
	        if (isInCheck(piece.isWhite())) {
	        	board.movePiece(target, selectedPiecePos);
	        	board.setPiece(target.row, target.col, captured);
	        	return false;
	        }
	        
	        selectedPiecePos = null;
	        whiteTurn = !whiteTurn;
	        return true;
	    }
	    return false;
	}
	
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
	
	
	public boolean isWhiteTurn() {
	    return whiteTurn;
	}
}