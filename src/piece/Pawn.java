package piece;

import java.util.ArrayList;

import game.Board;

public class Pawn extends Piece
{
    private ArrayList<int[]> validMoves;
    private boolean moved = false;

    public Pawn(int[] position, Color color)
    {
        super(position, color);
        validMoves = new ArrayList<>();
    }

    @Override
    public int[][] getValidMoves(Board board)
    {
        int row = getPosition()[0];
        int col = getPosition()[1];

        switch (this.getColor())
        {
            case WHITE:
                if (board.getSquare(row-1, col) == null)
                    addMove(board, validMoves, row-1, col);
                if (board.getSquare(row-2, col) == null && board.getSquare(row-1, col) == null && !moved)
                    addMove(board, validMoves, row-2, col);
                if (board.getSquare(row-1, col+1) != null && board.getSquare(row-1, col+1).color != Color.WHITE)
                    addMove(board, validMoves, row-1, col+1);
                if (board.getSquare(row-1, col-1) != null && board.getSquare(row-1, col-1).color != Color.WHITE)
                    addMove(board, validMoves, row-1, col-1);
                break;
            case BLACK:
                if (board.getSquare(row+1, col) == null)
                    addMove(board, validMoves, row+1, col);
                if (board.getSquare(row+2, col) == null && board.getSquare(row+1, col) == null && !moved)
                    addMove(board, validMoves, row+2, col);
                if (board.getSquare(row+1, col+1) != null && board.getSquare(row+1, col+1).color != Color.BLACK)
                    addMove(board, validMoves, row+1, col+1);
                if (board.getSquare(row+1, col-1) != null && board.getSquare(row+1, col-1).color != Color.BLACK)
                    addMove(board, validMoves, row+1, col-1);
                break;
        }
        int[][] result = new int[validMoves.size()][];
        for (int i = 0; i < result.length; i++)
            result[i] = validMoves.get(i);
        validMoves.clear();
        return result;
    }

    public void addMove(int[] position) {
        this.validMoves.add(position);
    }

    @Override
    public void setPosition(int[] position) {
        super.setPosition(position);
        this.moved = true;
    }
}