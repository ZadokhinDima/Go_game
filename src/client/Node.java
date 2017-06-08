package client;

/**
 * Created by dimaz on 28.05.2017.
 */
public abstract class Node {

    //abstract class - parent for empty nodes and rocks

    protected int x, y;
    protected boolean enable;


    public Node(int x, int y, boolean enable){

        this.x = x;
        this.y = y;
        this.enable = enable;
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnable() {
        return enable;
    }
}
