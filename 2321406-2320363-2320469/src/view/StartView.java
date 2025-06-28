package view;

import javax.swing.*;
import java.awt.*;
import model.ChessModel;
import controller.GameController;

public class StartView extends JFrame {
    public StartView() {
    	// Padronizando formato da janela
        super("Início - Jogo de Xadrez");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton newGameBtn = new JButton("Nova Partida");
        JButton loadFENFileBtn = new JButton("Carregar Partida de Arquivo");

        // Nova partida (talvez mudar pra tirar esse action listener)
        newGameBtn.addActionListener(e -> {
            dispose();
            ChessModel.resetInstance();
            new ConsoleView(ChessModel.getInstance());
        });

        // Carregar FEN de arquivo .txt
        loadFENFileBtn.addActionListener(e -> {
            GameController tempController = new GameController(ChessModel.getInstance());
            ChessModel loadedModel = tempController.carregarPartidaViaArquivo(this);

            if (loadedModel != null) {
                dispose();
                new ConsoleView(loadedModel);
            }
        });

        panel.add(newGameBtn);
        panel.add(loadFENFileBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
