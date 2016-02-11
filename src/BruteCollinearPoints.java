import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

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
    public BruteCollinearPoints(Point[] points) {
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
        Point[] p = new Point[4];
        if (4 > N)
            return;

        for (int i1 = 0; i1 < N; i1++) {
            p[0] = points[i1];
            for (int i2 = i1 + 1; i2 < N; i2++) {
                p[1] = points[i2];
                for (int i3 = i2 + 1; i3 < N; i3++) {
                    p[2] = points[i3];
                    if (!isCollinear(p[0], p[1], p[2]))
                        continue;
                    for (int i4 = i3 + 1; i4 < N; i4++) {
                        p[3] = points[i4];
                        if (isCollinear(p[0], p[1], p[3])) {
                            append(extremumPoints(p));
                            segmentsAmount++;
                            break;
                        }
                    }
                }
            }

        }

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

    private boolean isCollinear(Point a, Point b, Point c) {
        return b.slopeTo(a) == c.slopeTo(a);
    }

    private LineSegment extremumPoints(Point[] pointsCollinear) {
        Point p, q;
        p = pointsCollinear[0];
        q = pointsCollinear[0];
        for (int i = 1; i < pointsCollinear.length; i++) {
            if (pointsCollinear[i].compareTo(p) < 0)
                p = pointsCollinear[i];
            if (pointsCollinear[i].compareTo(q) > 0)
                q = pointsCollinear[i];
        }

        return new LineSegment(p, q);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
