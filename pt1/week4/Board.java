import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;
public class Board {
  private final int dimension;
  private int[] blocks;
  private Board goalBoard;

  // construct a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column j)
  public Board(int[][] blocks) {
    if (blocks == null) { throw new NullPointerException("N can't be null."); }
    if (blocks[0].length < 1) { throw new IllegalArgumentException("N must be > 0."); }
    dimension = blocks[0].length;
    this.blocks = new int[dimension * dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        this.blocks[ijTo1D(i, j)] = blocks[i][j];
      }
    }
  }

  private Board makeGoalBoard() {
    int blockNumber = 1;
    int[][] inputBlocks = new int[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        inputBlocks[i][j] = blockNumber;
        blockNumber++;
      }
    }
    inputBlocks[dimension - 1][dimension - 1] = 0;
    goalBoard = new Board(inputBlocks);
    return goalBoard;
  }

  private int ijTo1D(int i, int j) {
    return i * dimension + j;
  }

  private int[] oneDToIJ(int n) {
    int[] ij = new int[2];
    ij[0] = n / dimension;
    ij[1] = n % dimension;
    return ij;
  }

  // board dimension N
  public int dimension() {
    return dimension;
  }

  // number of blocks out of place
  public int hamming() {
    int hamCount = 0;
    if (this.isGoal()) { return 0; } // has benefit of instantiating goalBoard if not already
    else {
      for (int i = 0; i < blocks.length; i++) {
        if (this.blocks[i] == 0) { continue; }
        if (this.blocks[i] != goalBoard.blocks[i]) { hamCount++; }
      }
    }
    return hamCount;
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() {
    int manCount = 0;
    if (this.isGoal()) { return 0; }
    else {
      for (int i = 0; i < blocks.length; i++) {
        int n = this.blocks[i];
        if (n == 0) { continue; }
        int[] belongsAt = oneDToIJ(n - 1);
        int[] isAt = oneDToIJ(i);
        int dist = Math.abs(belongsAt[0] - isAt[0]) + Math.abs(belongsAt[1] - isAt[1]);
        manCount += dist;
      }
    }
    return manCount;
  }

  // is this board the goal board?
  public boolean isGoal() {
    if (goalBoard == null) { makeGoalBoard(); }
    return equals(goalBoard);
  }

  // a board that is obtained by exchanging any pair of blocks
  public Board twin() {
    int[][] twinBlocks = blocksTo2D();
    if (twinBlocks[0][0] == 0) {
      int swap = twinBlocks[1][0];
      twinBlocks[1][0] = twinBlocks[1][1];
      twinBlocks[1][1] = swap;
    }
    else if (twinBlocks[0][0] != 0 && twinBlocks[1][0] != 0) {
      int swap = twinBlocks[0][0];
      twinBlocks[0][0] = twinBlocks[1][0];
      twinBlocks[1][0] = swap;
    }
    else {
      int swap = twinBlocks[0][0];
      twinBlocks[0][0] = twinBlocks[0][1];
      twinBlocks[0][1] = swap;
    }
    return new Board(twinBlocks);
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (this == y) { return true; }
    if (y == null) { return false; }
    if (this.getClass() != y.getClass()) { return false; }
    Board that = (Board) y;
    if (this.dimension() != that.dimension()) { return false; }
    if (!Arrays.equals(this.blocks, that.blocks)) { return false; }
    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    Queue<Board> bQ = new Queue<Board>();
    // find 0
    int index = 0;
    for (int i = 0; i < blocks.length; i++) {
      if (blocks[i] == 0) { index = i; }
    }
    int[] ij = oneDToIJ(index);
    int zeroI = ij[0];
    int zeroJ = ij[1];

    if (zeroI > 0) { bQ.enqueue(swap(zeroI, zeroJ, zeroI - 1, zeroJ)); }
    if (zeroI < dimension - 1) { bQ.enqueue(swap(zeroI, zeroJ, zeroI + 1, zeroJ)); }
    if (zeroJ > 0) { bQ.enqueue(swap(zeroI, zeroJ, zeroI, zeroJ - 1)); }
    if (zeroJ < dimension - 1) { bQ.enqueue(swap(zeroI, zeroJ, zeroI, zeroJ + 1)); }

    return bQ;
  }

  private Board swap(int i, int j, int p, int q) {
    int[][] swapBlocks = blocksTo2D();
    int swap = swapBlocks[i][j];
    swapBlocks[i][j] = swapBlocks[p][q];
    swapBlocks[p][q] = swap;
    return new Board(swapBlocks);
  }

  private int[][] blocksTo2D() {
    int[][] blocks2D = new int[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        blocks2D[i][j] = blocks[ijTo1D(i, j)];
      }
    }
    return blocks2D;
  }

  // string representation of this board (in the output format specified below)
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(dimension() + "\n");
    for (int i = 0; i < blocks.length; i++) {
      s.append(String.format("%2d ", blocks[i]));
      if ((i + 1) % dimension() == 0) { s.append("\n"); }
    }
    return s.toString();
  }


  // unit tests (not graded)
  public static void main(String[] args) {

  }
}
