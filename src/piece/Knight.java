package piece;

import java.util.ArrayList;

import game.Board;

public class Knight extends Piece
{
    public Knight(int[] position, Color color) {
        super(position, color);
    }

    @Override
    public int[][] getValidMoves(Board board) {
        ArrayList<int[]> validMoves = new ArrayList<int[]>();
        int[] position = getPosition();
        int row = position[0];
        int col = position[1];

        addMove(board, validMoves, row-2, col-1);
        addMove(board, validMoves, row-1, col-2);
        addMove(board, validMoves, row-2, col+1);
        addMove(board, validMoves, row-1, col+2);
        addMove(board, validMoves, row+2, col-1);
        addMove(board, validMoves, row+1, col-2);
        addMove(board, validMoves, row+2, col+1);
        addMove(board, validMoves, row+1, col+2);

        int[][] result = new int[validMoves.size()][];
        for (int i = 0; i < result.length; i++)
            result[i] = validMoves.get(i);
        return result;
    }
    
}
