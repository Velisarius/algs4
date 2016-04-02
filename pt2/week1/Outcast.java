import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class Outcast {
  private WordNet wordnet;
  
  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }
  
  public String outcast(String[] nouns) {
    int max = Integer.MIN_VALUE;
    String outcast = null;
    
    for (String n : nouns) {
      int distance = 0;
      for (int i = 0; i < nouns.length; i++) {
        distance += wordnet.distance(n, nouns[i]);
      }
      if (distance > max) {
        max = distance;
        outcast = n;
       }
    }
    return outcast;
  }
  
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
  
  
}