package view;

import controller.GameController;
import model.ChessModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsoleView extends JFrame {
    private ChessModel model;
    private GameView gameView;
    private GameController controller;

    public ConsoleView() {
        super("Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 700);
        setLocationRelativeTo(null);

        model = ChessModel.getInstance(); 
        controller = new GameController(model);
        gameView = new GameView(model);
        controller.setGameView(gameView); 

        setJMenuBar(createMenuBar());
        add(gameView);

        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu jogoMenu = new JMenu("Jogo");

        JMenuItem novaPartida = new JMenuItem("Nova Partida");
        novaPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChessModel.resetInstance(); // reinicia o modelo
                model = ChessModel.getInstance(); // recria nova instância
                gameView.setModel(model); // atualiza model na view
                gameView.repaint();
            }
        });

        JMenuItem carregarPartida = new JMenuItem("Carregar Partida...");
        carregarPartida.setEnabled(false); // só será usado na 4ª iteração

        jogoMenu.add(novaPartida);
        jogoMenu.add(carregarPartida);
        menuBar.add(jogoMenu);

        return menuBar;
    }
}
