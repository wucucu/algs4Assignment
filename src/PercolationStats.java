import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] openSiteRatioArray;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        openSiteRatioArray = new double[T];
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Grid size N and experiment times T should be positive integers.");
        }
        Percolation perc;
        int openSiteAmount;
        for (int t = 0; t < T; t++) {
            perc = new Percolation(N);
            openSiteAmount = 0;
            while (!perc.percolates()) {
                int i, j;
                do {
                    i = StdRandom.uniform(1, N + 1);
                    j = StdRandom.uniform(1, N + 1);
                } while (perc.isOpen(i, j));
                perc.open(i, j);
                openSiteAmount += 1;
            }
            double A = openSiteAmount;
            double NN = N * N;
            openSiteRatioArray[t] = A / NN;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSiteRatioArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSiteRatioArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double m = mean();
        double t = openSiteRatioArray.length;
        return m - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double m = mean();
        double t = openSiteRatioArray.length;
        return m + 1.96 * stddev() / Math.sqrt(t);
    }

    // test client (described below)
    public static void main(String[] args) {
        int N = 2;
        int T = 10000;
        PercolationStats percStats = new PercolationStats(N, T);
        StdOut.println(percStats.mean());
        StdOut.println(percStats.stddev());
        System.out.println(percStats.confidenceLo());
        System.out.println(percStats.confidenceHi());

    }
}