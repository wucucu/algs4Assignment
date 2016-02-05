

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class test {

	public static void main(String[] args) {
		WeightedQuickUnionUF wqu;
		wqu = new WeightedQuickUnionUF(10);
		wqu.union(1, 2);
		WeightedQuickUnionUF wqu1 = new WeightedQuickUnionUF(10);
		wqu1 = wqu;
		wqu1.union(4, 5);
		StdOut.println(wqu.connected(4, 5));
	}

}
