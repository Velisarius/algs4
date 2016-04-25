import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.ArrayList;

public class BoggleSolver {
  private MyTrieST<Integer> tst;
  private char[][] bBoard;
  private HashSet<String> allWords;
  private HashSet<String> words;


  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    tst = new MyTrieST<Integer>();
    int v = 0;
    for (String s : dictionary) {
      tst.put(s, v);
    }

  }

  // Returns the HashSet of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    allWords = new HashSet<String>();
    bBoard = new char[board.rows()][board.cols()];
    for (int r = 0; r < board.rows(); r++) {
      for (int c = 0; c < board.cols(); c++) {
        bBoard[r][c] = board.getLetter(r, c);
      }
    }
    for (int r = 0; r < board.rows(); r++) {
      for (int c = 0; c < board.cols(); c++) {
        allWords.addAll(wordsFrom(r, c));
      }
    }
    return new ArrayList<String>(allWords);
  }

  private HashSet<String> wordsFrom(int r, int c) {
    boolean[][] visited = new boolean[bBoard.length][bBoard[0].length];
    words = new HashSet<String>();
    wordDFS(r, c, visited, "");
    return words;
  }

  private void wordDFS(int r, int c, boolean[][] visited, String string) {
    visited[r][c] = true;
    string += String.valueOf(bBoard[r][c]);
    if (bBoard[r][c] == 81) { string += "U"; } // Account for QU
    if (tst.containsKeysWithPrefix(string)) {
      if (string.length() > 2 && tst.contains(string)) { words.add(string); }
      if (visitable(r - 1, c - 1, visited)) { wordDFS(r - 1, c - 1, visited, string); }
      if (visitable(r - 1, c - 0, visited)) { wordDFS(r - 1, c - 0, visited, string); }
      if (visitable(r - 1, c + 1, visited)) { wordDFS(r - 1, c + 1, visited, string); }
      if (visitable(r - 0, c - 1, visited)) { wordDFS(r - 0, c - 1, visited, string); }
      if (visitable(r - 0, c + 1, visited)) { wordDFS(r - 0, c + 1, visited, string); }
      if (visitable(r + 1, c - 1, visited)) { wordDFS(r + 1, c - 1, visited, string); }
      if (visitable(r + 1, c - 0, visited)) { wordDFS(r + 1, c - 0, visited, string); }
      if (visitable(r + 1, c + 1, visited)) { wordDFS(r + 1, c + 1, visited, string); }
    }
    visited[r][c] = false;

  }

  private boolean visitable(int r, int c, boolean[][] visited) {
    if (r < 0 || r > bBoard.length - 1) { return false; }
    if (c < 0 || c > bBoard[0].length - 1) { return false; }
    if (visited[r][c]) { return false; }
    return true;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (tst.contains(word)) {
      return score(word);
    } else {
      return 0;
    }

  }

  private int score(String word) {
    int length = word.length();
    if (length > 7) { return 11; }
    else if (length > 6) { return 5; }
    else if (length > 5) { return 3; }
    else if (length > 4) { return 2; }
    else if (length > 2) { return 1; }
    else { return 0; }
  }


  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board))
    {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
