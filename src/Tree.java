import java.util.ArrayList;

public abstract class Tree {
    private int keys;
    public abstract int getKeys();
    public abstract void insert(String key, String value);
    public abstract String search(String key);
    public abstract ArrayList<String> search(String start, String end);
    public abstract boolean delete(String key);
    public abstract void preorderTreeWalk();
}
