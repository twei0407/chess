package piece;

import game.*;

public class Queen extends Piece
{
    public Queen(int[] position, Color color)
    {
        super(position, color);
    }

    public int[][] getValidMoves(Board board)
    {
        /* Delegate valid move selection to rook and bishop */
        int[][] a = (new Rook(this.position, this.color)).getValidMoves(board);
        int[][] b = (new Bishop(this.position, this.color)).getValidMoves(board);
        int[][] result = new int[a.length+b.length][];
        for (int i = 0; i < a.length; i++)
            result[i] = a[i];
        for (int i = a.length; i < a.length+b.length; i++)
            result[i] = b[i-a.length];
        // There should probably be a better way to do this without having to set the original location back to Queen
        board.getSquares()[this.position[0]][this.position[1]] = this;
        return result;
    }
}
