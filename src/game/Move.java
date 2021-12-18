package game;

/**
 * Solely exists due to HashSet<int[]> bad
 */
public class Move {
    public final int ROW;
    public final int COL;

    public Move(int ROW, int COL) {
        this.ROW = ROW;
        this.COL = COL;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Move))
            return false;
        Move that = (Move) obj;
        return this.ROW == that.ROW && this.COL == that.COL;
    }

    @Override
    public int hashCode() {
        return this.ROW*10 + this.COL;
    }
}
