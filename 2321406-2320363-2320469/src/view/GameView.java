package view;

import model.ChessModel;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameView extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private ChessModel model;
    private Image[] images;
    private String[] codes = {"bp", "br", "bn", "bb", "bq", "bk", "wp", "wr", "wn", "wb", "wq", "wk"};
    private Color white = new Color(51, 74, 75); 
    private Color black = new Color(59, 4, 30); 


    public GameView(ChessModel model) {
        this.model = model;
        this.images = new Image[codes.length];
        setPreferredSize(new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE));
        loadImages();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;

                if (model.selectPiece(row, col)) {
                    System.out.println("Casa selecionada: (" + row + ", " + col + ")");
                } else {
                    model.selectTargetSquare(row, col);
                }

                repaint();
            }
        });
    }

    private void loadImages() {
        for (int i = 0; i < codes.length; i++) {
            try {
                images[i] = ImageIO.read(getClass().getResource("/images/" + codes[i] + ".png"));
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Erro ao carregar imagem " + codes[i] + ": " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean claro = (row + col) % 2 == 0;
                g2.setColor(claro ? white : black);
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                String code = model.getPieceCode(row, col);
                int index = indexOfCode(code);
                if (index >= 0 && images[index] != null) {
                    g2.drawImage(images[index], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    private int indexOfCode(String code) {
        if (code == null) 
        	{
        		return -1;
        	}
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(code)) 
            	{
            	return i;
            	}
        }
        return -1;
    }

    public void setModel(ChessModel model) {
        this.model = model;
        repaint();
    }
}
