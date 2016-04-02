import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

  private int dequeSize;
  private Node<Item> header;
  private Node<Item> trailer;

  private class Node<Item> {
    private Item item;
    private Node<Item> next;
    private Node<Item> prev;
  }
  // construct an empty deque
  public Deque() {
    header = new Node<Item>();
    trailer = new Node<Item>();
    header.next = trailer;
    trailer.prev = header;
    dequeSize = 0;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return size() == 0;
  }

  // return the number of items on the deque
  public int size() {
    return dequeSize;
  }

  private boolean isSentinel(Node<Item> node) {
    return (node == header || node == trailer);
  }

  // sets next/prev for a/b
  private void setNext(Node<Item> a, Node<Item> b) {
    a.next = b;
    b.prev = a;
  }

  // return first node
  private Node<Item> getFirst() {
    return header.next;
  }

  // return last node
  private Node<Item> getLast() {
    return trailer.prev;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) { throw new NullPointerException("Item cannot be null."); }
    if (isEmpty()) {
      Node first = new Node<Item>();
      first.item = item;
      setNext(header, first);
      setNext(first, trailer);
    }
    else {
      Node<Item> second = getFirst();
      Node first = new Node<Item>();
      first.item = item;
      setNext(header, first);
      setNext(first, second);
    }
    dequeSize++;
  }

// add the item to the end
  public void addLast(Item item) {
    if (item == null) { throw new NullPointerException("Item cannot be null."); }
    if (isEmpty()) {
      Node last = new Node<Item>();
      last.item = item;
      setNext(header, last);
      setNext(last, trailer);
    }
    else {
      Node<Item> secondToLast = getLast();
      Node last = new Node<Item>();
      last.item = item;
      setNext(secondToLast, last);
      setNext(last, trailer);
    }
    dequeSize++;
  }

// remove and return the item from the front
  public Item removeFirst() {
    Node<Item> popNode;
    Item item;
    if (isEmpty()) { throw new NoSuchElementException("Deque is empty."); }

    popNode = getFirst();
    setNext(header, popNode.next);
    item = popNode.item;

    popNode.next = null;
    popNode.prev = null;
    popNode.item = null; // prevent orphaning?
    dequeSize--;
    return item;
  }

// remove and return the item from the end
  public Item removeLast() {
    Node<Item> popNode;
    Item item;
    if (isEmpty()) { throw new NoSuchElementException("Deque is empty."); }

    popNode = getLast();
    setNext(popNode.prev, trailer);
    item = popNode.item;

    popNode.next = null;
    popNode.prev = null;
    popNode.item = null; // prevent orphaning?
    dequeSize--;
    return item;
  }

  // return an iterator over items in order from front to end
  // public Iterator<Item> iterator() {
  // }

  public Iterator<Item> iterator()  {
    return new DequeIterator(getFirst());
  }

  // an iterator, doesn't implement remove() since it's optional
  private class DequeIterator implements Iterator<Item> {
    private Node<Item> current;

    public DequeIterator(Node<Item> first) {
      current = first;
    }

    public boolean hasNext()  { return (current != null && !isSentinel(current)); }
    public void remove()      { throw new UnsupportedOperationException("Cannot remove items.");  }

    public Item next() {
      if (!hasNext()) { throw new NoSuchElementException(); }
      Item item = current.item;
      current = current.next;
      return item;
    }
  }

// unit testing
  public static void main(String[] args) {
//    Deque d = new Deque();
//    StdOut.print(d.isEmpty());
//    String[] input = StdIn.readAllStrings();
//    for (int n = 0; n < input.length; n++) {
//      d.addFirst(input[n]);
//    }
//    Iterator i = d.iterator();
//    while (i.hasNext()) {
//      StdOut.print(i.next());
//    }
//    // for (int l = d.size(); l > 0; l--) {
//    //   StdOut.print(d.removeLast());
//    // }
//    StdOut.print(" | Again: ");
//    StdOut.print(d.isEmpty());
//
//    for (int n = 0; n < input.length; n++) {
//      d.addFirst(input[n]);
//    }
//    while (i.hasNext()) {
//      StdOut.print(i.next());
//    }
//    for (int l = d.size(); l > 0; l--) {
//      StdOut.print(d.removeLast());
//    }
  }
}
