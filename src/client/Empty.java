package client;

/**
 * Created by dimaz on 28.05.2017.
 */
public class Empty extends Node {
    //number of free nodes around this node
    int enableAround;

    public Empty(int x, int y, boolean empty, int enableAround) {
        super(x, y, empty);
        this.enableAround = enableAround;
    }

    //method is called when user added rock near this empty node
    public void addedNearMe() {
        enableAround--;
    }
    //Method for checking if the empty node available for specified user
    public boolean checkAvailable(boolean white) {

        boolean eat = false;

        if (enableAround > 0)
            return true;

        int availableInGroups = 0;


        //if new rock will eat any enemy group, node is available
        //also checking if move will be suicide
        //if yes, move is forbidden

        Rock toSide = (Rock) Game.getNode(this.x - 1, this.y);
        if (toSide != null && !(toSide.isWhite() == white)) {
            if (toSide.getGroup().getAvailableAround() == 1) {
                toSide.getGroup().eatIt();
                eat = true;
            }
        }
        if (toSide != null && (toSide.isWhite() == white)) {

            availableInGroups += toSide.getGroup().getAvailableAround() - 1;
        }

        toSide = (Rock) Game.getNode(this.x + 1, this.y);
        if (toSide != null && !(toSide.isWhite() == white)) {
            if (toSide.getGroup().getAvailableAround() == 1) {
                toSide.getGroup().eatIt();
                eat = true;
            }
        }
        if (toSide != null && (toSide.isWhite() == white)) {

            availableInGroups += toSide.getGroup().getAvailableAround() - 1;
        }

        toSide = (Rock) Game.getNode(this.x, this.y - 1);
        if (toSide != null && !(toSide.isWhite() == white)) {
            if (toSide.getGroup().getAvailableAround() == 1) {
                toSide.getGroup().eatIt();
                eat = true;
            }
        }
        if (toSide != null && (toSide.isWhite() == white)) {

            availableInGroups += toSide.getGroup().getAvailableAround() - 1;
        }

        toSide = (Rock) Game.getNode(this.x, this.y + 1);
        if (toSide != null && !(toSide.isWhite() == white)) {
            if (toSide.getGroup().getAvailableAround() == 1) {
                toSide.getGroup().eatIt();
                eat = true;
            }
        }
        if (toSide != null && (toSide.isWhite() == white)) {
            availableInGroups += toSide.getGroup().getAvailableAround() - 1;
        }

        if(eat)
            return true;

        if(availableInGroups == 0)
            return false;
        else
            return true;

    }


}
