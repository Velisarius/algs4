import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;
public class Solver {
  private MinPQ<SearchNode> pq1;
  private SearchNode solutionNode;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    Board board1;
    Board board2;
    if (initial == null) { throw new NullPointerException("Board can't be null."); }
    board1 = initial;
    board2 = board1.twin();
    pq1 = new MinPQ<SearchNode>(nodeCompare());
    pq1.insert(initializeNode(board1, null, true));
    pq1.insert(initializeNode(board2, null, false));
    solutionNode = solve();
  }

  private SearchNode solve() {
    int loopCount = 1;
    while (true) {
      loopCount++;
      SearchNode sn1 = pq1.delMin();
      Board board = sn1.board;
      if (board.isGoal()) { StdOut.println("loopCount: " + loopCount + "\n"); return sn1; }
      for (Board b : board.neighbors()) {
        if (sn1.prevNode == null || !b.equals(sn1.prevNode.board)) {
          SearchNode node = initializeNode(b, sn1, false);
          pq1.insert(node);
        }
      }
    }
  }

  private class SearchNode {
    private Board board;
    private SearchNode prevNode;
    private int moveCount;
    private int manCount;
    // private int hamCount;
    private int priority;
    private boolean isInitial;
  }

  private SearchNode initializeNode(Board b, SearchNode prev, boolean isInitial) {
    SearchNode node = new SearchNode();
    SearchNode prevNode = prev;
    node.board = b;
    node.prevNode = prevNode;
    if (prevNode == null) {
      node.moveCount = 0;
    } else {
      node.moveCount = prev.moveCount + 1;
    }
    if (isInitial || (node.prevNode != null && node.prevNode.isInitial)) {
      node.isInitial = true;
    }
    node.manCount = b.manhattan();
    // node.hamCount = b.hamming();
    node.priority = node.moveCount + node.manCount;
    return node;
  }

  // is the initial board solvable?
  public boolean isSolvable() {
    if (solutionNode == null) { return false; }
    return solutionNode.isInitial;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (!isSolvable()) { return -1; }
    else { return solutionNode.moveCount; }
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolvable()) { return null; }
    ArrayList<Board> bQ = new ArrayList<Board>();
    SearchNode node = solutionNode;
    while (node != null) {
      bQ.add(node.board);
      node = node.prevNode;
    }
    Collections.reverse(bQ);
    return bQ;
  }

  private Comparator<SearchNode> nodeCompare() {
    return new NodeCompare();
  }

  private class NodeCompare implements Comparator<SearchNode> {
    public int compare(SearchNode a, SearchNode b) {
      if (a.priority < b.priority) { return -1; }
      if (a.priority > b.priority) { return +1; }
      if (a.moveCount > b.moveCount) { return -1; }
      if (a.moveCount < b.moveCount) { return +1; }
      return -1;
    }
  }

  // solve a slider puzzle (given below)
  public static void main(String[] args) {
     // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
  }
}
