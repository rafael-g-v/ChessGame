package controller;

import model.ChessModel;
import view.ConsoleView;
import view.GameView;

import javax.swing.*;

//Controlador do jogo de xadrez que coordena a comunicacao entre o chessmodel e as views.
public class GameController {
    private ChessModel model;              
    private GameView view; // view do tabuleiro
    private ConsoleView consoleView;  // view ta tela de inicio

 
    public GameController(ChessModel model) {
        this.model = model;
    }

    public void setGameView(GameView view) {
        this.view = view;
    }

 
    public void setConsoleView(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    // Verifica se foi cheque mate, congelamento, ou se o jogo continua
    public void checkEndOfGame() {
        if (model.isCheckMate()) {
            JOptionPane.showMessageDialog(view, "Xeque-mate! O jogador " + (model.isWhiteTurn() ? "branco" : "preto") + " perdeu.");
            restart();
        } else if (model.isStalelMate()) {
            JOptionPane.showMessageDialog(view, "Empate por congelamento!");
            restart();
        } else if (consoleView != null) {
            consoleView.updateTurn(); // Atualiza a barra de menu com a cor do próximo turno
        }
    }

    // Faz a promocao do peao recebendo como parametro o tipo da peca que ele ira se transformar
    public void setPawnPromotion(String tipo) {
        model.promotePawn(tipo);
        view.repaint();           // atualiza a peca
        checkEndOfGame();     
    }


    private void restart() {
        ChessModel.resetInstance(); // Reinicia o Singleton do modelo
        model = ChessModel.getInstance(); // Cria nova instância
        view.setModel(model); // Atualiza o modelo na visualização
        this.setGameView(view); // Reaplica a view ao controlador
        view.repaint(); // Redesenha o tabuleiro

        if (consoleView != null) {
            consoleView.updateTurn(); // Atualiza o turno inicial no menu
        }
    }
}
