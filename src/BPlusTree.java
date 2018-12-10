import java.util.ArrayList;
public class  BPlusTree extends Tree{
    private int minDegree;
    private Node root;
    private Node firstLeaf;
    private int keys;

    private class Node {
        private int len;
        private ArrayList<Node> childs;
        private ArrayList<String> keys;
        private ArrayList<String> values;
        private Node parent;
        private Node leftNode;
        private Node rightNode;
        private boolean isLeaf;

        private Node () {
            this.len = 0;
            this.childs = new ArrayList<>();
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.leftNode = null;
            this.rightNode = null;
            this.parent = null;
            this.isLeaf = true;
        }
    }

    public BPlusTree(int minDegree) {
        Node node = new Node();
        this.root = node;
        this.firstLeaf = node;
        this.minDegree = minDegree;
    }

    public int getKeys() {
        return this.keys;
    }
    public Node getRoot() {
        return this.root;
    }

    public void insert(String key, String value) {
        this.keys++;
        Node tempRoot = root;
        if(tempRoot.len == 2 * minDegree - 1) {
            Node temp = new Node();
            temp.isLeaf = false;
            this.root = temp;
            temp.childs.add(0, tempRoot);
            tempRoot.parent = this.root;
            splitChild(temp, 0);
            insertNonFull(temp, key, value);
        }else
            insertNonFull(tempRoot, key, value);
    }

    public String search(String key) {
        long startTime = System.nanoTime();
        String result =  searchKey(this.getRoot(), key);
        long endTime = System.nanoTime();
        long runTime = endTime - startTime;
        System.out.println("BPlusTree search time：" + runTime + "ns");
        return result;
    }

    public ArrayList<String> search(String start, String end) {
        long startTime = System.nanoTime();
        ArrayList<String> result =  rangeSearch(start, end);
        long endTime = System.nanoTime();
        long runTime = endTime - startTime;
        System.out.println("BPlusTree range search time：" + runTime + "ns");
        return result;
    }

    public boolean delete(String key) {
        return deleteKey(key) != null;
    }

    private Node deleteKey(String key) {
        Node leafNode = searchLeafNode(this.getRoot(), key);
        if(leafNode != null) {
            this.keys--;
            //删除叶节点中的key值和value
            int num = leafNode.keys.indexOf(key);
            //如果删除后叶子节点过短
            if(leafNode.len <= minDegree - 1) {
                //情况1：该节点的兄弟节点长度大于等于minDegree
                int index = leafNode.parent.childs.indexOf(leafNode);
                Node right = index + 1 < leafNode.parent.childs.size()? leafNode.parent.childs.get(index + 1) : null;
                Node left = index - 1 >= 0 ? leafNode.parent.childs.get(index - 1) : null;
                if((left != null) && (left.keys.size() >= minDegree)) {
                    //情况1a: 向该节点的左节点借一个key值
                    leafNode.keys.remove(num);
                    leafNode.values.remove(num);
                    leafNode.keys.add(0, left.keys.get(left.keys.size() - 1));
                    leafNode.values.add(0, left.values.get(left.values.size() - 1));
                    leafNode.parent.keys.set(index - 1, left.keys.get(left.keys.size() - 1));
                    left.keys.remove(left.keys.size() - 1);
                    left.values.remove(left.values.size() - 1);
                    left.len--;
                }else if((right != null) && (right.keys.size() >= minDegree)) {
                    //情况1b:  向该节点的右节点借一个key值
                    leafNode.keys.remove(num);
                    leafNode.values.remove(num);
                    leafNode.keys.add(leafNode.parent.keys.get(index));
                    leafNode.values.add(right.values.get(0));
                    leafNode.parent.keys.set(index, right.keys.get(1));
                    right.keys.remove(0);
                    right.values.remove(0);
                    right.len--;
                }else {
                    //情况2：该节点的兄弟节点长度小于minDegree,则需要合并节点
                    if(left != null) {
                        //情况2a: 同左兄弟合并
                        left.keys.addAll(leafNode.keys);
                        left.values.addAll(leafNode.values);
                        left.len += leafNode.len;
                        left.rightNode = leafNode.rightNode;
                        if(leafNode.rightNode != null)
                            leafNode.rightNode.leftNode = left;
                        leafNode.parent.keys.remove(index - 1);
                        leafNode.parent.childs.remove(index);
                        leafNode.parent.len--;
                        leafNode = left;
                        num = 0;
                        while(key.compareToIgnoreCase(leafNode.keys.get(num)) > 0)
                            num++;
                        leafNode.keys.remove(num);
                        leafNode.values.remove(num);
                        leafNode.len--;
                    }else if(right != null){
                        //情况2b: 同右兄弟合并
                        leafNode.keys.addAll(right.keys);
                        leafNode.values.addAll(right.values);
                        leafNode.len += right.len;
                        leafNode.rightNode = right.rightNode;
                        if(right.rightNode != null)
                            right.rightNode.leftNode = leafNode;
                        leafNode.parent.keys.remove(index);
                        leafNode.parent.childs.remove(index + 1);
                        leafNode.parent.len--;
                        num = 0;
                        while(key.compareToIgnoreCase(leafNode.keys.get(num)) > 0)
                            num++;
                        leafNode.keys.remove(num);
                        leafNode.values.remove(num);
                        leafNode.len--;
                    }else {
                        leafNode.keys.remove(num);
                        leafNode.values.remove(num);
                        leafNode.len--;
                    }
                }
            }else {
                //叶子节点足够长，可以直接删除
                leafNode.keys.remove(num);
                leafNode.values.remove(num);
                leafNode.len--;
            }
            Node indexNode = searchIndexNode(this.getRoot(), key);
            //删除内部节点中的key值
            if(indexNode != null) {
                num = 0;
                while(key.compareToIgnoreCase(indexNode.keys.get(num)) > 0)
                    num++;
                indexNode.keys.set(num, getSuccessor(indexNode.childs.get(num + 1)));
            }
            //删除后可能导致父节点过短，需要修复工作
            deleteFixUp(leafNode.parent);
            return leafNode;
        }else {
            return null;
        }
    }

