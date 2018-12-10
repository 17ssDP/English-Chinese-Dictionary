import java.util.ArrayList;

public class RedBlackTree extends Tree{
    private final Node nil = new Node(false, "", "");
    private Node root;
    private int keys;

    public RedBlackTree() {
        this.root = nil;
        this.root.setRight(nil);
        this.root.setLeft(nil);
        this.root.setParent(nil);
    }
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        root.setColor(false);
        root.setRight(nil);
        root.setLeft(nil);
        root.setParent(nil);
        this.root = root;
    }

    public int getKeys() {
        return this.keys;
    }

    private void updateRoot(Node root) {
        root.setColor(false);
        root.setParent(nil);
        this.root = root;
    }

    public String search(String key) {
        long startTime = System.nanoTime();
        String result =  searchKey(this.getRoot(), key);
        long endTime = System.nanoTime();
        long runTime = endTime - startTime;
        System.out.println("RedBlackTree search time：" + runTime + "ns");
        return result;
    }

    public ArrayList<String> search(String start, String end) {
        long startTime = System.nanoTime();
        ArrayList<String> result =  rangeSearch(start, end);
        long endTime = System.nanoTime();
        long runTime = endTime - startTime;
        System.out.println("RedBlackTree range search time：" + runTime + "ns");
        return result;
    }

    private String searchKey(Node node, String key) {
        while(node != null && key.compareToIgnoreCase(node.getKey()) != 0) {
            if(key.compareToIgnoreCase(node.getKey()) < 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        if(node != null) {
            return node.getValue();
        }else {
            return "Can't find the word you search!";
        }
    }

    private Node searchNode(Node node, String key) {
        while(node != nil && key.compareToIgnoreCase(node.getKey()) != 0) {
            if(key.compareToIgnoreCase(node.getKey()) < 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }

    private Node searchRangeNode(Node node, String key) {
        if((node.getLeft() != nil) && (key.compareToIgnoreCase(node.getKey()) < 0))
            return searchRangeNode(node.getLeft(), key);
        else if((node.getRight() != nil) && (key.compareToIgnoreCase(node.getKey()) > 0))
            return searchRangeNode(node.getRight(), key);
        else {
            if(key.compareToIgnoreCase(node.getKey()) <= 0)
                return node;
            else
                return treeSuccessor(node);
        }
    }

    private ArrayList<String> rangeSearch(String start, String end) {
        Node node = searchRangeNode(this.getRoot(), start);
        ArrayList<String> result = new ArrayList<>();
        while(node != nil && end.compareToIgnoreCase(node.getKey()) >= 0) {
            result.add(node.getKey() + "   " + node.getValue());
            node = treeSuccessor(node);
        }
        return result;
    }

    public void insert(String key, String value) {
        this.keys++;
        Node node = new Node(true, key, value);
        Node tempNil = nil;
        Node tempRoot = this.getRoot();
        while(tempRoot != nil) {
            tempNil = tempRoot;
            if(node.getKey().compareToIgnoreCase(tempRoot.getKey()) < 0)
                tempRoot = tempRoot.getLeft();
            else if(node.getKey().compareToIgnoreCase(tempRoot.getKey()) > 0)
                tempRoot = tempRoot.getRight();
            else {
                tempRoot.setValue(value);
                this.keys--;
                return;
            }
        }
        node.setParent(tempNil);
        if(tempNil == nil)
            this.updateRoot(node);
        else if(node.getKey().compareToIgnoreCase(tempNil.getKey()) < 0)
            tempNil.setLeft(node);
        else
            tempNil.setRight(node);
        node.setLeft(nil);
        node.setRight(nil);
        node.setColor(true);
        this.insertFixUp(node);
    }

    public boolean delete(String key) {
        return deleteNode(key) != nil;
    }

    private Node deleteNode(String key) {
        Node node = searchNode(this.getRoot(), key);
        if(node != nil) {
            this.keys--;
            Node temp = node;
            Node newNode;
            boolean originalColor = temp.getColor();
            if(node.getLeft() == nil) {
                newNode = node.getRight();
                transplant(node, node.getRight());
            }else if(node.getRight() == nil) {
                newNode = node.getLeft();
                transplant(node, node.getLeft());
            }else {
                temp = miniNum(node.getRight());
                originalColor = temp.getColor();
                newNode = temp.getRight();
                if(temp.getParent() == node)
                    newNode.setParent(temp);
                else {
                    transplant(temp, temp.getRight());
                    temp.setRight(node.getRight());
                    temp.getRight().setParent(temp);
                }
                transplant(node, temp);
                temp.setLeft(node.getLeft());
                temp.getLeft().setParent(temp);
                temp.setColor(node.getColor());
            }
            if(!originalColor)
                deleteFixUp(newNode);
        }
        return node;
    }

    private void inorderTreeWalk(Node root, ArrayList<String> result, String start, String end) {
        if(root != nil) {
            inorderTreeWalk(root.getLeft(), result, start, end);
            if((root.getKey().compareToIgnoreCase(start) >= 0) && (root.getKey().compareToIgnoreCase(end) <= 0))
                result.add(root.getKey() + "   " + root.getValue());
            inorderTreeWalk(root.getRight(), result, start, end);
        }
    }

    public void preorderTreeWalk() {
        preorderTreeWalk(this.getRoot(), 0, 0);
    }

    private void preorderTreeWalk(Node root, int level, int child) {
        if(root != nil) {
            System.out.println();
            System.out.print("Level=" + level++ + "  ");
            System.out.print("Child=" + child + "  ");
            System.out.print(root.getKey() + "/" + root.getValue());
            String color = root.getColor()? "RED" : "BLACK";
            System.out.print("(" + color + ")");
            preorderTreeWalk(root.getLeft(), level, 0);
            preorderTreeWalk(root.getRight(), level, 1);
        }else {
            System.out.println();
            System.out.print("Level=" + level + "  ");
            System.out.print("Child=" + child + "  ");
            System.out.print("null");
        }
    }

    private void insertFixUp(Node node) {
        while(node.getParent().getColor()) {
            if(node.getParent() == node.getParent().getParent().getLeft()) {
                Node uncle = node.getParent().getParent().getRight();
                if(uncle.getColor()) {
                    node.getParent().setColor(false);
                    uncle.setColor(false);
                    node.getParent().getParent().setColor(true);
                    node = node.getParent().getParent();
                }else {
                    if(node == node.getParent().getRight()) {
                        node = node.getParent();
                        leftRotate(node);
                    }
                    node.getParent().setColor(false);
                    node.getParent().getParent().setColor(true);
                    rightRotate(node.getParent().getParent());
                }
            }else {
                Node uncle = node.getParent().getParent().getLeft();
                if(uncle.getColor()) {
                    node.getParent().setColor(false);
                    uncle.setColor(false);
                    node.getParent().getParent().setColor(true);
                    node = node.getParent().getParent();
                }else {
                    if(node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rightRotate(node);
                    }
                    node.getParent().setColor(false);
                    node.getParent().getParent().setColor(true);
                    leftRotate(node.getParent().getParent());
                }
            }
        }
        this.getRoot().setColor(false);
    }

    private void deleteFixUp(Node node) {
        while(node != this.getRoot() && !node.getColor()) {
            if(node == node.getParent().getLeft()) {
                Node sibling = node.getParent().getRight();
                if(sibling.getColor()) {
                    sibling.setColor(false);
                    node.getParent().setColor(true);
                    leftRotate(node.getParent());
                    sibling = node.getParent().getRight();
                }else if(!sibling.getLeft().getColor() && !sibling.getRight().getColor()) {
                    sibling.setColor(true);
                    node = node.getParent();
                }else {
                    if(!sibling.getRight().getColor()) {
                        sibling.getLeft().setColor(false);
                        sibling.setColor(true);
                        rightRotate(sibling);
                        sibling = node.getParent().getRight();
                    }
                    sibling.setColor(node.getParent().getColor());
                    node.getParent().setColor(false);
                    sibling.getRight().setColor(false);
                    leftRotate(node.getParent());
                    node = this.getRoot();
                }
            }else {
                Node sibling = node.getParent().getLeft();
                if(sibling.getColor()) {
                    sibling.setColor(false);
                    node.getParent().setColor(true);
                    rightRotate(node.getParent());
                    sibling = node.getParent().getLeft();
                }else if(!sibling.getLeft().getColor() && !sibling.getRight().getColor()) {
                    sibling.setColor(true);
                    node = node.getParent();
                }else {
                    if(!sibling.getLeft().getColor()) {
                        sibling.getRight().setColor(false);
                        sibling.setColor(true);
                        leftRotate(sibling);
                        sibling = node.getParent().getLeft();
                    }
                    sibling.setColor(node.getParent().getColor());
                    node.getParent().setColor(false);
                    sibling.getLeft().setColor(false);
                    rightRotate(node.getParent());
                    node = this.getRoot();
                }
            }
        }
        node.setColor(false);
    }

    private void transplant(Node parent, Node child) {
        if(parent.getParent() == nil)
            this.updateRoot(child);
        else if(parent == parent.getParent().getLeft())
            parent.getParent().setLeft(child);
        else
            parent.getParent().setRight(child);
        child.setParent(parent.getParent());
    }

    private Node miniNum(Node node) {
        while(node.getLeft() != nil)
            node = node.getLeft();
        return node;
    }

    private void leftRotate(Node shaft){
        Node temp = shaft.getRight();
        shaft.setRight(temp.getLeft());
        if(temp.getLeft() != nil)
            temp.getLeft().setParent(shaft);
        temp.setParent(shaft.getParent());
        if(shaft.getParent() == nil)
            this.updateRoot(temp);
        else if(shaft == shaft.getParent().getLeft())
            shaft.getParent().setLeft(temp);
        else
            shaft.getParent().setRight(temp);
        temp.setLeft(shaft);
        shaft.setParent(temp);
//        shaft.exchangeColor(temp);
    }

    private void rightRotate(Node shaft){
        Node temp = shaft.getLeft();
        shaft.setLeft(temp.getRight());
        if(temp.getRight() != nil)
            temp.getRight().setParent(shaft);
        temp.setParent(shaft.getParent());
        if(shaft.getParent() == nil)
            this.updateRoot(temp);
        else if(shaft == shaft.getParent().getLeft())
            shaft.getParent().setLeft(temp);
        else
            shaft.getParent().setRight(temp);
        temp.setRight(shaft);
        shaft.setParent(temp);
//        shaft.exchangeColor(temp);
    }

    private Node treeSuccessor(Node node) {
        if(node.getRight() != nil)
            return treeMinimum(node.getRight());
        Node newNode = node.getParent();
        while(newNode != nil && node != newNode.getLeft()) {
            node = newNode;
            newNode = newNode.getParent();
        }
        return newNode;
    }

    private Node treeMinimum(Node node) {
        while(node.getLeft() != nil)
            node = node.getLeft();
        return node;
    }
}
