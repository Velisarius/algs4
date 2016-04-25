public class MyTrieST<Value> {
    private static final int R = 26;
    private static final int charOffset = 65;
    private Node root = new Node();

    private static class Node {
      private Object value;
      private Node[] next = new Node[R];
    }

    public void put(String key, Value val) {
      root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
      if (x == null) { x = new Node(); }
      if (d == key.length()) { x.value = val; return x; }
      int c = key.charAt(d) - charOffset;
      x.next[c] = put(x.next[c], key, val, d + 1);
      return x;
    }

    public boolean contains(String key) {
      return get(key) != null;
    }

    public Object get(String key) {
      Node x = get(root, key, 0);
      if (x == null) { return null; }
      return x.value;
    }

    private Node get(Node x, String key, int d) {
      if (x == null) { return null; }
      if (d == key.length()) { return x; }
      int c = key.charAt(d) - charOffset;
      return get(x.next[c], key, d + 1);
    }

    public boolean containsKeysWithPrefix(String prefix) {
      Node x = get(root, prefix, 0);
      if (x == null) { return false; }
      return x.next != null;
    }
  }
