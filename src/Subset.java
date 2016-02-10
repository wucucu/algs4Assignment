import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();

        // Reservoir sampling

        if (k == 0)
            return;

        int i = 0;
        while (i < k) {
            if (StdIn.isEmpty())
                return;

            i++;
            String item = StdIn.readString();
            q.enqueue(item);
        }

        while (!StdIn.isEmpty()) {
            i++;
            String item = StdIn.readString();

            if (StdRandom.uniform(i) < k) {
                q.dequeue();
                q.enqueue(item);
            }
        }

        for (int j = 0; j < k; j++)
            StdOut.println(q.dequeue());
    }

}
