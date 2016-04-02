import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
  private double[] percThresholds;
  // perform T independent experiments on an N-by-N grid
  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) {
      throw new IllegalArgumentException("N & T must be > 0.");
    }
    percThresholds = new double[T];
    for (int i = 0; i < T; i++) {
      Percolation perc = new Percolation(N);
      int opened = 0;
      while (!perc.percolates()) {
        int x = StdRandom.uniform(1, N + 1);
        int y = StdRandom.uniform(1, N + 1);
        if (!perc.isOpen(x, y)) {
          perc.open(x, y);
          opened++;
        }
      }
      percThresholds[i] = opened / (double) (N * N);
    }
  }
  // sample mean of percolation threshold
  public double mean() {
    double[] a = percThresholds;
    return StdStats.mean(a);
  }
  // sample standard deviation of percolation threshold
  public double stddev() {
    double[] a = percThresholds;
    if (a.length == 1) { return Double.NaN; } 
    else { return StdStats.stddev(a); }
  }
  // low  endpoint of 95% confidence interval
  public double confidenceLo() {
    double[] a = percThresholds;
    return (mean() - 1.96 * stddev() / Math.sqrt(a.length));
  }
  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    double[] a = percThresholds;
    return (mean() + 1.96 * stddev() / Math.sqrt(a.length));
  }
  // test client (described below)
  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(N, T);
    StdOut.println("mean: " + ps.mean());
    StdOut.println("stddev: " + ps.stddev());
    StdOut.println("95% confidence interval: " + ps.confidenceLo() + ", " + ps.confidenceHi());
  }
}
