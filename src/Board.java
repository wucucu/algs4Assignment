import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int N;
    // private char[] block;
    private int zeroBlockPosition;
    private int[] block;
    private int amountOfBlocksOutOfPlace;
    private int sumOfManhattanDistance;

    public Board(int[][] tiles) {
        N = tiles.length;
        block = new int[N * N];

        int zP = 0;
        int k = 0;
        for (int[] row : tiles) {
            for (int number : row) {
                block[k] = number;
                if (number == 0)
                    zP = k;
                k++;
            }
        }

        zeroBlockPosition = zP;
        amountOfBlocksOutOfPlace = initHamming();
        sumOfManhattanDistance = initManhattan();
    }

    private Board(Board that) {
        this.N = that.N;
        this.block = new int[this.N * this.N];
        for (int i = 0; i < block.length; i++) {
            this.block[i] = that.block[i];
        }
        this.zeroBlockPosition = that.zeroBlockPosition;
        this.amountOfBlocksOutOfPlace = that.amountOfBlocksOutOfPlace;
        this.sumOfManhattanDistance = that.sumOfManhattanDistance;
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // 1 if number not in the goal positon
    // 0 otherwise
    private int isOutOfPlace(int index, int number) {
        if (number - 1 != index)
            return 1;
        return 0;
    }

    private int initHamming() {
        int h = 0;
        for (int i = 0; i < block.length; i++) {
            int x = block[i];
            if (x == 0)
                continue;
            h = h + isOutOfPlace(i, x);
        }
        return h;
    }

    // number of blocks out of place
    public int hamming() {
        return amountOfBlocksOutOfPlace;
    }

    // treat the top left grid as (0, 0), the bottom right grid as (N-1, N-1)
    private int[] currentIndex2DPosition(int index) {
        int[] p = new int[2];
        p[0] = index / N;
        p[1] = index % N;
        return p;
    }

    private int[] goal2DPosition(int number) {
        int[] p = new int[2];
        p[0] = (number - 1) / N;
        p[1] = (number - 1) % N;
        return p;
    }

    // sum of the vertical and horizontal distance from the blocks to their goal
    // positions
    private int manhattanDistance(int[] p1, int[] p2) {
        return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
    }

    private int initManhattan() {
        int m = 0;
        for (int i = 0; i < block.length; i++) {
            int x = block[i];
            if (x == 0)
                continue;
            m = m + manhattanDistance(currentIndex2DPosition(i), goal2DPosition(x));
        }
        return m;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return sumOfManhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() == 0)
            return true;
        return false;
    }

    private Board swap(int i, int j) {
        Board newBoard = new Board(this);

        newBoard.block[i] = this.block[j];
        newBoard.block[j] = this.block[i];

        int z = this.zeroBlockPosition;
        int h = this.amountOfBlocksOutOfPlace;
        int m = this.sumOfManhattanDistance;

        if (newBoard.block[i] == 0) {
            z = i;
            h = h - isOutOfPlace(i, this.block[i]);
            h = h + isOutOfPlace(j, newBoard.block[j]);

            m = m - manhattanDistance(currentIndex2DPosition(i), goal2DPosition(this.block[i]));
            m = m + manhattanDistance(currentIndex2DPosition(j), goal2DPosition(newBoard.block[j]));

        } else if (newBoard.block[j] == 0) {
            z = j;
            h = h - isOutOfPlace(j, this.block[j]);
            h = h + isOutOfPlace(i, newBoard.block[i]);

            m = m - manhattanDistance(currentIndex2DPosition(j), goal2DPosition(this.block[j]));
            m = m + manhattanDistance(currentIndex2DPosition(i), goal2DPosition(newBoard.block[i]));

        } else {
            h = h - isOutOfPlace(i, this.block[i]);
            h = h - isOutOfPlace(j, this.block[j]);
            h = h + isOutOfPlace(i, newBoard.block[i]);
            h = h + isOutOfPlace(j, newBoard.block[j]);

            m = m - manhattanDistance(currentIndex2DPosition(i), goal2DPosition(this.block[i]));
            m = m - manhattanDistance(currentIndex2DPosition(j), goal2DPosition(this.block[j]));
            m = m + manhattanDistance(currentIndex2DPosition(i), goal2DPosition(newBoard.block[i]));
            m = m + manhattanDistance(currentIndex2DPosition(j), goal2DPosition(newBoard.block[j]));
        }

        newBoard.zeroBlockPosition = z;
        newBoard.amountOfBlocksOutOfPlace = h;
        newBoard.sumOfManhattanDistance = m;

        return newBoard;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (N == 0)
            return null;

        int s = 0;
        if (s == zeroBlockPosition)
            s++;
        int t = s + 1;
        if (t == zeroBlockPosition)
            t++;

        return this.swap(s, t);
    }

    // does this board equal y
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        if (that.N != this.N)
            return false;
        if (that.amountOfBlocksOutOfPlace != this.amountOfBlocksOutOfPlace)
            return false;
        if (that.sumOfManhattanDistance != this.sumOfManhattanDistance)
            return false;
        for (int i = 0; i < this.block.length; i++) {
            if (that.block[i] != this.block[i])
                return false;
        }
        return true;
    }

    // all neighbouring boards
    public Iterable<Board> neighbors() {
        Queue<Board> thisNeighbors = new Queue<Board>();

        int p = zeroBlockPosition;

        // blank square not on the first row, swap blank squre with upper block
        if (p / N > 0) {
            thisNeighbors.enqueue(this.swap(p, p - N));
        }
        // blank square not on the first row, swap blank squre with beneath
        // block
        if (p / N < N - 1) {
            thisNeighbors.enqueue(this.swap(p, p + N));
        }
        // blank square not on the first column, swap blank squre with left
        // block
        if (p % N != 0) {
            thisNeighbors.enqueue(this.swap(p, p - 1));
        }
        // blank square not on the last column, swap blank squre with right
        // block
        if (p % N != N - 1) {
            thisNeighbors.enqueue(this.swap(p, p + 1));
        }

        return thisNeighbors;
    }

    // string representation of this board (in the output format specified
    // below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(N + "\n");
        int k = 0;
        for (int i : block) {
            sb.append(String.format("%2d ", i));
            k++;
            if (k % N == 0)
                sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.hamming() + " " + initial.manhattan());
        StdOut.println(initial);

        for (Board board : initial.neighbors()) {
            StdOut.println(board.hamming() + " " + board.manhattan());
            StdOut.println(board);
        }

        StdOut.println(initial.twin().hamming() + " " + initial.twin().manhattan());
        StdOut.println(initial.twin());

        StdOut.println(new Board(initial).twin().equals(initial));
    }

}
