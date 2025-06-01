package view;

import controller.GameController;
import model.ChessModel;

import javax.swing.*;

/**
 * Classe principal da interface gráfica que representa a janela do jogo de xadrez.
 * Utiliza o padrão MVC: interage com o modelo (ChessModel) e o controlador (GameController).
 */
public class ConsoleView extends JFrame {
    private ChessModel model; // Referência para o modelo (estado do jogo)
    private GameView gameView; // Painel onde o tabuleiro será desenhado
    private GameController controller; // Controlador responsável pela lógica do jogo
    private JLabel turnoLabel; // Rótulo que indica o turno atual (brancas ou pretas)

    /**
     * Construtor que inicializa os componentes da interface gráfica.
     * Define título, comportamento ao fechar, tamanho da janela e cria os objetos MVC.
     */
    public ConsoleView() {
        super("Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 700);

        model = ChessModel.getInstance();  // pega a instancia do modelo
        controller = new GameController(model); // Cria o controlador com referência ao modelo
        controller.setConsoleView(this); // Passa referência da view ao controlador
        gameView = new GameView(model); // Inicializa o painel de visualização do jogo
        controller.setGameView(gameView);  
        gameView.setController(controller); 

        setJMenuBar(createMenuBar());  // Cria e associa a barra de menus
        add(gameView);  // Adiciona o painel do jogo à janela

        updateTurn();  // Atualiza a indicação do turno
        setVisible(true); // Torna a janela visível
    }

    /**
     * Gerencia as telas do tabuleiro e a de inicio
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Jogo");

        JMenuItem newGame = new JMenuItem("Nova Partida");
        newGame.addActionListener(e -> restartGame());  // Reinicia o jogo ao ser clicado

        JMenuItem carregarPartida = new JMenuItem("Carregar Partida");
        carregarPartida.setEnabled(false); // Placeholder para funcionalidades futuras

        gameMenu.add(newGame);
        gameMenu.add(carregarPartida);
        menuBar.add(gameMenu);

        turnoLabel = new JLabel();  // Rótulo que mostrará o turno atual
        menuBar.add(turnoLabel);

        return menuBar;
    }

    /**
     * Reinicializa o modelo, controlador e painel do jogo para começar nova partida.
     */
    private void restartGame() {
        ChessModel.resetInstance();               
        model = ChessModel.getInstance(); // Reseta e pega nova instância do modelo
        gameView.setModel(model);   // Atualiza o modelo no painel
        controller = new GameController(model);   // Cria novo controlador com novo modelo
        controller.setConsoleView(this);
        controller.setGameView(gameView);
        gameView.setController(controller);
        updateTurn();   // Atualiza o turno após reinício
        gameView.repaint();                       
    }

    // atualiza a label do turno
    public void updateTurn() {
        String cor = model.isWhiteTurn() ? "brancas" : "pretas";
        turnoLabel.setText("Turno de " + cor);
    }
}
