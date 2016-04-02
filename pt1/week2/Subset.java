import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
public class Subset {
  public static void main(String[] args) {
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    int k = Integer.parseInt(args[0]);
    String[] s = StdIn.readAllStrings();
    StdRandom.shuffle(s);
    for(int i = 0; i < k; i++) {
      rq.enqueue(s[i]);
    }
//    while(!StdIn.isEmpty()) {
//      String s = StdIn.readString();
//      rq.enqueue(s);
//    }
    
    for (int i = 0; i < k; i++) {
      StdOut.println(rq.dequeue());
    }
  }
}