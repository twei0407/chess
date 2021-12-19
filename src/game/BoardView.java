package game;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BoardView extends JPanel implements ChangeListener
{
    private final int width = 800;
    private final int height = 800;
    private Board board;

    public BoardView(Board board)
    {
        this.board = board;
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new GridLayout(board.SIZE, board.SIZE));
        this.setBackground(Color.BLUE);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        /* Paint only the chess board */
        for (int i = 0; i < width; i += width/8)
        {
            g2.setColor(g2.getColor()==Color.BLACK ? Color.WHITE : Color.BLACK);
            for (int j = 0; j < height; j += height/8)
            {
                Rectangle2D rect = new Rectangle2D.Float(i, j, width/8, height/8);
                g2.setColor(g2.getColor()==Color.BLACK ? Color.WHITE : Color.BLACK);
                g2.fill(rect);
                g2.draw(rect);
            }
        }
        /* Paint the chess pieces onto the board */
        for (int i = 0; i < board.SIZE; i++)
        {
            for (int j = 0; j < board.SIZE; j++)
            {
                if (board.getSquare(i, j) != null)
                {
                    String imgPath = "img/" + board.getSquare(i, j).getColor().name().toLowerCase() + '_' + 
                        board.getSquare(i, j).getClass().getName().replaceFirst(board.getSquare(i, j).getClass().getPackageName() + '.', "").toLowerCase() + ".png";
                    try {
                        BufferedImage image = ImageIO.read(getClass().getResource('/' + imgPath));
                        g2.drawImage(image, j*width/8, i*width/8, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /* Paint valid moves if any */
        for (int[] position: board.getEvaluated(board.getSelectedPiece()))
        {
            Ellipse2D ellipse = new Ellipse2D.Float(position[1]*width/8 + 33, position[0]*width/8 + 33, 33, 33);
            g2.setColor(new Color(100, 100, 100, 130));
            g2.fill(ellipse);
            g2.draw(ellipse);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.repaint();
    }
}
