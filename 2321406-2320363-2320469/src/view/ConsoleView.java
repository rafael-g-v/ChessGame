package view;

import controller.GameController;
import model.ChessModel;

import javax.swing.*;

public class ConsoleView extends JFrame {
    private ChessModel model;
    private GameView gameView;
    private GameController controller;
    private JLabel turnoLabel;

    public ConsoleView() {
        super("Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 700);
        setLocationRelativeTo(null);

        model = ChessModel.getInstance();
        controller = new GameController(model);
        controller.setConsoleView(this); // Passa a referência do ConsoleView
        gameView = new GameView(model);
        controller.setGameView(gameView);
        gameView.setController(controller);

        setJMenuBar(createMenuBar());
        add(gameView);

        atualizarTurno(); // mostra o turno inicial
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jogoMenu = new JMenu("Jogo");

        JMenuItem novaPartida = new JMenuItem("Nova Partida");
        novaPartida.addActionListener(e -> restartGame());

        JMenuItem carregarPartida = new JMenuItem("Carregar Partida...");
        carregarPartida.setEnabled(false); // para uso futuro

        jogoMenu.add(novaPartida);
        jogoMenu.add(carregarPartida);
        menuBar.add(jogoMenu);

        menuBar.add(Box.createHorizontalGlue()); // empurra o texto para a direita
        turnoLabel = new JLabel();
        menuBar.add(turnoLabel);

        return menuBar;
    }

    private void restartGame() {
        ChessModel.resetInstance();
        model = ChessModel.getInstance();
        gameView.setModel(model);
        controller = new GameController(model);
        controller.setConsoleView(this); // Atualiza a referência
        controller.setGameView(gameView);
        gameView.setController(controller);
        atualizarTurno();
        gameView.repaint();
    }

    public void atualizarTurno() {
        String cor = model.isWhiteTurn() ? "brancas" : "pretas";
        turnoLabel.setText("Turno de " + cor);
    }
}
