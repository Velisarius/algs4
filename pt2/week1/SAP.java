import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;
public class SAP {
  private Digraph G;
  private HashMap<Integer, Integer> lengthHash;
  private HashMap<Integer, Integer> ancestorHash;


  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph inputG) {
    if (inputG == null) { throw new NullPointerException("Graph can't be null."); }
    this.G = new Digraph(inputG);
    lengthHash = new HashMap<Integer, Integer>();
    ancestorHash = new HashMap<Integer, Integer>();
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if ((v < 0) || (v > G.V())) { throw new IndexOutOfBoundsException("v out of bounds."); }
    if ((w < 0) || (w > G.V())) { throw new IndexOutOfBoundsException("w out of bounds."); }
    int hashCode = (v + " " + w).hashCode();
    if (lengthHash.containsKey(hashCode)) { return lengthHash.get(hashCode); }
    int minDist = Integer.MAX_VALUE;
    BreadthFirstDirectedPaths bfds1 = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfds2 = new BreadthFirstDirectedPaths(G, w);
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfds1.hasPathTo(vertex) && bfds2.hasPathTo(vertex)) {
        int dist = bfds1.distTo(vertex) + bfds2.distTo(vertex);
        if (dist < minDist) {
          minDist = dist;
        }
      }
    }
    if (minDist == Integer.MAX_VALUE) {
      return -1;
    } else {
      lengthHash.put(hashCode, minDist);
      return minDist;
    }
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if ((v < 0) || (v > G.V())) { throw new IndexOutOfBoundsException("v out of bounds."); }
    if ((w < 0) || (w > G.V())) { throw new IndexOutOfBoundsException("w out of bounds."); }
    int hashCode = (v + " " + w).hashCode();
    if (ancestorHash.containsKey(hashCode)) { return ancestorHash.get(hashCode); }
    int minVertex = Integer.MAX_VALUE;
    int minDist = Integer.MAX_VALUE;
    BreadthFirstDirectedPaths bfds1 = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfds2 = new BreadthFirstDirectedPaths(G, w);
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfds1.hasPathTo(vertex) && bfds2.hasPathTo(vertex)) {
        int dist = bfds1.distTo(vertex) + bfds2.distTo(vertex);
        if (dist < minDist) {
          minVertex = vertex;
          minDist = dist;
        }
      }
    }
    if (minVertex == Integer.MAX_VALUE) {
      return -1;
    } else {
      ancestorHash.put(hashCode, minVertex);
      return minVertex;
    }
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    for (int i : v) {
      if ((i < 0) || (i > G.V())) { throw new IndexOutOfBoundsException("v out of bounds."); }
    }
    for (int i : w) {
      if ((i < 0) || (i > G.V())) { throw new IndexOutOfBoundsException("w out of bounds."); }
    }
    int minDist = Integer.MAX_VALUE;
    BreadthFirstDirectedPaths bfds1 = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfds2 = new BreadthFirstDirectedPaths(G, w);
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfds1.hasPathTo(vertex) && bfds2.hasPathTo(vertex)) {
        int dist = bfds1.distTo(vertex) + bfds2.distTo(vertex);
        if (dist < minDist) {
          minDist = dist;
        }
      }
    }
    if (minDist == Integer.MAX_VALUE) {
      return -1;
    } else { return minDist; }
    // int minLength = Integer.MAX_VALUE;
    // for (int vVertex : v) {
    //   for (int wVertex : w) {
    //     int length = length(vVertex, wVertex);
    //     if (length < minLength) { minLength = length; }
    //   }
    // }
    // if (minLength == Integer.MAX_VALUE) {
    //   return -1;
    // } else { return minLength; }

  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    for (int i : v) {
      if ((i < 0) || (i > G.V())) { throw new IndexOutOfBoundsException("v out of bounds."); }
    }
    for (int i : w) {
      if ((i < 0) || (i > G.V())) { throw new IndexOutOfBoundsException("w out of bounds."); }
    }
    int minVertex = Integer.MAX_VALUE;
    int minDist = Integer.MAX_VALUE;
    BreadthFirstDirectedPaths bfds1 = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bfds2 = new BreadthFirstDirectedPaths(G, w);
    for (int vertex = 0; vertex < G.V(); vertex++) {
      if (bfds1.hasPathTo(vertex) && bfds2.hasPathTo(vertex)) {
        int dist = bfds1.distTo(vertex) + bfds2.distTo(vertex);
        if (dist < minDist) {
          minVertex = vertex;
          minDist = dist;
        }
      }
    }
    if (minVertex == Integer.MAX_VALUE) {
      return -1;
    } else { return minVertex; }
    // int minLength = Integer.MAX_VALUE;
    // int minVertexV = Integer.MAX_VALUE;
    // int minVertexW = Integer.MAX_VALUE;
    // for (int vVertex : v) {
    //   for (int wVertex : w) {
    //     int length = length(vVertex, wVertex);
    //     if (length < minLength) {
    //       minLength = length;
    //       minVertexV = vVertex;
    //       minVertexW = wVertex;
    //     }
    //   }
    // }
    // if (minVertexV == Integer.MAX_VALUE && minVertexW == Integer.MAX_VALUE) {
    //   return -1;
    // } else { return ancestor(minVertexV, minVertexW); }
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
}
