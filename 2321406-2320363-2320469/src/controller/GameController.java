package controller;

import model.ChessModel;

import observer.Observable;
import observer.Observer;

import view.ConsoleView;
import view.GameView;
import view.StartView;

import javax.swing.*;

import java.awt.Component;
import java.io.*;


//Controlador do jogo de xadrez que coordena a comunicacao entre o chessmodel e as views.
public class GameController implements Observer{
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
    
    //Recebe: Observable que originou a notificação
    //Ação: Atualiza UI e verifica fim de jogo
    @Override
    public void notificar(Observable o) {
        SwingUtilities.invokeLater(() -> {
            if (view != null) view.repaint(); // Atualiza tabuleiro
            if (consoleView != null) consoleView.updateTurn();
            checkEndOfGame();
        });
    }


    // Verifica se foi cheque mate, congelamento, ou se o jogo continua
    public void checkEndOfGame() {
        if (model.isCheckMate()) {
            JOptionPane.showMessageDialog(view, "Xeque-mate! O jogador " + (model.isWhiteTurn() ? "branco" : "preto") + " perdeu.");
            // Fecha a janela atual
            if (consoleView != null) {
                consoleView.dispose();
            }

            // Abre a tela inicial
            new StartView();
        } else if (model.isStalelMate()) {
            JOptionPane.showMessageDialog(view, "Empate por congelamento!");
            // Fecha a janela atual
            if (consoleView != null) {
                consoleView.dispose();
            }

            // Abre a tela inicial
            new StartView();
        } else if (consoleView != null) {
            consoleView.updateTurn(); // Atualiza a barra de menu com a cor do próximo turno
        }
    }

    // Faz a promocao do peao recebendo como parametro o tipo da peca que ele ira se transformar
    public void setPawnPromotion(String tipo) {
        model.promotePawn(tipo);
        view.repaint(); // atualiza a peca
        checkEndOfGame();     
    }

    //Reseta o modelo do jogo para o estado inicial, atualiza as referências nas views e notifica componentes 
    public void restartGame() {
        ChessModel.resetInstance();
        this.model = ChessModel.getInstance();

        GameView newGameView = new GameView(model);
        newGameView.setController(this);

        this.setGameView(newGameView);

        if (consoleView != null) {
            consoleView.setModel(model);
            consoleView.setGameView(newGameView);
            consoleView.updateTurn();
        }
    }

    //Recebe: componente pai para a caixa de diálogo
    //Retorna: true se salvou com sucesso, false caso contrário
    public boolean salvarPartida(Component parent) {
        String fen = model.generateFEN();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar partida");

        int escolha = fileChooser.showSaveDialog(parent);
        if (escolha == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Garante a extensão .txt
            if (!selectedFile.getName().toLowerCase().endsWith(".txt")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }

            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                writer.println(fen);
                writer.flush();
                JOptionPane.showMessageDialog(parent, "Partida salva com sucesso!");
                return true;  // Salvamento concluído
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao salvar arquivo: " + ex.getMessage());
            }
        }
        return false;  // Salvamento não ocorreu (cancelou ou deu erro)
    }

    //Recebe: JFrame pai para diálogos
    //Retorna: ChessModel carregado ou null se falhar
    public ChessModel carregarPartidaViaArquivo(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                String fen = reader.readLine();
                reader.close();

                if (fen != null && !fen.trim().isEmpty()) {
                    ChessModel.resetInstance();
                    ChessModel newModel = ChessModel.getInstance();
                    newModel.loadFEN(fen.trim());

                    JOptionPane.showMessageDialog(parent, "Partida carregada com sucesso!");
                    return newModel;  // Retorna o modelo carregado

                } else {
                    JOptionPane.showMessageDialog(parent, "Arquivo FEN vazio ou inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(parent, "Erro ao carregar arquivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;  // Caso não carregue
    }
    
    public void desistir() {
        String vencedor = model.isWhiteTurn() ? "pretas" : "brancas";
        JOptionPane.showMessageDialog(view,
            "Jogador das " + (model.isWhiteTurn() ? "brancas" : "pretas") +
            " desistiu!\nVitória das " + vencedor + ".");

        // Fecha a janela atual
        if (consoleView != null) {
            consoleView.dispose();
        }

        // Abre a tela inicial
        new StartView();
    }
}
