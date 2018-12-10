//import static jdk.nashorn.internal.objects.NativeSet.size;
//
//public class RedBlackBST<Key extends Comparable<Key>, Value> {
//    private static final boolean RED = true;
//    private static final boolean BLACK = false;
//    private Node root;
//
//    private class Node{
//        Key key;
//        Value value;
//        Node left, right;
//        int N;
//        boolean color;
//
//        Node(Key key, Value value, int N, boolean color) {
//            this.key = key;
//            this.value = value;
//            this.N = N;
//            this.color = color;
//        }
//    }
//
//    private boolean isRed(Node node) {
//        if(node == null)
//            return false;
//        return node.color == RED;
//    }
//
//    Node rotateLeft(Node shaft) {
//        Node child = shaft.right;
//        shaft.right = child.left;
//        child.left = shaft;
//        child.color = shaft.color;
//        shaft.color = RED;
//        child.N = shaft.N;
//        shaft.N = 1 + size(shaft.left) + size(shaft.right);
//        return child;
//    }
//
//    Node rotateRight(Node shaft) {
//        Node child = shaft.left;
//        shaft.left = child.right;
//        child.right = shaft;
//        child.color = shaft.color;
//        shaft.color = RED;
//        child.N = shaft.N;
//        shaft.N = 1 + size(shaft.left) + size(shaft.right);
//        return child;
//    }
//
//    void flipColors(Node node) {
//        node.color = RED;
//        node.left.color = BLACK;
//        node.right.color = BLACK;
//    }
//
//    private int size(Node node) {
//        if(node == null)
//            return 0;
//        else
//            return node.N;
//    }
//
//    public void put(Key key, Value value) {
//        // 查找key,找到则更新其值，否则为它新建一个节点
//        root = put(root, key, value);
//        root.color = BLACK;
//    }
//
//    private Node put(Node node, Key key, Value value) {
//        if(node == null)
//            return new Node(key, value, 1, RED);
//        int cmp = key.compareTo(node.key);
//        if(cmp < 0)
//            node.left = put(node.left, key, value);
//        else if(cmp > 0)
//            node.right = put(node.right, key, value);
//        else
//            node.value = value;
//
//        if(isRed(node.right) && !isRed(node.left))
//            node = rotateLeft(node);
//        if(isRed(node.left) && isRed(node.left.left))
//            node = rotateRight(node);
//        if(isRed(node.left) && isRed(node.right))
//            flipColors(node);
//        node.N = size(node.left) + size(node.right) + 1;
//        return node;
//    }
//
//    private Node moveRedLeft(Node node) {
//        //假设节点node为红色，node.left和node.left.left都是黑色，
//        //将node.left或者node.left的子节点之一变红
//        flipColors(node);
//        if(isRed(node.right.left)) {
//            node.right = rotateRight(node.right);
//            node = rotateLeft(node);
//        }
//        return node;
//    }
//
//    public void deleteMin() {
//        if(!isRed(root.left) && !isRed(root.right))
//            root.color = RED;
//        root = deleteMin(root);
//        if(!isEmpty())
//            root.color = BLACK;
//    }
//
//    public Node deleteMin(Node node) {
//        if(node.left == null)
//            return null;
//        if(!isRed(node.left) && !isRed(node.left.left))
//            node = moveRedLeft(node);
//        node.left = deleteMin(node.left);
//        return balance(node);
//    }
//
//    private Node balance(Node node) {
//        if(isRed(node.right))
//            node = rotateLeft(node);
//        if(isRed(node.left) && isRed(node.right))
//            flipColors(node);
//        node.N = size(node.left) + size(node.right) + 1;
//        return node;
//    }
//
//    private Node moveRedRight(Node node) {
//        //假设节点node为红色，node.right和node.right.right都是黑色，
//        //将node.right或者node.right的子节点之一变红
//        flipColors(node);
//        if(isRed(node.left.left))
//            node = rotateRight(node);
//        return node;
//    }
//
//    public void deleteMax() {
//        if(!isRed(root.left) && !isRed(root.right))
//            root.color = RED;
//        root = deleteMax(root);
//        if(!isEmpty())
//            root.color = BLACK;
//    }
//
//    public Node deleteMax(Node node) {
//        if(isRed(node.left))
//            node = rotateRight(node);
//        if(node.right == null)
//            return null;
//        if(!isRed(node.right) && !isRed(node.right.left))
//            node = moveRedRight(node);
//        node.right = deleteMax(node.right;
//        return balance(node);
//    }
//
//    public void delete(Key key) {
//        if(!isRed(root.left) && !isRed(root.right))
//            root.color = RED;
//        root = delete(root, key);
//        if(!isEmpty)
//            root.color = BLACK;
//    }
//
//    private Node delete(Node node, Key key) {
//        if(key.compareTo(node.key) < 0) {
//            if(!isRed(node.left) && !isRed(node.left.left))
//                node = moveRedLeft(node);
//            node.left = delete(node.left, key);
//        }else {
//            if(isRed(node.left))
//                node = rotateRight(node);
//            if(key.compareTo(node.key) == 0 && (node.right == null))
//                return null;
//            if(!isRed(node.right) && !isRed(node.right.left))
//                node = moveRedRight(node);
//            if(key.compareTo(node.key) == 0) {
//            }
//        }
//    }
//}
