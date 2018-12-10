public class Node {
    private String key;
    private String value;
    private Node left;
    private Node right;
    private Node parent;
    private boolean color;
    private boolean isLeaf;

    public Node (boolean color, String key, String value) {
        this.color = color;
        this.key = key;
        this.value = value;
    }

    public Node (String key, String value, boolean isLeaf) {
        this.key = key;
        this.value = value;
        this.isLeaf = isLeaf;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public void exchangeColor(Node node) {
        boolean temp = node.getColor();
        node.setColor(this.getColor());
        this.setColor(temp);
    }

    public boolean isLeaf() {
        return this.isLeaf;
    }
}