    public void preorderTreeWalk(){
        preorderTreeWalk(this.getRoot(), 0, 0);
    }

    private String searchKey(Node node, String key) {
        int num = 0;
        while((num < node.len) && key.compareToIgnoreCase(node.keys.get(num)) > 0)
            num++;
        if((num < node.len) && key.equals(node.keys.get(num))) {
            if(node.isLeaf)
                return node.values.get(num);
            else
                return searchKey(node.childs.get(num + 1), key);
        }
        else if(node.isLeaf)
            return "Can't find the word you search!";
        else
            return searchKey(node.childs.get(num), key);
    }

    private Node rangeSearchKey(Node node, String key) {
        int num = 0;
        while((num < node.len) && key.compareToIgnoreCase(node.keys.get(num)) > 0)
            num++;
        if((num < node.len) && key.equals(node.keys.get(num))) {
            if(node.isLeaf)
                return node;
            else
                return rangeSearchKey(node.childs.get(num + 1), key);
        }
        else if(node.isLeaf)
            return node;
        else
            return rangeSearchKey(node.childs.get(num), key);
    }

    //查找包含key值的叶子节点
    private Node searchLeafNode(Node node, String key) {
        int num = 0;
        while((num < node.len) && key.compareToIgnoreCase(node.keys.get(num)) > 0)
            num++;
        if((num < node.len) && key.equals(node.keys.get(num))) {
            if(node.isLeaf)
                return node;
            else
                return searchLeafNode(node.childs.get(num + 1), key);
        }
        else if(node.isLeaf)
            return null;
        else
            return searchLeafNode(node.childs.get(num), key);
    }

    //查找包含key值的内部节点
    private Node searchIndexNode(Node node, String key) {
        int num = 0;
        while((num < node.len) && key.compareToIgnoreCase(node.keys.get(num)) > 0)
            num++;
        if((num < node.len) && key.equals(node.keys.get(num))) {
            if(node.isLeaf)
                return null;
            else
                return node;
        }
        else if(node.isLeaf)
            return null;
        else
            return searchIndexNode(node.childs.get(num), key);
    }

    private ArrayList<String> rangeSearch(String start, String end) {
        Node startNode = rangeSearchKey(this.getRoot(), start);
        ArrayList<String> result = new ArrayList<>();
        int indexStart = 0;
        for(int i =0; i < startNode.len; i++) {
            if(start.compareToIgnoreCase(startNode.keys.get(i)) > 0)
                indexStart++;
        }
        while(startNode != null) {
            while((indexStart < startNode.len) && (startNode.keys.get(indexStart).compareToIgnoreCase(end) <= 0)) {
                result.add(startNode.keys.get(indexStart) + "   " + startNode.values.get(indexStart));
                indexStart++;
            }
            indexStart = 0;
            startNode = startNode.rightNode;
        }
        return result;
    }

    private void preorderTreeWalk(Node root, int level, int child) {
//        System.out.print("root: " + this.root);
//        System.out.print("len:" + root.len + "   ");
//        System.out.print("keys:" + root.keys.size() + "   ");
//        System.out.print("children:" + root.childs.size() + "   ");
//        System.out.print("parent: " + root.parent + "   ");
        System.out.println();
        System.out.print("level=" + level++ + "   ");
        System.out.print("child=" + child + "   ");
        if(!root.isLeaf) {
            for(int i = 0; i < root.len; i++)
                System.out.print(root.keys.get(i) + "/");
            for(int i = 0; i <= root.len; i++) {
                preorderTreeWalk(root.childs.get(i), level, i);
            }
        }else {
//            System.out.print("Left: " + root.leftNode + "   ");
//            System.out.print("self: " + root + "   ");
//            System.out.print("Right: " + root.rightNode + "   ");
            for(int i = 0; i < root.len; i++) {
                System.out.print(root.keys.get(i) + "/");
                System.out.print(root.values.get(i) + "/");
            }
        }
    }

