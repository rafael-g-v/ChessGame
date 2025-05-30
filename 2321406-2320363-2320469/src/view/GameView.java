package view;

import model.ChessModel;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;


public class GameView extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private ChessModel model; // removido 'final'
    private final Map<String, BufferedImage> pieceImages;

    public GameView(ChessModel model) {
        this.model = model;
        this.pieceImages = new HashMap<>();
        setPreferredSize(new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE));
        carregarImagens();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;

                // Primeiro tenta selecionar a peça, se não conseguir, tenta movimentar
                if (!model.selectPiece(row, col)) {
                    model.selectTargetSquare(row, col);
                }
                repaint(); // Atualiza a tela depois de cada clique
            }
        });
    }

    private void carregarImagens() {
        String[] nomes = {"bp", "br", "bn", "bb", "bq", "bk", "wp", "wr", "wn", "wb", "wq", "wk"};
        for (String nome : nomes) {
            try {
                File file = new File("resources/images/" + nome + ".png"); // caminho relativo ao diretório raiz do projeto
                if (!file.exists()) {
                    System.err.println("Arquivo não encontrado: " + file.getAbsolutePath());
                    continue;
                }

                pieceImages.put(nome, ImageIO.read(file));
                System.out.println("Imagem carregada: " + file.getPath());

            } catch (IOException e) {
                System.err.println("Erro ao carregar imagem: " + nome);
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Desenha o tabuleiro
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean claro = (row + col) % 2 == 0;
                g2.setColor(claro ? Color.LIGHT_GRAY : Color.DARK_GRAY);
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Desenha as peças
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                String code = model.getPieceCode(row, col);
                if (code != null) {
                    BufferedImage img = pieceImages.get(code);
                    if (img != null) {
                        g2.drawImage(img, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }

    // Método para atualizar o modelo após reset
    public void setModel(ChessModel model) {
        this.model = model;
        repaint();
    }
}
