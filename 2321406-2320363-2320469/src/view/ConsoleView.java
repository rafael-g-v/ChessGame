package view;

import controller.GameController;
import model.ChessModel;

import javax.swing.*;

/**
 * Janela principal do jogo de xadrez (View do padrão MVC).
 * Exibe o tabuleiro e um menu de opções.
 */
public class ConsoleView extends JFrame {
    private ChessModel model;
    private GameView gameView;
    private GameController controller;
    private JLabel turnoLabel;

    /**
     * Construtor da janela principal. Recebe o modelo já pronto (novo ou carregado).
     */
    public ConsoleView(ChessModel model) {
        super("Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 700);

        this.model = model;
        this.controller = new GameController(model);
        this.controller.setConsoleView(this);

        this.gameView = new GameView(model);
        this.controller.setGameView(gameView);
        this.gameView.setController(controller);

        setJMenuBar(createMenuBar()); // cria o turnoLabel
        updateTurn();                 // agora seguro
        add(gameView);

        setVisible(true);
    }

    /**
     * Cria a barra de menu com opções de jogo e exibe o turno atual.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Jogo");

        JMenuItem newGame = new JMenuItem("Reiniciar Partida");
        newGame.addActionListener(e -> controller.restartGame());


        JMenuItem carregarPartida = new JMenuItem("Carregar Partida");
        carregarPartida.addActionListener(e -> carregarPartida());

        gameMenu.add(newGame);
        gameMenu.add(carregarPartida);
        menuBar.add(gameMenu);

        turnoLabel = new JLabel(); // agora inicializada
        menuBar.add(turnoLabel);

        return menuBar;
    }

    /**
     * Atualiza a label de turno no menu.
     */
    public void updateTurn() {
        String cor = model.isWhiteTurn() ? "brancas" : "pretas";
        turnoLabel.setText("Turno de " + cor);
    }
    public void setController(GameController controller) {
        this.controller = controller;
        if (gameView != null) {
            controller.setGameView(gameView);
            gameView.setController(controller);
        }
    }
    
    private void carregarPartida() {
        ChessModel newModel = controller.carregarPartidaViaArquivo(this);
        if (newModel != null) {
            dispose();
            new ConsoleView(newModel);
        }
    }
    
    public void setModel(ChessModel model) {
        this.model = model;
    }
    
    public void setGameView(GameView gameView) {
        if (this.gameView != null) {
            remove(this.gameView);
        }
        this.gameView = gameView;
        add(gameView);
        revalidate();
        repaint();
    }


}
