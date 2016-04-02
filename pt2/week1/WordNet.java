import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class WordNet {
  private Digraph wordGraph;
  private SAP sap;
  private HashMap<String, ArrayList<Integer>> synsetWordHash;
  private HashMap<Integer, String> synsetIdHash;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null) { throw new NullPointerException("synsets can't be null."); }
    if (hypernyms == null) { throw new NullPointerException("hypernyms can't be null."); }
    In s = new In(synsets);
    In h = new In(hypernyms);
    parseSynsets(s);
    parseHypernyms(h);
    sap = new SAP(wordGraph);
    DirectedCycle dc = new DirectedCycle(wordGraph);
    if (dc.hasCycle()) { throw new IllegalArgumentException("Must be a DAG."); }
    int rootCount = 0;
    for (int v = 0; v < wordGraph.V(); v++) {
      if (synsetIdHash.containsKey(v) && wordGraph.outdegree(v) == 0) {
        rootCount += 1;
        if (rootCount > 1) { throw new IllegalArgumentException("Only one root allowed."); }
      }
    }
  }

  private void parseSynsets(In input) {
    synsetWordHash = new HashMap<String, ArrayList<Integer>>();
    synsetIdHash = new HashMap<Integer, String>();
    String[] lines = input.readAllLines();
    for (int i = 0; i < lines.length; i++) {
      String[] arr = lines[i].split(",");
      int id = Integer.parseInt(arr[0]);
      synsetIdHash.put(id, arr[1]);
      String[] synonyms = arr[1].split(" ");
      for (int j = 0; j < synonyms.length; j++) {
        String key = synonyms[j];
        ArrayList<Integer> val = synsetWordHash.get(key);
        if (val == null) { val = new ArrayList<Integer>(); }
        val.add(id);
        synsetWordHash.put(key, val);
      }
    }
  }

  private void parseHypernyms(In input) {
    wordGraph = new Digraph(synsetWordHash.size());
    String[] lines = input.readAllLines();
    for (int i = 0; i < lines.length; i++) {
      String[] arr = lines[i].split(",");
      int synsetId = Integer.parseInt(arr[0]);
      for (int j = 1; j < arr.length; j++) {
        int hypernymId = Integer.parseInt(arr[j]);
        wordGraph.addEdge(synsetId, hypernymId);
      }
    }
  }
  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return synsetWordHash.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) { throw new NullPointerException("word can't be null."); }
    return synsetWordHash.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null) { throw new NullPointerException("A can't be null."); }
    if (nounB == null) { throw new NullPointerException("B can't be null."); }
    if (!isNoun(nounA)) { throw new IllegalArgumentException("A must be a noun."); }
    if (!isNoun(nounB)) { throw new IllegalArgumentException("B must be a noun."); }
    return sap.length(synsetWordHash.get(nounA), synsetWordHash.get(nounB));
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null) { throw new NullPointerException("A can't be null."); }
    if (nounB == null) { throw new NullPointerException("B can't be null."); }
    if (!isNoun(nounA)) { throw new IllegalArgumentException("A must be a noun."); }
    if (!isNoun(nounB)) { throw new IllegalArgumentException("B must be a noun."); }
    int ancestor = sap.ancestor(synsetWordHash.get(nounA), synsetWordHash.get(nounB));
    return synsetIdHash.get(ancestor);
  }

  public static void main(String[] args) {
    WordNet wn = new WordNet(args[0], args[1]);
    // while (!StdIn.isEmpty()) {
    //     String v = StdIn.readString();
    //     String w = StdIn.readString();
    //     int length   = wn.distance(v, w);
    //     String ancestor = wn.sap(v, w);
    //     StdOut.printf("distance = %d, sap = %s\n", length, ancestor);
    // }
  }

}
