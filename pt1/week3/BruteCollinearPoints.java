import java.util.*;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private ArrayList<LineSegment> collinearSegs;
  private final Point[] points;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] pointsParam) {
    if (pointsParam == null) { throw new NullPointerException("Point[] can't be blank."); }
    this.points = pointsParam.clone();
    Arrays.sort(points);
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null) {
        throw new NullPointerException("Point[] can't be blank.");
      }
      if (i > 0 && points[i].compareTo(points[i - 1]) == 0) {
        throw new IllegalArgumentException("No duplicate points allowed.");
      }
    }

    collinearSegs = new ArrayList<LineSegment>();

    for (int i = 0; i < points.length; i++) {
      for (int j = i + 1; j < points.length; j++) {
        for (int k = j + 1; k < points.length; k++) {
          for (int l = k + 1; l < points.length; l++) {
            Point[] fourArr = new Point[4];
            fourArr[0] = points[i];
            fourArr[1] = points[j];
            fourArr[2] = points[k];
            fourArr[3] = points[l];
            checkFour(fourArr);
          }
        }
      }
    }
  }

  // the number of line segments
  public  int numberOfSegments() {
    return collinearSegs.size();
  }

  // the line segments
  public LineSegment[] segments() {
    return collinearSegs.toArray(new LineSegment[collinearSegs.size()]);
  }

  private void checkFour(Point[] points) {
    Point p = points[0];
    Point q = points[1];
    Point r = points[2];
    Point s = points[3];
    double p2q = p.slopeTo(q);
    double p2r = p.slopeTo(r);
    double p2s = p.slopeTo(s);

    if (p2q == p2r && p2r == p2s) {
      LineSegment ls = new LineSegment(p, s);
      collinearSegs.add(ls);
    }
  }

  public static void main(String[] args) {

  //   // read the N points from a file
  //   In in = new In(args[0]);
  //   int N = in.readInt();
  //   Point[] points = new Point[N];
  //   for (int i = 0; i < N; i++) {
  //       int x = in.readInt();
  //       int y = in.readInt();
  //       points[i] = new Point(x, y);
  //   }

  //   // draw the points
  //   StdDraw.show(0);
  //   StdDraw.setXscale(0, 32768);
  //   StdDraw.setYscale(0, 32768);
  //   for (Point p : points) {
  //       p.draw();
  //   }
  //   StdDraw.show();

  //   // print and draw the line segments
  //   BruteCollinearPoints collinear = new BruteCollinearPoints(points);
  //   for (LineSegment segment : collinear.segments()) {
  //       StdOut.println(segment);
  //       segment.draw();
  //   }
  }
}
