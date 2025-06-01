package view;

import model.ChessModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private ChessModel model;
    private Image[] images;
    private String[] codes = {"bp", "br", "bn", "bb", "bq", "bk", "wp", "wr", "wn", "wb", "wq", "wk"};
    private Color white = new Color(51, 74, 75);
    private Color black = new Color(59, 4, 30);
    private List<int[]> validMoves = new ArrayList<>();
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameView(ChessModel model) {
        this.model = model;
        this.images = new Image[codes.length];
        setPreferredSize(new Dimension(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE));
        loadImages();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;

                if (model.hasPendingPromotion()) {
                    showPromotionMenu(e.getX(), e.getY());
                    return;
                }

                if (model.selectPiece(row, col)) {
                    selectedRow = row;
                    selectedCol = col;
                    validMoves = model.getValidMovesForPiece(row, col);
                } else {
                    if (model.selectTargetSquare(row, col)) {
                        if (model.hasPendingPromotion()) {
                            selectedRow = row;
                            selectedCol = col;
                        } else {
                            selectedRow = -1;
                            selectedCol = -1;
                        }
                    } else {
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                    validMoves.clear();
                }
                repaint();
            }
        });
    }

    private void showPromotionMenu(int x, int y) {
        JPopupMenu menu = new JPopupMenu();
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        for (String opt : options) {
            JMenuItem item = new JMenuItem(opt);
            item.addActionListener(e -> {
                model.promotePawn(opt);
                selectedRow = -1;
                selectedCol = -1;
                repaint();
            });
            menu.add(item);
        }
        menu.show(this, x, y);
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
                boolean color_type = (row + col) % 2 == 0;
                g2.setColor(color_type ? white : black);
                g2.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                String code = model.getPieceCode(row, col);
                int index = indexOfCode(code);
                if (index >= 0 && images[index] != null) {
                    g2.drawImage(images[index], col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        // Destacar movimentos válidos
        g2.setColor(new Color(255, 255, 0, 128));
        for (int[] move : validMoves) {
            g2.fillRect(move[1] * TILE_SIZE, move[0] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private int indexOfCode(String code) {
        if (code == null) {
            return -1;
        }
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(code)) {
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
