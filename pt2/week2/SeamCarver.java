import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class SeamCarver {
  private Picture picture;
  private int height;
  private int width;
  private int[][] colorArray;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture inputPicture) {
    if (input_picture == null) { throw new NullPointerException("picture can't be null"); }
    this.picture = new Picture(inputPicture);
    height = picture.height();
    width = picture.width();
    colorArray = new int[height()][width()];
    for (int col = 0; col < width(); col++) {
      for (int row = 0; row < height(); row++) {
        colorArray[row][col] = picture.get(col, row).getRGB();
      }
    }
  }

  // current picture
  public Picture picture() {
    Picture pic = new Picture(colorArray[0].length, colorArray.length);
    for (int col = 0; col < pic.width(); col++) {
      for (int row = 0; row < pic.height(); row++) {
        Color color = new Color(colorArray[row][col]);
        pic.set(col, row, color);
      }
    }
    return pic;
  }

  // width of current picture
  public int width() {
    return width;
  }

  // height of current picture
  public int height() {
    return height;
  }

  private boolean validCoordinates(int x, int y) {
    if (0 <= x && x < width() && 0 <= y && y < height()) {
      return true;
    }
    return false;
  }

  private boolean validSeam(int[] seam, boolean vertical) {
    for (int i = 0; i < seam.length - 1; i++) {
      int a = seam[i];
      int b = seam[i + 1];
      // check adjacency & lower bound for all but last
      if (a < b - 1 || a > b + 1 || a < 0) { return false; }
      // check upper bounds
      if (vertical && a >= width()) { return false; }
      if (!vertical && a >= height()) { return false; }
    }
    // check last element
    int last = seam[seam.length - 1];
    if (last < 0 || (vertical && last >= width()) || (!vertical && last >= height())) { return false; }
    if ((vertical && seam.length != height()) || (!vertical && seam.length != width())) { return false; }
    return true;
  }

  private boolean isBorder(int x, int y) {
    if (x == 0 || x == width() - 1 || y == 0 || y  == height() - 1) {
      return true;
    }
    return false;
  }

  private double square(int n) {
    return Math.pow(n, 2);
  }

  private int getRed(int x, int y) {
    Color rgb = new Color(colorArray[y][x]);
    return rgb.getRed();
  }

  private int getGreen(int x, int y) {
    Color rgb = new Color(colorArray[y][x]);
    return rgb.getGreen();
  }

  private int getBlue(int x, int y) {
    Color rgb = new Color(colorArray[y][x]);
    return rgb.getBlue();
  }

  private double hzDiff(int x, int y) {
    int r = Math.abs(getRed(x - 1, y) - getRed(x + 1, y));
    int g = Math.abs(getGreen(x - 1, y) - getGreen(x + 1, y));
    int b = Math.abs(getBlue(x - 1, y) - getBlue(x + 1, y));
    return square(r) + square(g) + square(b);
  }

  private double vtDiff(int x, int y) {
    int r = Math.abs(getRed(x, y - 1) - getRed(x, y + 1));
    int g = Math.abs(getGreen(x, y - 1) - getGreen(x, y + 1));
    int b = Math.abs(getBlue(x, y - 1) - getBlue(x, y + 1));
    return square(r) + square(g) + square(b);
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    if (!validCoordinates(x, y)) { throw new IndexOutOfBoundsException("x or y not in range."); }
    if (isBorder(x, y)) { return 1000.0; }
    return Math.sqrt(hzDiff(x, y) + vtDiff(x, y));
  }

  private ArrayList<int[]> adjacentPixels(int x, int y) {
    ArrayList<int[]> adjPix = new ArrayList<int[]>();
    if (y < height() - 1) {
      int row = y + 1;
      if (validCoordinates(x - 1, row)) {
        adjPix.add(new int[] {x - 1, row});
      }
      if (validCoordinates(x, row)) {
        adjPix.add(new int[] {x, row});      }
      if (validCoordinates(x + 1, row)) {
        adjPix.add(new int[] {x + 1, row});
      }
    }
    else {
      return new ArrayList<int[]>();
    }
    return adjPix;
  }

  private Picture flip(Picture original) {
    Picture flipped = new Picture(height(), width());
    for (int col = 0; col < flipped.width(); col++) {
      for (int row = 0; row < flipped.height(); row++) {
        Color color = original.get(row, col);
        flipped.set(col, row, color);
      }
    }
    height = flipped.height();
    width = flipped.width();

    int cols = colorArray[0].length;
    int rows = colorArray.length;
    int[][] flippedArray = new int[cols][rows];
    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        flippedArray[col][row] = colorArray[row][col];
      }
    }
    colorArray = flippedArray;

    return flipped;
  }

  private int pixelHash(int x, int y) {
    String s = x + " " + y;
    return s.hashCode();
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    double minDist = Double.POSITIVE_INFINITY;
    int[] minPixel = new int[2];
    double[][] distToPixel = new double[width()][height()];
    HashMap<Integer, int[]> pixelToPixel = new HashMap<Integer, int[]>();
    for (double[] row : distToPixel) { Arrays.fill(row, Double.POSITIVE_INFINITY); }
    for (int i = 0; i < width(); i++) {
      distToPixel[i][0] = 0;
    }
    for (int y = 0; y < height() - 1; y++) {
      for (int x = 0; x < width(); x++) {

        for (int[] ap : adjacentPixels(x, y)) {
          if (distToPixel[ap[0]][ap[1]] > distToPixel[x][y] + energy(ap[0], ap[1])) {
            distToPixel[ap[0]][ap[1]] = distToPixel[x][y] + energy(ap[0], ap[1]);
            pixelToPixel.put(pixelHash(ap[0], ap[1]), new int[] { x, y });
          }
        }
      }
    }
    for (int i = 0; i < width(); i++) {
      if (distToPixel[i][height() - 1] < minDist) {
        minDist = distToPixel[i][height() - 1];
        minPixel = new int[] { i, height() - 1 };
      }
    }
    int[] outputSeam = new int[height()];
    Stack<Integer> pixPath = new Stack<Integer>();
    pixPath.push(minPixel[0]);
    int mp = pixelHash(minPixel[0], minPixel[1]);
    while (true) {
      int[] pixelArr = pixelToPixel.get(mp);
      if (pixelArr == null) { break; }
      pixPath.push(pixelArr[0]);
      mp = pixelHash(pixelArr[0], pixelArr[1]);
    }
    for (int i = 0; i < outputSeam.length; i++) { outputSeam[i] = pixPath.pop(); }
    return outputSeam;
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    picture = flip(picture);
    int[] horizontalSeam = findVerticalSeam();
    picture = flip(picture);
    return horizontalSeam;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (seam == null) { throw new NullPointerException("seam can't be null"); }
    if (!validSeam(seam, true)) { throw new IllegalArgumentException("invalid seam"); }
    if (width() <= 1) { throw new IllegalArgumentException("too narrow"); }

    for (int row = 0; row < height(); row++) {
      int[] temp = new int[colorArray[row].length - 1];
      System.arraycopy(colorArray[row], 0, temp, 0, temp.length);
      System.arraycopy(colorArray[row], seam[row] + 1, temp, seam[row], colorArray[row].length - seam[row] - 1);
      colorArray[row] = temp;
    }
    width--;

  }

  // // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    if (seam == null) { throw new NullPointerException("seam can't be null"); }
    if (!validSeam(seam, false)) { throw new IllegalArgumentException("invalid seam"); }
    if (height() <= 1) { throw new IllegalArgumentException("too short"); }

    picture = flip(picture);
    removeVerticalSeam(seam);
    picture = flip(picture);
  }
}
