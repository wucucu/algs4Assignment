import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int N;
    private int first;
    private int last;

    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        N = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void resize(int max) {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last = N;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();

        if (N == q.length)
            resize(2 * q.length);

        q[last] = item;

        last++;
        if (last == q.length)
            last = 0;

        N++;
    }

    // randomly remove and return an item
    public Item dequeue() {
        if (N == 0)
            throw new NoSuchElementException();

        int r = (first + StdRandom.uniform(N)) % q.length;
        Item item = q[r];
        q[r] = q[first];
        q[first] = null;

        first++;
        if (first == q.length)
            first = 0;

        N--;
        if (N > 0 && N == q.length / 4)
            resize(q.length / 2);

        return item;
    }

    // return a item randomly
    public Item sample() {
        if (N == 0) throw new NullPointerException();
        return q[(first + StdRandom.uniform(N)) % N];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int index;
        private int n;
        private int[] r;

        public RandomizedQueueIterator() {
            index = 0;
            n = N;
            r = new int[n];

            for (int i = 0; i < r.length; i++) {
                r[i] = i;
            }

            StdRandom.shuffle(r);
        }

        public boolean hasNext() {
            return index < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = q[(r[index] + first) % q.length];
            index++;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (String s : args)
            q.enqueue(s);
        for (String item : q)
            StdOut.print(item + " ");
        StdOut.println();
        for (int i = 0; i < q.size(); i++)
            StdOut.print(q.sample() + " ");
        StdOut.println();
        while (!q.isEmpty())
            StdOut.print(q.dequeue() + " ");
    }
}
