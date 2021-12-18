package piece;

import java.util.ArrayList;

import game.Board;

public class Rook extends Piece
{
    private boolean moved = false;

    public Rook(int[] position, Color color) {
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
        for (int i = row - 1; i > -1; i--)                      // top
            if (addMove(board, validMoves, i, col)) { break; }
        for (int i = col - 1; i > -1; i--)                      // left
            if (addMove(board, validMoves, row, i)) { break; }
        for (int i = col + 1; i < board.SIZE; i++)              // right
            if (addMove(board, validMoves, row, i)) { break; }
        for (int i = row + 1; i < board.SIZE; i++)              // bottom
            if (addMove(board, validMoves, i, col)) { break; }

        int[][] result = new int[validMoves.size()][];
        for (int i = 0; i < result.length; i++)
            result[i] = validMoves.get(i);
        return result;
    }

    public boolean isMoved() {
        return this.moved;
    }

    @Override
    public void setPosition(int[] position) {
        super.setPosition(position);
        this.moved = true;
    }
}
