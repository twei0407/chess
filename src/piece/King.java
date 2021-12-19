package piece;

import java.util.ArrayList;

import game.Board;

public class King extends Piece
{
    private boolean moved = false;

    public King(int[] position, Color color) {
        super(position, color);       
    }

    @Override
    public int[][] getValidMoves(Board board)
    {
        ArrayList<int[]> validMoves = new ArrayList<int[]>();
        int[] position = getPosition();
        int row = position[0];
        int col = position[1];
        Piece r1, n1, b1, q1;
        /* check and add surrounding squares */
        addMove(board, validMoves, row-1, col-1);
        addMove(board, validMoves, row-1, col  );
        addMove(board, validMoves, row-1, col+1);
        addMove(board, validMoves, row,   col-1);
        addMove(board, validMoves, row,   col+1);
        addMove(board, validMoves, row+1, col-1);
        addMove(board, validMoves, row+1, col  );
        addMove(board, validMoves, row+1, col+1);
        switch (this.color)  // castling
        {
            case WHITE:
                if (!this.moved && !this.inCheck(board))
                {
                    r1 = board.getSquares()[board.SIZE-1][0];
                    n1 = board.getSquares()[board.SIZE-1][1];
                    b1 = board.getSquares()[board.SIZE-1][2];
                    q1 = board.getSquares()[board.SIZE-1][3];
                    if (r1 instanceof Rook && !((Rook)r1).isMoved() && n1 == null && b1 == null && q1 == null && !new King(new int[]{board.SIZE-1, 3}, this.color).inCheck(board))
                        addMove(board, validMoves, board.SIZE-1, 2);
                    r1 = board.getSquares()[board.SIZE-1][board.SIZE-1];
                    n1 = board.getSquares()[board.SIZE-1][board.SIZE-2];
                    b1 = board.getSquares()[board.SIZE-1][board.SIZE-3];
                    if (r1 instanceof Rook && !((Rook)r1).isMoved() && n1 == null && b1 == null && !new King(new int[]{board.SIZE-1, board.SIZE-3}, this.color).inCheck(board))
                        addMove(board, validMoves, board.SIZE-1, board.SIZE-2);
                }
                break;
            case BLACK:
                if (!this.moved && !this.inCheck(board))
                {
                    r1 = board.getSquares()[0][0];
                    n1 = board.getSquares()[0][1];
                    b1 = board.getSquares()[0][2];
                    q1 = board.getSquares()[0][3];
                    if (r1 instanceof Rook && !((Rook)r1).isMoved() && n1 == null && b1 == null && q1 == null && !new King(new int[]{0, 3}, this.color).inCheck(board))
                        addMove(board, validMoves, 0, 2);
                    r1 = board.getSquares()[0][board.SIZE-1];
                    n1 = board.getSquares()[0][board.SIZE-2];
                    b1 = board.getSquares()[0][board.SIZE-3];
                    if (r1 instanceof Rook && !((Rook)r1).isMoved() && n1 == null && b1 == null && !new King(new int[]{0, board.SIZE-3}, this.color).inCheck(board))
                        addMove(board, validMoves, 0, board.SIZE-2);
                }
               break;
        }

        int[][] result = new int[validMoves.size()][];
        for (int i = 0; i < result.length; i++)
            result[i] = validMoves.get(i);
        validMoves.clear();
        return result;
    }

    /**
     * Checks if the King is in check. King iterates through the board as if it were all chess pieces
     * @return true or false
     */
    public boolean inCheck(Board board)
    {
        int[] position = getPosition();
        int row = position[0];
        int col = position[1];

        Piece p;
        final int _S = board.SIZE;
        /* Bishop and Queen Moves */
        for (int i = row-1,j = col-1; i > -1 && j > -1; i--, j--)   // top-left
        {
            p = board.getSquares()[i][j];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Bishop))
                break;
            else if (p instanceof Queen || p instanceof Bishop)
                return true;
        }
        for (int i = row-1,j = col+1; i > -1 && j < _S; i--, j++)   // top-right
        {
            p = board.getSquares()[i][j];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Bishop))
                break;
            else if (p instanceof Queen || p instanceof Bishop)
                return true;
        }
        for (int i = row+1,j = col-1; i < _S && j > -1; i++, j--)   // bottom-left
        {
            p = board.getSquares()[i][j];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Bishop))
                break;
            else if (p instanceof Queen || p instanceof Bishop)
                return true;
        }
        for (int i = row+1,j = col+1; i < _S && j < _S; i++, j++)   // bottom-right
        {
            p = board.getSquares()[i][j];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Bishop))
                break;
            else if (p instanceof Queen || p instanceof Bishop)
                return true;
        }
        /* Rook and Queen Moves */
        for (int i = row - 1; i > -1; i--)                      // top
        {
            p = board.getSquares()[i][col];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Rook))
                break;
            else if ((p instanceof Queen || p instanceof Rook))
                return true;
        }
        for (int i = col - 1; i > -1; i--)                      // left
        {
            p = board.getSquares()[row][i];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Rook))
                break;
            else if ((p instanceof Queen || p instanceof Rook))
                return true;
        }
        for (int i = col + 1; i < board.SIZE; i++)              // right
        {
            p = board.getSquares()[row][i];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Rook))
                break;
            else if ((p instanceof Queen || p instanceof Rook))
                return true;
        }
        for (int i = row + 1; i < board.SIZE; i++)              // bottom
        {
            p = board.getSquares()[i][col];
            if (p == null)
                continue;
            if (p.color == this.color || !(p instanceof Queen || p instanceof Rook))
                break;
            else if ((p instanceof Queen || p instanceof Rook))
                return true;
        }
        /* Knight Moves */
        p = board.getSquare(row-2,col-1);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row-2,col+1);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row-1,col+2);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row-1,col-2);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row+2,col-1);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row+1,col-2);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row+2,col+1);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        p = board.getSquare(row+1,col+2);
        if (p != null && p.color != this.color && p instanceof Knight)
            return true;
        /* Pawn Attack Moves */
        switch(this.color)
        {
            case WHITE:
                p = board.getSquare(row-1,col-1);
                if (p != null && p.color != this.color && p instanceof Pawn)
                    return true;
                p = board.getSquare(row-1,col+1);
                if (p != null && p.color != this.color && p instanceof Pawn)
                    return true;
                break;
            case BLACK:
                p = board.getSquare(row+1,col-1);
                if (p != null && p.color != this.color && p instanceof Pawn)
                    return true;
                p = board.getSquare(row+1,col+1);
                if (p != null && p.color != this.color && p instanceof Pawn)
                    return true;
                break;
        }
        /* Opponent King Moves */
        p = board.getSquare(row-1, col-1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row-1, col  );
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row-1, col+1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row  , col-1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row  , col+1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row+1, col-1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row+1, col  );
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        p = board.getSquare(row+1, col+1);
        if (p != null && p.color != this.color && p instanceof King)
            return true;
        return false;
    }

    @Override
    public void setPosition(int[] position) {
        super.setPosition(position);
        this.moved = true;
    } 
}
