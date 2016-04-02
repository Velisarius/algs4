import java.util.*;
import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class FastCollinearPoints {
  private ArrayList<LineSegment> collinearSegs;
  private final Point[] points;

  // finds all line segments containing 4 or more points
  public FastCollinearPoints(Point[] pointsParam) {
    if (pointsParam == null) { throw new NullPointerException("Point[] can't be blank."); }
    this.points = pointsParam.clone();
    Arrays.sort(points); // sort input
    for (int i = 0; i < points.length; i++) {
      if (points[i] == null) {
        throw new NullPointerException("Point[] can't be blank.");
      }
      if (i > 0 && points[i].compareTo(points[i - 1]) == 0) {
        throw new IllegalArgumentException("No duplicate points allowed.");
      }
    }

    collinearSegs = new ArrayList<LineSegment>();
    double slope;

    for (int i = 0; i < points.length; i++) {
      Point[] sortedArr = points.clone(); // clone sorted array
      Point swap = sortedArr[i];
      sortedArr[i] = sortedArr[0];
      sortedArr[0] = swap;
      Arrays.sort(sortedArr, 1, sortedArr.length, sortedArr[0].slopeOrder()); // sort cloned array by slope
      if (points.length > 1) {
        slope = sortedArr[0].slopeTo(sortedArr[1]);
      } else { slope = Double.NEGATIVE_INFINITY; }
      ArrayList<Point> colPointsArr = new ArrayList<Point>();
      colPointsArr.add(sortedArr[0]);
      // iterate over every point
      for (int j = 1; j < sortedArr.length; j++) {
        double currentSlope = sortedArr[0].slopeTo(sortedArr[j]);
        if (j == sortedArr.length - 1) {
          if (slope == currentSlope) { colPointsArr.add(sortedArr[j]); }
          if (colPointsArr.size() > 3) {
            Collections.sort(colPointsArr);
            LineSegment ls = new LineSegment(colPointsArr.get(0), colPointsArr.get(colPointsArr.size() - 1));
            if (colPointsArr.get(0).compareTo(points[i]) >= 0) { collinearSegs.add(ls); }
          }
        }
        else if (slope == currentSlope) {
          colPointsArr.add(sortedArr[j]);
        }
        else {
          if (colPointsArr.size() > 3) {
            Collections.sort(colPointsArr);
            LineSegment ls = new LineSegment(colPointsArr.get(0), colPointsArr.get(colPointsArr.size() - 1));
            if (colPointsArr.get(0).compareTo(points[i]) >= 0) { collinearSegs.add(ls); }
          }
          colPointsArr.clear();
          colPointsArr.add(sortedArr[0]);
          colPointsArr.add(sortedArr[j]);
          slope = currentSlope;
        }
      }
    }

  }

  // the number of line segments
  public int numberOfSegments() {
    return collinearSegs.size();
  }

  // the line segments
  public LineSegment[] segments() {
    return collinearSegs.toArray(new LineSegment[collinearSegs.size()]);
  }


   public static void main(String[] args) {

    // read the N points from a file
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
