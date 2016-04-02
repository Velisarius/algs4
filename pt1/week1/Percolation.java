import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
  private int gridSize;
  private int[][] grid;
  private boolean[] openSquares;
  private boolean[] connectedBottom;
  private WeightedQuickUnionUF wquf;

  // create N-by-N grid, with all sites blocked
  public Percolation(int N) {
    gridSize = N;
    if (gridSize <= 0) {
      throw new IllegalArgumentException("Grid size must be > 0.");
    }
    grid = new int[gridSize][gridSize];
    openSquares = new boolean[gridSize * gridSize];
    connectedBottom = new boolean[gridSize * gridSize];
    wquf = new WeightedQuickUnionUF(gridSize * gridSize + 1); // + 1 for virtual top
    for (int i = 0; i < gridSize; i++) {
      for (int j = 0; j < gridSize; j++) {
        grid[i][j] = i * gridSize + (j + 1);
        if (i == 0) { wquf.union(0, xyTo1D(i + 1, j + 1)); }
        if (i == gridSize - 1) { connectedBottom[xyTo1D(i + 1, j + 1)] = true; }
      }
    }
  }

  private int xyTo1D(int i, int j) {
    return (i-1) * gridSize + (j - 1);
  }

  private void validate(int i, int j) {
    int N = gridSize;
    if (i <= 0 || i > N) {
      throw new IndexOutOfBoundsException("row index i out of bounds");
    }
    if (j <= 0 || j > N) {
      throw new IndexOutOfBoundsException("column index j out of bounds");
    }
  }

  private void connect(int thisI, int thisJ, int thatI, int thatJ) {
    validate(thisI, thisJ);
    validate(thatI, thatJ);
    int a = xyTo1D(thisI, thisJ);
    int b = xyTo1D(thatI, thatJ);
    if (connectedBottom[wquf.find(a)] || connectedBottom[wquf.find(b)]) {
      wquf.union(a, b);
      connectedBottom[wquf.find(a)] = true;
    }
    else { wquf.union(a, b); }


  }

  // open site (row i, column j) if it is not open already
  public void open(int i, int j) {
    validate(i, j);
    openSquares[xyTo1D(i, j)] = true;
    // connect top
    if (i > 1 && isOpen(i - 1, j)) { connect(i, j, i - 1, j); }
    // connect below
    if (i < gridSize && isOpen(i + 1, j)) { connect(i, j, i + 1, j); }
    // connect left
    if (j > 1 && isOpen(i, j - 1)) { connect(i, j, i, j - 1); }
    // connect right
    if (j < gridSize && isOpen(i, j + 1)) { connect(i, j, i, j + 1); }
  }
  // is site (row i, column j) open?
  public boolean isOpen(int i, int j) {
    validate(i, j);
    return openSquares[xyTo1D(i, j)];
  }
  // is site (row i, column j) full?
  public boolean isFull(int i, int j) {
    validate(i, j);
    return (isOpen(i, j) && wquf.connected(xyTo1D(i, j), 0));
  }
  // does the system percolate?
  public boolean percolates() {
    // special edge cases for N = 1 or 2
    if (gridSize <= 2) {
      if (gridSize == 1) { return isOpen(1, 1); }
      else { return (isOpen(1, 1) && isOpen(2, 1)) || (isOpen(1, 2) && isOpen(2, 2)); }
    }
    else { return connectedBottom[wquf.find(0)]; }
  }
  // test client (optional)
  public static void main(String[] args) {

  }
}
