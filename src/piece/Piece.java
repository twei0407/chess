package piece;
import java.util.ArrayList;
import java.util.Arrays;

import game.*;

public abstract class Piece implements Cloneable
{
    protected int[] position = new int[2];
    protected Color color;

    public Piece(int[] position, Color color) {
        this.position = position;
        this.color = color;
    }

    /**
     * Returns all valid squares the player can move to
     */
    public abstract int[][] getValidMoves(Board board);

    public int[] getPosition() {
        return this.position;
    }

    public Color getColor() {
        return this.color;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    /**
     * Helper function to add position into validMoves for getValidMoves
     * comment: somewhat questionable this is being used in an if statement but then again Java implements add methods 
     *          as a boolean anyways so this isn't a big stretch
     * @return true if needed to break in outer loop
     */
    protected boolean addMove(Board board, ArrayList<int[]> validMoves, int row, int col)
    {
        if (row < 0 || row > board.SIZE-1 || col < 0 || col > board.SIZE-1) // out of board range
            return false;
        int[] oldPosition = new int[]{this.position[0], this.position[1]};
        int oldRow = oldPosition[0];
        int oldCol = oldPosition[1];
        Piece thatPiece = board.getSquares()[row][col];
        
        if (thatPiece!=null && thatPiece.getColor()==this.color) { // collide with own piece
            return true;
        }
        // We should probably create a deep copy of the Board/2D Array instead of modifying the original but that requires work
        board.getSquares()[oldRow][oldCol] = null;
        this.position = new int[]{row, col};
        board.getSquares()[row][col] = this;
        // Check if move will cause the King to be in check
        if ((this.color==Color.WHITE && board.WHITE_KING.inCheck(board)) || (this.color==Color.BLACK && board.BLACK_KING.inCheck(board)))
        {
            board.getSquares()[row][col] = thatPiece;
            this.position = new int[]{oldRow, oldCol};
            board.getSquares()[oldRow][oldCol] = this;
            return false;   // false to allow sequential pieces to protect the king from checks
        }
        board.getSquares()[row][col] = thatPiece;
        this.position = new int[]{oldRow, oldCol};
        board.getSquares()[oldRow][oldCol] = this;
        if (thatPiece!=null && thatPiece.getColor()!=this.color)   // collide with opponent's piece
        {
            validMoves.add(new int[2]);
            validMoves.get(validMoves.size()-1)[0] = row;
            validMoves.get(validMoves.size()-1)[1] = col;
            return true;
        }
        else    // general case
        {
            validMoves.add(new int[2]);
            validMoves.get(validMoves.size()-1)[0] = row;
            validMoves.get(validMoves.size()-1)[1] = col;
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        Piece that = (Piece) o;
        return Arrays.equals(this.position,that.position) && this.color==that.color;
    }

    @Override
    public int hashCode() {
        return color.hashCode() + position[0]*10 + position[1];
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Piece p = (Piece) super.clone();
        p.position = new int[]{p.position[0], p.position[1]};
        return p;
    }
}
