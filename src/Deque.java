import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// a double-ended queue generalizing a stack and a queue
// support adding and removing elements from either the front or the back
public class Deque<Item> implements Iterable<Item> {
    private int N; // size
    private Node<Item> headSentinel;
    private Node<Item> tailSentinel;

    // doubly linked node
    private class Node<Item> {
        private Item item;
        // successor
        private Node<Item> succ;
        // predecessor
        private Node<Item> pred;
    }

    // construct an empty deque
    public Deque() {
        N = 0;
        headSentinel = new Node<Item>();
        tailSentinel = new Node<Item>();

        headSentinel.succ = tailSentinel;
        tailSentinel.pred = headSentinel;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return N == 0 ? true : false;
    }

    // the number of elements in the deque
    public int size() {
        return N;
    }

    // add an item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();

        N++;

        Node<Item> first = new Node<Item>();
        first.item = item;
        first.succ = headSentinel.succ;
        first.pred = headSentinel;

        first.succ.pred = first;
        headSentinel.succ = first;
    }

    // add an item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();

        N++;

        Node<Item> last = new Node<Item>();
        last.item = item;
        last.succ = tailSentinel;
        last.pred = tailSentinel.pred;

        last.pred.succ = last;
        tailSentinel.pred = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        N--;

        Item item = headSentinel.succ.item;
        headSentinel.succ = headSentinel.succ.succ;

        // avoid loitering
        headSentinel.item = null;
        headSentinel.pred = null;

        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        N--;

        Item item = tailSentinel.pred.item;
        tailSentinel = tailSentinel.pred;

        // avoid loitering
        tailSentinel.item = null;
        tailSentinel.succ = null;

        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator() {
            current = headSentinel.succ;
        }
        
        public boolean hasNext() {
            return current.succ != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            
            Item item = current.item;
            current = current.succ;
            
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<Integer>();
        for (String s : args) {
            Integer item = Integer.parseInt(s);
            dq.addLast(item);
        }
        for (Integer item : dq)
            StdOut.print(item + " ");
    }
}
