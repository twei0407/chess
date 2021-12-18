package piece;

import java.util.ArrayList;

import game.*;

public class Bishop extends Piece 
{
    public Bishop(int[] position, Color color) {
        super(position, color);
    }

    @Override
    public int[][] getValidMoves(Board board)
    {
        ArrayList<int[]> validMoves = new ArrayList<int[]>();
        int[] position = getPosition();
        int row = position[0];
        int col = position[1];
        /* traverse the board starting from the piece's position */
        final int _S = board.SIZE;
        for (int i = row-1,j = col-1; i > -1 && j > -1; i--, j--)   // top-left
            if (addMove(board, validMoves, i, j)) { break; }
        for (int i = row-1,j = col+1; i > -1 && j < _S; i--, j++)   // top-right
            if (addMove(board, validMoves, i, j)) { break; }
        for (int i = row+1,j = col-1; i < _S && j > -1; i++, j--)   // bottom-left
            if (addMove(board, validMoves, i, j)) { break; }
        for (int i = row+1,j = col+1; i < _S && j < _S; i++, j++)   // bottom-right
            if (addMove(board, validMoves, i, j)) { break; }

        int[][] result = new int[validMoves.size()][];
        for (int i = 0; i < result.length; i++)
            result[i] = validMoves.get(i);
        return result;
    }
    
}
