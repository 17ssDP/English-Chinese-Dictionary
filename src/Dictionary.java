import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dictionary {
    private BPlusTree bPlusTree;
    private RedBlackTree redBlackTree;
    public Dictionary() {
        this.bPlusTree = new BPlusTree(5);
        this.redBlackTree = new RedBlackTree();
    }

    public void insert(String english, String chinese, int type) {
        Tree tree = type == 0? this.bPlusTree : this.redBlackTree;
        tree.insert(english, chinese);
    }

    public boolean delete(String english, int type) {
        return type == 0? this.bPlusTree.delete(english) : this.redBlackTree.delete(english);
    }

    public String search(String key, int type) {
        return type == 0? this.bPlusTree.search(key) : this.redBlackTree.search(key);
    }

    public ArrayList<String> search(String start, String end, int type) {
        return type == 0? this.bPlusTree.search(start, end) : this.redBlackTree.search(start, end);
    }

    public boolean update(File file, int type) {
        Tree tree = type == 0? this.bPlusTree : this.redBlackTree;
        try {
            String string = null;
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            string = input.readLine();
            if(string.equals("INSERT")) {
                String english;
                String chinese;
                int num = 0;
                long startTime = System.nanoTime();
                long endTime;
                while(((english = input.readLine()) != null) && ((chinese = input.readLine()) != null)) {
                    tree.insert(english, chinese);
                    num++;
                    if(num == 100) {
                        endTime = System.nanoTime();
                        System.out.println(tree.getClass() + " Insert time: " + (endTime - startTime) + "ns");
                        if(tree.getKeys() <= 500) {
                            tree.preorderTreeWalk();
                        }
                        num = 0;
                        startTime = System.nanoTime();
                    }
                }
            }else if(string.equals("DELETE")) {
                String english;
                int num = 0;
                long startTime = System.nanoTime();
                long endTime;
                while((english = input.readLine()) != null) {
                    tree.delete(english);
                    num++;
                    if(num == 100) {
                        endTime = System.nanoTime();
                        System.out.println(tree.getClass() + " Delete time: " + (endTime - startTime) + "ns");
                        startTime = endTime;
                        num = 0;
                    }
                }
            }
            inputStream.close();
            input.close();
            return  true;
        }catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }
}
