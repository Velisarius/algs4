import edu.princeton.cs.algs4.*;
public class KdTree {
  private Node root;
  private int count;
  private Queue<Point2D> rangeResults;
  private Point2D minPoint;
  private double minDistSq;

  // construct an empty set of points
  public KdTree() {

  }

  private static class Node {
    private Point2D point;
    private RectHV rect;
    private Node left;
    private Node right;
    private boolean vertical;
    public Node(Point2D point, int depth, RectHV rect) {
      this.point = point;
      if (depth % 2 == 0) { this.vertical = true; }
      else { this.vertical = false; }
      this.rect = rect;
    }

    public int compareTo(Point2D thatPoint) {
      if (this.vertical) {
        if (this.point.equals(thatPoint)) { return 0; }
        if (this.point.x() < thatPoint.x()) { return -1; }
        // if (this.point.x() > thatPoint.x()) { return +1; }
        // return 0;
        return +1;
      }
      else {
        if (this.point.equals(thatPoint)) { return 0; }
        if (this.point.y() < thatPoint.y()) { return -1; }
        // if (this.point.y() > thatPoint.y()) { return +1; }
        // return 0;
        return +1;
      }
    }

    public RectHV rectangle(Point2D point) {
      double xmin, ymin, xmax, ymax;
      xmin = this.rect.xmin();
      ymin = this.rect.ymin();
      xmax = this.rect.xmax();
      ymax = this.rect.ymax();

      boolean positive;
      if (this.compareTo(point) < 0) { positive = true; }
      else  { positive = false; }

      if (this.vertical) {
        if (positive) {
          xmin = this.point.x();
        } else {
          xmax = this.point.x();
        }
      }
      else {
        if (positive) {
          ymin = this.point.y();
        } else {
          ymax = this.point.y();
        }
      }

      return new RectHV(xmin, ymin, xmax, ymax);
    }
  }

  // is the set empty?
  public boolean isEmpty() {
    return count == 0;
  }

  // number of points in the set
  public int size() {
    return count;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    root = put(root, p, 0, null);
  }

  private Node put(Node node, Point2D p, int depth, Node parent) {
    if (node == null) {
      count++;
      RectHV r;
      if (parent == null) { r = new RectHV(0.0, 0.0, 1.0, 1.0); }
      else { r = parent.rectangle(p); }
      return new Node(p, depth, r);
    }

    int cmp = node.compareTo(p);
    if (cmp > 0) {
      node.left = put(node.left, p, depth + 1, node);
    }
    if (cmp < 0) {
      node.right = put(node.right, p, depth + 1, node);
    }
    // else node.point = p;
    // System.out.print(node.rect);
    return node;
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    return has(root, p);
  }

  private boolean has(Node node, Point2D p) {
    while (node != null) {
      if (node.point.equals(p)) { return true; }
      int cmp = node.compareTo(p);;
      if (cmp > 0) { node = node.left; }
      if (cmp < 0) { node = node.right; }
    }
    return false;
  }

  // public RectHV findRect(Point2D p) { return findRect(root, p); }
  // private RectHV findRect(Node node, Point2D p) {
  //   while (node != null) {
  //     if (node.point == p) { return node.rect; }
  //     int cmp = node.compareTo(p);;
  //     if (cmp > 0) { node = node.left; }
  //     if (cmp < 0) { node = node.right; }
  //   }
  //   return null;
  // }

  // draw all points to standard draw
  public void draw() {
    StdDraw.show();
    draw(root);
  }

  private void draw(Node node) {
    if (node == null) { return; }
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(.01);
    node.point.draw();

    if (node.vertical) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.setPenRadius(.001);
      line(node).draw();
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.setPenRadius(.001);
      line(node).draw();
    }
    draw(node.left);
    draw(node.right);
  }

  // private void draw(Node node) {
  //   if (node == null) { return; }

  //   StdDraw.setPenColor(StdDraw.BLACK);
  //   StdDraw.setPenRadius(.01);
  //   node.point.draw();

  //   double x = node.point.x();
  //   double y = node.point.y();

  //   if (node.vertical) {
  //     StdDraw.setPenColor(StdDraw.RED);
  //     StdDraw.setPenRadius(.005);
  //     StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());

  //   } else {
  //     StdDraw.setPenColor(StdDraw.BLUE);
  //     StdDraw.setPenRadius(.005);
  //     StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
  //   }

  //   draw(node.left);
  //   draw(node.right);
  // }

  private RectHV line(Node node) {
    if (node.vertical) {
      return new RectHV(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
    }
    else {
      return new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
    }
  }

  private void rangeSearch(Node node, RectHV rect) {
    if (node == null) { return; }
    if (rect.contains(node.point)) { rangeResults.enqueue(node.point); }
    if (rect.intersects(line(node))) {
      rangeSearch(node.left, rect);
      rangeSearch(node.right, rect);
    }
    else if (node.vertical) {
      if (rect.xmax() < node.point.x()) {
        rangeSearch(node.left, rect);
      } else { rangeSearch(node.right, rect); }
    }
    else {
      if (rect.ymax() < node.point.y()) {
        rangeSearch(node.left, rect);
      } else { rangeSearch(node.right, rect); }
    }
  }

  // all points that are inside the rectangle
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) { throw new NullPointerException("Null arguments not allowed."); }
    rangeResults = new Queue<Point2D>();
    rangeSearch(root, rect);
    return rangeResults;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) { throw new NullPointerException("Null arguments not allowed."); }
    if (isEmpty()) { return null; }
    minDistSq = Double.POSITIVE_INFINITY;
    nodeSearch(root, root, p);
    return minPoint;
  }

  private void nodeSearch(Node node, Node nearest, Point2D searchPoint) {
    Node champion = nearest;
    if (node == null) { return; }
    double distance = node.point.distanceSquaredTo(searchPoint);
    if (distance < minDistSq) {
      minDistSq = distance;
      minPoint = node.point;
    }
    int cmp = node.compareTo(searchPoint);
    Node first;
    Node second;
    if (cmp > 0) {
      first = node.left;
      second = node.right;
    } else {
      first = node.right;
      second = node.left;
    }

    if (first != null && first.rect.distanceSquaredTo(searchPoint) < minDistSq) {
      nodeSearch(first, champion, searchPoint);
    }
    if (second != null && second.rect.distanceSquaredTo(searchPoint) < minDistSq) {
      nodeSearch(second, champion, searchPoint);
    }
    return;
  }

  // private Node nearestNode(Node node, Node nearest, Point2D searchPoint, double minDist) {
  //   Node champion = nearest;
  //   double minDistance = minDist;
  //   if (node == null) {return champion; }
  //   double distance = node.point.distanceSquaredTo(searchPoint);
  //   if (distance < minDist) {
  //     minDistance = distance;
  //     champion = node;
  //   }
  //   int cmp = node.compareTo(searchPoint);
  //   Node first;
  //   Node second;
  //   if (cmp > 0) {
  //     first = node.left;
  //     second = node.right;
  //   } else {
  //     first = node.right;
  //     second = node.left;
  //   }

  //   if (first != null && first.rect.distanceSquaredTo(searchPoint) < minDistance) {
  //     champion = nearestNode(first, champion, searchPoint, minDistance);
  //   }
  //   if (second != null && second.rect.distanceSquaredTo(searchPoint) < minDistance) {
  //     champion = nearestNode(second, champion, searchPoint, minDistance);
  //   }
  //   return champion;
  // }


   // unit testing of the methods (optional)
  public static void main(String[] args) {

  }

}
