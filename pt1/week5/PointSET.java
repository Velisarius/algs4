import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.*;
import java.util.TreeSet;
public class PointSET {
  private TreeSet<Point2D> treeSet;

  // construct an empty set of points
  public PointSET() {
    treeSet = new TreeSet<Point2D>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return treeSet.isEmpty();
  }

  // number of points in the set
  public int size() {
    return treeSet.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    treeSet.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    return treeSet.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    // StdDraw.show(0);
    for (Point2D p : treeSet) { p.draw(); }
    StdDraw.show(0);
  }

  // all points that are inside the rectangle
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) { throw new NullPointerException("Null arguments not allowed."); }
    Queue<Point2D> pQ = new Queue<Point2D>();
    for (Point2D p : treeSet) {
      if (rect.contains(p)) { pQ.enqueue(p); }
    }
    return pQ;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    if (treeSet.isEmpty()) { return null; }
    Point2D nearestPoint = new Point2D(0.0,0.0);
    double dist = Double.POSITIVE_INFINITY;
    for (Point2D thatPoint : treeSet) {
      if (p == thatPoint) { continue; }
      if (p.distanceTo(thatPoint) < dist) {
        dist = p.distanceTo(thatPoint);
        nearestPoint = thatPoint;
      }
    }
    return nearestPoint;
  }


   // unit testing of the methods (optional)
  public static void main(String[] args) {
    PointSET test = new PointSET();
    Point2D p1 = new Point2D(0.1,0.1);
    Point2D p2 = new Point2D(0.2,0.2);
    Point2D p3 = new Point2D(0.3,0.3);
    Point2D p4 = new Point2D(0.4,0.4);
    Point2D p5 = new Point2D(0.5,0.5);
    test.insert(p1);
    test.insert(p2);
    test.insert(p3);
    test.insert(p4);
    test.insert(p5);
    test.draw();

  }

}
