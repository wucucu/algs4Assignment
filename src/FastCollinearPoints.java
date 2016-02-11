import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    // input amount of points
    private final int N;
    private Point[] points;

    // singly linked data structure to store the segments
    private Node firstSegmentNode;
    private int segmentsAmount;

    private class Node {
        private Node next;
        private LineSegment item;
    }

    // find all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new NullPointerException();
        }

        N = points.length;
        this.points = new Point[N];
        for (int i = 0; i < N; i++) {
            this.points[i] = points[i];
        }

        Arrays.sort(this.points);

        for (int i = 0; i < N - 1; i++) {
            if (this.points[i].compareTo(this.points[i + 1]) == 0)
                throw new IllegalArgumentException();
        }

        segmentsAmount = 0;
        firstSegmentNode = null;
        findCollinear4PointsSegments();
    }

    private void findCollinear4PointsSegments() {
        Point[] a = new Point[N];

        for (int i = 0; i < N; i++) {
            a[i] = points[i];
        }

        for (int i = 0; i < N; i++) {
            Point p = a[i];

            /*
             * StdOut.print(i); StdOut.print(p); StdOut.println();
             */

            // for(Point x:a) StdOut.print(x);
            // StdOut.println();

            // Arrays.sort(points);
            Arrays.sort(points, p.slopeOrder());
            sortSlopeOrderEqualItems(points);

            /*
             * for (Point x : points) { StdOut.print(x);
             * StdOut.print(x.slopeTo(p)); } StdOut.println();
             */

            for (int j = 1; j < N;) {
                int k = j;
                while (points[k].slopeTo(p) == points[j].slopeTo(p)) {
                    k++;
                    if (k == N)
                        break;
                }

                if (k - j >= 3) {
                    if (p.compareTo(points[j]) == 0)
                        throw new IllegalArgumentException();
                    if (p.compareTo(points[j]) < 0) {
                        // StdOut.print(new LineSegment(p, points[k - 1]));
                        // StdOut.println();
                        append(new LineSegment(p, points[k - 1]));
                        segmentsAmount++;
                    }
                }
                j = k;
            }

            // StdOut.println();
        }
    }

    private void sortSlopeOrderEqualItems(Point[] p) {
        for (int i = 1; i < N;) {
            int k = i;
            while (k < N - 1) {
                if (p[k].slopeTo(p[0]) == p[k + 1].slopeTo(p[0]))
                    k++;
                else
                    break;
            }
            insertionSort(p, i, k);
            i = k + 1;
        }
    }

    private void insertionSort(Point[] p, int i, int k) {
        for (int l = i + 1; l < k + 1; l++) {
            int m = l;
            while (p[m].compareTo(p[m - 1]) < 0) {
                swap(p, m, m - 1);
                m = m - 1;
                if (m == i)
                    break;
            }
        }
    }

    private void swap(Point[] p, int l, int i) {
        Point temp = p[l];
        p[l] = p[i];
        p[i] = temp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsAmount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] collinear4PointsSegments = new LineSegment[segmentsAmount];

        Node current = firstSegmentNode;
        for (int i = 0; i < segmentsAmount; i++) {
            collinear4PointsSegments[i] = current.item;
            current = current.next;
        }

        return collinear4PointsSegments;
    }

    private void append(LineSegment l) {
        Node newFirst = new Node();

        newFirst.item = l;
        newFirst.next = firstSegmentNode;

        firstSegmentNode = newFirst;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
