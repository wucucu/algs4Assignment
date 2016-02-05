import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[] siteOpenStates;
    private boolean[] siteLinkToTopStates;
    private boolean[] siteLinkToBottomStates;
    private int gridSize;
    private int gridTotalSize;

    private WeightedQuickUnionUF wqu;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("grid size should be positive integer");
        }

        gridSize = N;
        gridTotalSize = gridSize * gridSize;
        siteOpenStates = new boolean[gridTotalSize + 1];
        siteFullStates = new boolean[gridTotalSize + 1];

        for (int i = 0; i < gridTotalSize + 1; i++) {
            siteOpenStates[i] = false;
            siteFullStates[i] = false;
        }

        wqu = new WeightedQuickUnionUF(1 + gridTotalSize);

    }

    private void validate(int i, int j) {
        if (i < 1 || j < 1 || i > gridSize || j > gridSize) {
            throw new IndexOutOfBoundsException("index(row " + i + ", column " + j + ") not in the grid range");
        }
    }

    private int ijTo1D(int i, int j) {
        validate(i, j);
        return ((i - 1) * gridSize + j);
    }

    // mark (i, j)th site as open and link it to neighbour open sites
    public void open(int i, int j) {
        siteOpenStates[ijTo1D(i, j)] = true;

        // union the (i, j)th site with neighbour open sites.
        // wqu and wqu1 first row link to the source site indexed as 0;
        if (i == 1) {
            wqu.union(ijTo1D(i, j), 0);
        }

        
        int sourceRoot = wqu.find(0);
        
        // link to the site above;
        if (i > 1) {
            if (isOpen(i - 1, j)) {
                wqu.union(ijTo1D(i, j), ijTo1D(i - 1, j));
            }
        }
        // link to the site below;
        if (i < gridSize) {
            if (isOpen(i + 1, j)) {
                wqu.union(ijTo1D(i, j), ijTo1D(i + 1, j));
            }
        }
        // link to the site on the left;
        if (j > 1) {
            if (isOpen(i, j - 1)) {
                wqu.union(ijTo1D(i, j), ijTo1D(i, j - 1));
            }
        }
        // link to the site on the right;
        if (j < gridSize) {
            if (isOpen(i, j + 1)) {
                wqu.union(ijTo1D(i, j), ijTo1D(i, j + 1));
            }
        }
        
        
        
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        return siteOpenStates[ijTo1D(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        return siteFullStates[ijTo1D(i, j)];
    }

    // does the system percolate?
    public boolean percolates() {
        return wqu.connected(0, gridTotalSize + 1);
    }

    public static void main(String[] args) {
        int N = 2;
        Percolation perc = new Percolation(N);

        while (!perc.percolates()) {
            int i, j;
            do {
                i = StdRandom.uniform(1, N + 1);
                j = StdRandom.uniform(1, N + 1);
            } while (perc.isOpen(i, j));
            perc.open(i, j);
        }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < N + 1; j++) {
                if (perc.isOpen(i, j)) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
    }
}
