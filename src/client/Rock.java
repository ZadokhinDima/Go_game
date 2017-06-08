package client;

/**
 * Created by dimaz on 28.05.2017.
 */
public class Rock extends Node{

    private boolean white;
    private Group group;

    public Rock(int x, int y, boolean white){

        super(x, y, false);
        this.white = white;

        //Creating new group of Rocks with specified color
        this.group = new Group(white, this);

        //Going around the place of new Rock

        //left neighbour
        Node nearby = Game.getNode(x - 1, y);
        if(nearby != null) {
            if (nearby.isEnable())
                ((Empty) nearby).addedNearMe();
            else{
                ((Rock)nearby).getGroup().deleteAvailable(x, y);
            }
        }

        //right neighbour
        nearby = Game.getNode(x + 1, y);
        if(nearby != null) {
            if (nearby.isEnable())
                //if empty, tell him that he has 1 less empty neighbours
                ((Empty) nearby).addedNearMe();
            else{
                //if there is a Rock, tell his group, that this place is no longer free.
                ((Rock)nearby).getGroup().deleteAvailable(x, y);
            }
        }

        //upper neighbour
        nearby = Game.getNode(x, y - 1);
        if(nearby != null) {
            if (nearby.isEnable())
                //if empty, tell him that he has 1 less empty neighbours
                ((Empty) nearby).addedNearMe();
            else{
                //if there is a Rock, tell his group, that this place is no longer free.
                ((Rock)nearby).getGroup().deleteAvailable(x, y);
            }
        }

        //lower neighbour
        nearby = Game.getNode(x, y + 1);
        if(nearby != null) {
            if (nearby.isEnable())
                //if empty, tell him that he has 1 less empty neighbours
                ((Empty) nearby).addedNearMe();
            else{
                //if there is a Rock, tell his group, that this place is no longer free.
                ((Rock)nearby).getGroup().deleteAvailable(x, y);
            }
        }


    }

    public Group getGroup() {
        return group;
    }


    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isWhite() {
        return white;
    }
}
