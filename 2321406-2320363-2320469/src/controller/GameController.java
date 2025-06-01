package controller;

import model.ChessModel;
import view.ConsoleView;
import view.GameView;

import javax.swing.*;

public class GameController {
    private ChessModel model;
    private GameView view;
    private ConsoleView consoleView;

    public GameController(ChessModel model) {
        this.model = model;
    }

    public void setGameView(GameView view) {
        this.view = view;
    }

    public void setConsoleView(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    public void verificarFimDeJogo() {
        if (model.isCheckMate()) {
            JOptionPane.showMessageDialog(view, "Xeque-mate! O jogador " + (model.isWhiteTurn() ? "branco" : "preto") + " perdeu.");
            reiniciar();
        } else if (model.isStalelMate()) {
            JOptionPane.showMessageDialog(view, "Empate por congelamento!");
            reiniciar();
        } else if (consoleView != null) {
            consoleView.atualizarTurno();
        }
    }

    public void tratarPromocao(String tipo) {
        model.promotePawn(tipo);
        view.repaint();
        verificarFimDeJogo();
    }

    private void reiniciar() {
        ChessModel.resetInstance();
        model = ChessModel.getInstance();
        view.setModel(model);
        this.setGameView(view);
        view.repaint();
        if (consoleView != null) {
            consoleView.atualizarTurno();
        }
    }
}
