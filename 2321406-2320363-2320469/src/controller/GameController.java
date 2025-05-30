package controller;

import model.ChessModel;
import view.GameView;

public class GameController {
    private ChessModel model;
    private GameView view;

    public GameController(ChessModel model) {
        this.model = model;
    }

    public void setGameView(GameView view) {
        this.view = view;
    }

    public void processarClique(int row, int col) {
        // Tenta selecionar peça. Se já houver uma selecionada, tenta mover.
        if (!model.selectPiece(row, col)) {
            model.selectTargetSquare(row, col);
        }

        if (view != null) {
            view.repaint();
        }
    }

    public boolean isTurnoBranco() {
        return model.isWhiteTurn();
    }
}