    private void splitChild(Node parent, int num) {
        Node right = new Node();
        Node left = parent.childs.get(num);
        right.isLeaf = left.isLeaf;
        if(left.isLeaf) {
            right.len = minDegree;
            //复制key值
            for(int j = 0; j <= minDegree - 1; j++)
                right.keys.add(j, left.keys.get(j + minDegree - 1));
            //复制value
            for(int j = 0; j <= minDegree - 1; j++)
                right.values.add(j, left.values.get(j + minDegree - 1));
            //移除value
            for(int j = 0; j < minDegree; j++)
                left.values.remove(left.values.size()  - 1);
            //链接叶子节点
            if(left.rightNode != null) {
                right.rightNode = left.rightNode;
                left.rightNode.leftNode = right;
            }
            left.rightNode = right;
            right.leftNode = left;
        }else {
            right.len = minDegree - 1;
            //复制key值
            for(int j = 0; j < minDegree - 1; j++)
                right.keys.add(j, left.keys.get(j + minDegree));
            //转移子节点
            for(int j = 0; j < minDegree; j++) {
                right.childs.add(j, left.childs.get(j + minDegree));
                left.childs.get(j + minDegree).parent = right;
            }
        }
        //向父节点中转移子节点
        parent.childs.add(num + 1, right);
        right.parent = parent;
        //向父节点中增加key值
        parent.keys.add(num, left.keys.get(minDegree - 1));
        parent.len++;
        //移除被复制的key值
        left.len = minDegree - 1;
        for(int i = 0; i < minDegree; i++)
            left.keys.remove(minDegree - 1);
        //移除子节点
        if(!left.isLeaf) {
            for(int i = 0; i < minDegree; i++)
                left.childs.remove(left.childs.size() - 1);
        }
    }

    private void insertNonFull(Node node, String key, String value) {
        int length = node.len;
        if(node.isLeaf) {
            while(length >= 1 && key.compareToIgnoreCase(node.keys.get(length - 1)) < 0) {
                length--;
            }
            node.keys.add(length, key);
            node.values.add(length, value);
            node.len++;
            //判断是否重复插入
            if((length - 1) >= 0 && node.keys.get(length - 1).equals(key)) {
                node.keys.remove(length - 1);
                node.values.remove(length - 1);
                node.len--;
                this.keys--;
            }
        }else {
            while(length >= 1 && key.compareToIgnoreCase(node.keys.get(length - 1)) < 0)
                length--;
            if(node.childs.get(length).len == 2 * minDegree - 1) {
                splitChild(node, length);
                if(key.compareToIgnoreCase(node.keys.get(length)) > 0)
                    length++;
            }
            insertNonFull(node.childs.get(length), key, value);
        }
    }

    private String getSuccessor(Node node) {
        if(!node.isLeaf) {
            return getSuccessor(node.childs.get(0));
        }
        return node.keys.get(0);
    }

    private void deleteFixUp(Node node) {
        if(node.len < minDegree - 1) {
            if(node.parent != null) {
                int index = node.parent.childs.indexOf(node);
                Node right = index + 1 < node.parent.childs.size()? node.parent.childs.get(index + 1) : null;
                Node left = index - 1 >= 0 ? node.parent.childs.get(index - 1) : null;
                //过程同叶节点的修复情况类似，只是内部节点属性更少
                if((left != null) && (left.keys.size() >= minDegree)) {
                    node.keys.add(0, node.parent.keys.get(index - 1));
                    node.childs.add(0, left.childs.get(left.childs.size() - 1));
                    node.len++;
                    node.parent.keys.set(index - 1, left.keys.get(left.keys.size() - 1));
                    left.keys.remove(left.keys.size() - 1);
                    left.childs.remove(left.childs.size() - 1);
                    left.len--;
                }else if((right != null) && (right.keys.size() >= minDegree)) {
                    node.keys.add(node.parent.keys.get(index));
                    node.childs.add(right.childs.get(0));
                    node.len++;
                    node.parent.keys.set(index, right.keys.get(0));
                    right.keys.remove(0);
                    right.childs.remove(0);
                    right.len--;
                }else {
                    if(left != null) {
                        left.keys.add(node.parent.keys.get(index - 1));
                        left.keys.addAll(node.keys);
                        left.childs.addAll(node.childs);
                        left.len = left.len + node.len + 1;
                        node.parent.keys.remove(index - 1);
                        node.parent.childs.remove(index);
                        node.parent.len--;
                    }else {
                        node.keys.add(node.parent.keys.get(index));
                        node.keys.addAll(right.keys);
                        node.childs.addAll(right.childs);
                        node.len = node.len + right.len + 1;
                        node.parent.keys.remove(index);
                        node.parent.childs.remove(index + 1);
                        node.parent.len--;
                    }
                    deleteFixUp(node.parent);
                }
            }else {
                this.root = node.childs.get(0);
                this.root.isLeaf = true;
                this.root.parent = null;
            }
        }
    }
}
