package game;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.util.HashSet;

import piece.*;

public class Game
{
    final static int WIDTH = 1250;
    final static int HEIGHT = 1050;
    final static int SIZE = 8;

    static JFrame frame = new JFrame();
    static Piece[][] squares = new Piece[SIZE][SIZE];
    static Color currentColor = Color.WHITE;
    static Board board;
    static BoardView view;
    public static void main(String[] args) throws Exception
    {

        // Instantiate board pieces using Reflection
        String[] layout = new String[]{"piece.Rook", "piece.Knight", "piece.Bishop", "piece.Queen", "piece.King", "piece.Bishop", "piece.Knight", "piece.Rook"};
        for (int i = 0; i < SIZE; i++)
        {
            Class<?> c = Class.forName(layout[i]);
            // c.getConstructors()[0].getParameterTypes()  => ALTERNATE WAY OF GETTING PARAM TYPES
            squares[0][i] = (Piece) c.getDeclaredConstructor(new Class[]{int[].class, Color.class}).newInstance(new Object[]{new int[]{0, i}, Color.BLACK});
            squares[1][i] = new Pawn(new int[]{1, i}, Color.BLACK);
            squares[SIZE-2][i] = new Pawn(new int[]{SIZE-2, i}, Color.WHITE);
            squares[SIZE-1][i] = (Piece) c.getDeclaredConstructor(new Class[]{int[].class, Color.class}).newInstance(new Object[]{new int[]{SIZE-1, i}, Color.WHITE});
        }

        board = new Board(SIZE, squares, (King)squares[SIZE-1][4], (King)squares[0][4]);
        view = new BoardView(board);

        board.attach(view);

        frame.setSize(WIDTH, HEIGHT);
        frame.setLayout(new FlowLayout());

        frame.add(view);

        frame.setTitle("Chess");
        frame.pack();
        frame.getContentPane().setBackground(new java.awt.Color(133, 62, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // Game loop
        loop(null, new HashSet<>());
    }

    public static void loop(Piece piece, HashSet<Move> validMoves)
    {
        /* Handle Checkmate and Stalemate */
        boolean breakLoop = false;
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (squares[i][j]!=null && squares[i][j].getColor()==currentColor && board.evaluate(squares[i][j]).size()!=0) {
                    breakLoop = true;
                    break;
                }
            }
            if (breakLoop)
                break;
            if (i == SIZE-1)
            {
                // Handle Stalemate
                if (currentColor==Color.BLACK && !board.BLACK_KING.inCheck(board) || currentColor==Color.WHITE && !board.WHITE_KING.inCheck(board)) {
                    JOptionPane.showMessageDialog(null, "STALEMATE", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                currentColor = currentColor==Color.BLACK ? Color.WHITE : Color.BLACK;
                JOptionPane.showMessageDialog(null, currentColor.toString() + " WON", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        // Add JButton as the Controller to modify and update the state of the Board
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                JButton button = new JButton();
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setSize(100, 100);

                if (!validMoves.contains(new Move(i,j)) && squares[i][j]==null)
                    button.setEnabled(false);
                else if (!validMoves.contains(new Move(i,j)) && squares[i][j]!=null && squares[i][j].getColor()!=currentColor)  // Ensures only current player can move
                    button.setEnabled(false);
                else
                {
                    final int row = i;
                    final int col = j;

                    if (validMoves.contains(new Move(row,col)))
                    {
                        button.addActionListener(new ActionListener() { // Add buttons corresponding to move selection
                            @Override
                            public void actionPerformed(ActionEvent e){
                                view.removeAll();
                                board.setSelectedPiece(null);
                                board.move(piece, new int[]{row, col});
                                if (piece instanceof Pawn && (row==0 || row==SIZE-1))
                                {
                                    String[] values = {"Queen", "Rook", "Bishop", "Knight"};
                                    String selected = (String) JOptionPane.showInputDialog(null, "Pawn Promotion", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "Queen");
                                    if (selected == null)
                                        return;
                                    switch(selected) {
                                        case "Queen":
                                            squares[row][col] = new Queen(piece.getPosition(), piece.getColor());
                                            break;
                                        case "Rook":
                                            squares[row][col] = new Rook(piece.getPosition(), piece.getColor());
                                            break;
                                        case "Bishop":
                                            squares[row][col] = new Bishop(piece.getPosition(), piece.getColor());
                                            break;
                                        case "Knight":
                                            squares[row][col] = new Knight(piece.getPosition(), piece.getColor());
                                            break;
                                    }
                                }
                                currentColor = currentColor==Color.BLACK ? Color.WHITE : Color.BLACK;
                                loop(null, new HashSet<>());
                            }
                        });
                    }
                    else
                    {
                        button.addActionListener(new ActionListener() { // Add buttons corresponding to piece selection
                            @Override
                            public void actionPerformed(ActionEvent e){
                                view.removeAll();
                                HashSet<Move> validMoves = new HashSet<>();
                                board.setSelectedPiece(squares[row][col]);
                                for (int[] move: board.evaluate(squares[row][col]))
                                    validMoves.add(new Move(move[0], move[1]));
                                loop(squares[row][col], validMoves);
                            }
                        });
                    }
                }
                view.add(button);
            }
        }
        frame.revalidate();
    }
}
