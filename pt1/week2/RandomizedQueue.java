import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] rqueue; // array of items
  private int size; // number of elements on stack

  // construct an empty randomized queue
  public RandomizedQueue() {
    rqueue = (Item[]) new Object[2];
    size = 0;
  }

  // is the queue empty?
  public boolean isEmpty() {
    return size() == 0;
  }

  // return the number of items on the queue
  public int size() {
    return size;
  }

  private void resize(int newSize) {
    Item[] temp = (Item[]) new Object[newSize];
    for (int i = 0; i < size; i++) {
      temp[i] = rqueue[i];
    }
    rqueue = temp;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null) { throw new NullPointerException("Item cannot be null."); }
    if (size == rqueue.length) { resize(2*rqueue.length); }
    rqueue[size++] = item;
  }

  // remove and return a random item
  public Item dequeue() {
    if (isEmpty()) { throw new NoSuchElementException("No items found."); }
    int randomInt = StdRandom.uniform(size);
    Item item = rqueue[randomInt];
    rqueue[randomInt] = rqueue[size - 1];
    rqueue[size - 1] = null; // avoid loitering
    size--;
    // shrink size of array if necessary
    if (size > 0 && size == rqueue.length / 4) { resize(rqueue.length / 2); }
    return item;
  }

  // return (but do not remove) a random item
  public Item sample() {
    if (isEmpty()) { throw new NoSuchElementException("No items found."); }
    int randomInt = StdRandom.uniform(size);
    Item item = rqueue[randomInt];
    return item;
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private int n;
    private Item[] randomArr;

    public RandomizedQueueIterator() {
      n = size() - 1;
      randomArr = (Item[]) new Object[size()];
      for (int i = 0; i < size(); i++) {
        randomArr[i] = rqueue[i];
      }
      StdRandom.shuffle(randomArr);
    }

    public boolean hasNext() {
      return n >= 0;
    }

    public void remove() { throw new UnsupportedOperationException(); }

    public Item next() {
      if (!hasNext()) { throw new NoSuchElementException(); }
      return randomArr[n--];
    }
  }


  // unit testing
  public static void main(String[] args) {
    // RandomizedQueue<String> rq = new RandomizedQueue<String>();
    // StdOut.print(rq.isEmpty());
    // String[] input = StdIn.readAllStrings();
    // for (int n = 0; n < input.length; n++) {
    //   rq.enqueue(input[n]);
    // }
    // Iterator i = rq.iterator();
    // Iterator j = rq.iterator();

    // StdOut.print(" Print i.next: ");

    // for (String s : rq) {
    //   for (String ss : rq) {
    //     StdOut.print(ss);
    //   }
    //   StdOut.print(s); }
    // StdOut.print(" | Sample: ");
    // for (int l = 0; l < 7; l++) {
    //   StdOut.print(rq.sample());
    // }
    // StdOut.print(" | Dequeue: ");
    // for (int l = rq.size(); l > 0; l--) {
    //   StdOut.print(rq.dequeue());
    // }
    // StdOut.print(" | Again: ");
    // StdOut.print(rq.isEmpty());

    // for (int n = 0; n < input.length; n++) {
    //   rq.enqueue(input[n]);
    // }
    // while (i.hasNext()) {
    //   StdOut.print(i.next());
    // }
    // for (int l = rq.size(); l > 0; l--) {
    //   StdOut.print(rq.dequeue());
    // }

  }
}
