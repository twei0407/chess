package game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import piece.*;

public class Board
{
    public final int SIZE;
    public final King WHITE_KING;
    public final King BLACK_KING;
    private Piece[][] squares;
    private ArrayList<Piece> removed;
    private HashMap<Piece, HashSet<int[]>> evaluated;
    private ChangeListener view;

    private Piece selectedPiece = null;

    public Board(int SIZE, Piece[][] squares, King WHITE_KING, King BLACK_KING)
    {
        this.SIZE = SIZE;
        this.squares = squares;
        this.WHITE_KING = WHITE_KING;
        this.BLACK_KING = BLACK_KING;
        this.removed = new ArrayList<>();
        this.evaluated = new HashMap<>();
    }

    public Piece[][] getSquares() {
        return this.squares;
    }

    public Piece getSquare(int row, int col) {
        if (!(row < 0 || row > this.SIZE-1 || col < 0 || col > this.SIZE-1))
            return this.squares[row][col];
        return null;
    }

    public ArrayList<Piece> getRemoved() {
        return removed;
    }

    public Piece getSelectedPiece() {
        if (this.selectedPiece != null)
            try {
                return (Piece) this.selectedPiece.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void setSelectedPiece(Piece piece) {
        this.selectedPiece = piece;
    }

    public HashSet<int[]> getEvaluated(Piece piece) {
        if (piece == null)
            return new HashSet<>();
        return this.evaluated.get(piece);
    }

    /**
     * Allows the player to make a move on the board and handles the removal of any piece
     * @param piece
     * @param newPosition
     */
    public void move(Piece piece, int[] newPosition)
    {
        int row = newPosition[0];
        int col = newPosition[1];
        
        if (piece instanceof Pawn && col != piece.getPosition()[1])  // edge case: en passant removal
            switch (piece.getColor()) {
                case WHITE:
                    if (this.squares[row][col] == null) // Check to ensure opponent's pawn did move for en passant
                        this.squares[row+1][col] = null; break;
                case BLACK:
                    if (this.squares[row][col] == null) // Check to ensure opponent's pawn did move for en passant
                        this.squares[row-1][col] = null; break;
            }
        /* add additional valid move to opponent's pawn(s) for en passant */
        if (piece instanceof Pawn && Math.abs(row - piece.getPosition()[0]) == 2)
        {
            if (col-1 > 0 && this.squares[row][col-1] instanceof Pawn && this.squares[row][col-1].getColor() != piece.getColor())      // add to left pawn
                switch (piece.getColor()) {
                    case WHITE:
                        ((Pawn)this.squares[row][col-1]).addMove(new int[]{row+1, col}); break;
                    case BLACK:
                        ((Pawn)this.squares[row][col-1]).addMove(new int[]{row-1, col}); break;
                }
            if (col+1 < this.SIZE && this.squares[row][col+1] instanceof Pawn && this.squares[row][col+1].getColor() != piece.getColor())      // add to right pawn
                switch (piece.getColor()) {
                    case WHITE:
                        ((Pawn)this.squares[row][col+1]).addMove(new int[]{row+1, col}); break;
                    case BLACK:
                        ((Pawn)this.squares[row][col+1]).addMove(new int[]{row-1, col}); break;
                }
        }
        /* edge case: castling move */
        if (piece instanceof King && piece.getPosition()[1] - col == 2)   // set Rook's position
        {
            Piece r1 = this.squares[row][0];
            this.squares[row][0] = null;
            r1.setPosition(new int[]{row, col+1});
            this.squares[row][col+1] = r1;
        }
        else if (piece instanceof King && piece.getPosition()[1] - col == -2) // set Rook's position
        {
            Piece r1 = this.squares[row][this.SIZE-1];
            this.squares[row][this.SIZE-1] = null;
            r1.setPosition(new int[]{row, col-1});
            this.squares[row][col-1] = r1;
        }
        if (this.squares[row][col] != null && this.squares[row][col].getColor() != piece.getColor()) {  // general case: remove opponent's piece
            remove(this.squares[row][col]);
            removed.add(this.squares[row][col]);
        }
        /* Move the selected Piece */
        this.squares[piece.getPosition()[0]][piece.getPosition()[1]] = null;
        piece.setPosition(newPosition);
        this.squares[row][col] = piece;
        this.view.stateChanged(new ChangeEvent(this));
        this.evaluated.clear();
    }

    /**
     * Evaluate all possible moves of a given chess piece when player picks up a piece
     * @param piece the piece to be evaluated
     * @return Board consisting of highlighted tiles of where the given chess piece can move to
     */
    public HashSet<int[]> evaluate(Piece piece)
    {
        if (piece == null)
            return new HashSet<>();
        if (!this.evaluated.containsKey(piece))
            this.evaluated.put(piece, new HashSet<int[]>(Arrays.asList(piece.getValidMoves(this))));
        this.view.stateChanged(new ChangeEvent(this));
        return this.evaluated.get(piece);
    }

    public HashSet<Move> evaluateMoves(Piece piece) {
        HashSet<Move> moves = new HashSet<>();
        for (int[] move: this.evaluate(piece)) {
            moves.add(new Move(move[0], move[1]));
        }
        return moves;
    }

    public void attach(ChangeListener view) {
        this.view = view;
    }

    /**
     * Remove a piece from the board
     * @param piece the piece to be removed
     */
    private void remove(Piece piece)
    {
        if (piece == null)
            return;
        int row = piece.getPosition()[0];
        int col = piece.getPosition()[1];
        this.squares[row][col] = null;
    }
}
