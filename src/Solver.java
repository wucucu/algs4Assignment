import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int move;

        public SearchNode(Board board) {
            this.board = board;
            previous = null;
            move = 0;
        }

        public SearchNode nextNode(Board nextBoard) {
            SearchNode nextNode = new SearchNode(nextBoard);
            nextNode.previous = this;

            nextNode.move = this.move + 1;
            return nextNode;
        }

        public int manhattanPriority() {
            return move + board.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            if (manhattanPriority() > o.manhattanPriority())
                return 1;
            if (manhattanPriority() < o.manhattanPriority())
                return -1;
            if (this.board.manhattan() > o.board.manhattan())
                return 1;
            if (this.board.manhattan() < o.board.manhattan())
                return -1;
            return 0;
        }

    }

    private MinPQ<SearchNode> onHandNodesMinPQ;
    private Stack<Board> solutionStack;
    private boolean solvableFlag;
    private int solutionMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();

        // initialization
        onHandNodesMinPQ = new MinPQ<SearchNode>();
        solutionStack = new Stack<Board>();
        solvableFlag = false;
        solutionMoves = -1;

        // solve process
        onHandNodesMinPQ.insert(new SearchNode(initial));
        onHandNodesMinPQ.insert(new SearchNode(initial.twin()));

        SearchNode currentSearchNode;

        while (true) {
            currentSearchNode = onHandNodesMinPQ.delMin();
            if (currentSearchNode.board.isGoal())
                break;
            Board previousBoard = null;
            if (currentSearchNode.previous != null)
                previousBoard = currentSearchNode.previous.board;

            for (Board board : currentSearchNode.board.neighbors()) {
                if (board.equals(previousBoard))
                    continue;
                onHandNodesMinPQ.insert(currentSearchNode.nextNode(board));
            }

        }

        // determine solvable by checking whether the first node is initial
        // board or not(the twin in the not case)
        SearchNode goalSearchNode = currentSearchNode;

        while (currentSearchNode.previous != null) {
            solutionStack.push(currentSearchNode.board);
            currentSearchNode = currentSearchNode.previous;
        }

        solutionStack.push(currentSearchNode.board);

        if (currentSearchNode.board.equals(initial)) {
            solvableFlag = true;
            solutionMoves = goalSearchNode.move;
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvableFlag;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solutionMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvableFlag)
            return solutionStack;
        return null;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
