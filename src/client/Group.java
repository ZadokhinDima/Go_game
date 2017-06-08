package client;

import java.util.HashSet;

/**
 * Created by dimaz on 04.06.2017.
 */
public class Group {
    //Rocks of this group
    HashSet<Rock> members;
    //Available places around group
    HashSet<Empty> around;


    public HashSet<Rock> getMembers() {
        return members;
    }

    public HashSet<Empty> getAround() {
        return around;
    }

    public int getAvailableAround() {
        return around.size();
    }

    private boolean white;
    //if group is eaten, alive sets to false
    private boolean alive;

    //new group is being created every time player add the new rock
    public Group(boolean white, Rock oldest) {
        this.white = white;
        alive = true;
        members = new HashSet<>();

        members.add(oldest);
        around = new HashSet<>();

        //Groups that are in touch with this group
        HashSet<Group> nearbyGroups = new HashSet<>();

        //Going around and check all the groups that are in touch
        Node nearbyNode = Game.getNode(oldest.getX() - 1, oldest.getY());
        if (nearbyNode != null)
            if (nearbyNode.isEnable())
                around.add((Empty) Game.getNode(oldest.getX() - 1, oldest.getY()));
            else if (((Rock) nearbyNode).isWhite() == white)
                nearbyGroups.add(((Rock) nearbyNode).getGroup());
            else if (((Rock) nearbyNode).getGroup().getAvailableAround() == 1)
                ((Rock) nearbyNode).getGroup().eatIt();

        nearbyNode = Game.getNode(oldest.getX() + 1, oldest.getY());
        if (nearbyNode != null)
            if (nearbyNode.isEnable())
                around.add((Empty) Game.getNode(oldest.getX() + 1, oldest.getY()));
            else if (((Rock) nearbyNode).isWhite() == white)
                nearbyGroups.add(((Rock) nearbyNode).getGroup());
            else if (((Rock) nearbyNode).getGroup().getAvailableAround() == 1)
                ((Rock) nearbyNode).getGroup().eatIt();

        nearbyNode = Game.getNode(oldest.getX(), oldest.getY() - 1);
        if (nearbyNode != null)
            if (nearbyNode.isEnable())
                around.add((Empty) Game.getNode(oldest.getX(), oldest.getY() - 1));
            else if (((Rock) nearbyNode).isWhite() == white)
                nearbyGroups.add(((Rock) nearbyNode).getGroup());
            else if (((Rock) nearbyNode).getGroup().getAvailableAround() == 1)
                ((Rock) nearbyNode).getGroup().eatIt();

        nearbyNode = Game.getNode(oldest.getX(), oldest.getY() + 1);
        if (nearbyNode != null)
            if (nearbyNode.isEnable()) {
                around.add((Empty) Game.getNode(oldest.getX(), oldest.getY() + 1));
            }
            else if (((Rock) nearbyNode).isWhite() == white)
                nearbyGroups.add(((Rock) nearbyNode).getGroup());
            else if (((Rock) nearbyNode).getGroup().getAvailableAround() == 1)
                ((Rock) nearbyNode).getGroup().eatIt();

        //Absorbing all nearby friendly groups to make big one
        for (Group nearbyGroup : nearbyGroups) {
            for (Rock rock : nearbyGroup.getMembers()) {
                rock.setGroup(this);
                members.add(rock);
            }
            for (Empty empty : nearbyGroup.getAround()) {
                around.add(empty);
            }

        }

    }

    public boolean isWhite() {
        return white;
    }


    public boolean isAlive() {
        return alive;
    }


    //Method that makes group not alive (if it is surrounded)
    public void eatIt() {
        if(alive){
            alive = false;
            if(isWhite())
                Game.countBlack += getMembers().size();
            else
                Game.countWhite += getMembers().size();
        }
    }


    public void addRock(Rock e) {
        if (e.isWhite() == white)
            members.add(e);
    }


    //deleting node with specified coordinates from the set of free nodes around group
    public void deleteAvailable(int x, int y){

        Empty toDelete = null;

        for(Empty empty : around)
            if(empty.getX() == x && empty.getY() == y)
                toDelete = empty;

        around.remove(toDelete);

        //if no free nodes left - group is getting eaten
        if(around.isEmpty())
            eatIt();

    }
}
