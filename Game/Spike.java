import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Spike is an object that gets created in the world.
 * If a player touches the spike then the player dies.
 * The spike is also able to move.
 * 
 * @author Terrence Hung, Ernest Wong
 * @version June 13, 2014
 */
public class Spike extends Background
{
    //declare variables
    private boolean moving;
    private boolean inWorld;
    private boolean x;
    private int direction;
    private int speed;
    
    /**
     * Constructor for class Spike. This constructor should be used when the spike isn't going to move.
     * 
     * @param rotation How much the spike is going to be rotated clockwise in degrees.
     */
    public Spike(int rotation) {
        turn(rotation);
        moving = false;
        inWorld = true;
    }
    
    /**
     * Constructor for class Spike. This constructor should be used when the spike will move.
     * 
     * @param rotation  How much the spike is going to be rotated clockwise in degrees.
     * @param x         True if the spike will move in the x direction, false if it will move in the y direction.
     * @param direction If moving in the x direction, 1 for moving right and -1 for moving left. 
     *                  If moving in the y direction, 1 for moving up and -1 for moving down.
     * @param speed     How fast the spike will be moving.
     */
    public Spike(int rotation, boolean x, int direction, int speed)
    {
        turn(rotation);
        moving = false;
        inWorld = true;
        this.x = x;
        this.direction = direction;
        this.speed = speed;
    }

    public void act() 
    {
        //checks for intersection with player
        Player p = (Player)getOneTouchedObject(Player.class);
        if (p != null) {
            p.onGround(); //call this method so player won't accelerate downward anymore
            p.setAlive(false); //set player's alive variable to false
        }
        
        move(); //moves spike
        //checks if spike is at the edge of the world and if it is, set inWorld to false
        if (getX() >= 1023 || getX() <= 1 || getY() >= 575 || getY() <= 1) {
            inWorld = false;
        }
        //remove spike if it is not in world
        if (!inWorld)
            getWorld().removeObject(this);
    }   

    /**
     * Moves the spike.
     */
    private void move()
    {
        //if it is moving up or down
        if (moving && !x) {
            setLocation (getX(), getY() - speed*direction);
        } else if (moving && x) { //if it is moving left or right
            setLocation(getX() + speed*direction, getY());
        }
    }

    /**
     * Accessor for boolean moving.
     * 
     * @return boolean True if object is moving.
     */
    public boolean getMoving()
    {
        return moving;
    }
    
    /**
     * Mutator for boolean moving.
     * 
     * @param moving True if object is moving.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * Accessor for boolean inWorld.
     * 
     * @return boolean True if object is in world.
     */
    public boolean getInWorld() {
        return inWorld;
    }
    
    /**
     * Mutator for boolean inWorld.
     * 
     * @param inWorld True if object is in world.
     */
    public void setInWorld(boolean inWorld) {
        this.inWorld = inWorld;
    }
    
    /**
     * Taken from Busch2207 at the Greenfoot forums.
     * 
     * http://www.greenfoot.org/topics/260
     */
    public Actor getOneTouchedObject(Class clss)  
    {  
        List<Actor> list = getIntersectingObjects(clss);  
        List<Actor> list2 = getIntersectingObjects(clss);  
        list2.clear();  
        GreenfootImage i=new GreenfootImage(getImage());  
        i.rotate(getRotation());  
        int w=i.getWidth(),h=i.getHeight(),x=getX(),y=getY();  
        for(Actor A : list)  
        {  
            GreenfootImage Ai = new GreenfootImage(A.getImage()), i2 = new GreenfootImage(w,h);  
            Ai.rotate(A.getRotation());  
            int Aw=Ai.getWidth(),Ah=Ai.getHeight(),Ax=A.getX(),Ay=A.getY();  
            i2.drawImage(Ai,Ax-x-(Aw/2-w/2),Ay-y-(Ah/2-h/2));  
            for(int yi = 0; yi<h; yi++)  
                for(int xi = 0; xi<w; xi++)  
                    if(i2.getColorAt(xi,yi).getAlpha()>0 && i.getColorAt(xi,yi).getAlpha()>0)  
                        return A;  
        }  
        return null;  
    }
    
    /**
     * CODE BELOW DOES NOT WORK SOMETIMES DUE TO GREENFOOT BUG?
     * This method checks for player detection with the spike. Sometimes it works, sometimes it doesn't.
     * 
     * http://i.imgur.com/OwZhV51.png
     */
    //     private void checkPlayerHit() {
    //         //get hit detection with spikes
    //         Player p = (Player)getOneIntersectingObject(Player.class);
    //         int x, y;
    //         if (p != null) {
    //             //if the spike is pointing up
    //             if (rotation == 0) {
    //                 for (int i = 1; i < 7; i++) {
    //                     //check coordinates of the outside of the spike to make collision detection precise
    //                     switch (i) {
    //                         case 1:
    //                         x = 16;
    //                         y = 16;
    //                         break;
    // 
    //                         case 2:
    //                         x = 13;
    //                         y = 8;
    //                         break;
    // 
    //                         case 3:
    //                         x = 10;
    //                         y = 2;
    //                         break;
    // 
    //                         case 4:
    //                         x = 8;
    //                         y = -2;
    //                         break;
    // 
    //                         case 5:
    //                         x = 4;
    //                         y = -9;
    //                         break;
    // 
    //                         case 6:
    //                         x = 0;
    //                         y = -16;
    //                         break;
    // 
    //                         default:
    //                         x = 0;
    //                         y = 0;
    //                     }
    //                     //check if player touches left side of spike
    //                     Player p2 = (Player)getOneObjectAtOffset(-x, y, Player.class);
    //                     if (p2 == null)
    //                         p2 = (Player)getOneObjectAtOffset(x, y, Player.class); //check right side of spike
    //                     if (p2 != null) {
    //                         p2.onGround(); //call this method so player won't accelerate downward anymore
    //                         p2.setAlive(false); //set player's alive variable to false
    //                         p2.setRestriction(true); //don't allow the player to move anymore
    //                         break; //break out of for loop
    //                     }
    //                 }
    //             } else if (rotation == 180) {
    //                 //if spike is pointing down
    //                 for (int i = 1; i < 7; i++) {
    //                     //check coordinates of the outside of the spike to make collision detection precise
    //                     switch (i) {
    //                         case 1:
    //                         x = 16;
    //                         y = -16;
    //                         break;
    // 
    //                         case 2:
    //                         x = 13;
    //                         y = -8;
    //                         break;
    // 
    //                         case 3:
    //                         x = 10;
    //                         y = -2;
    //                         break;
    // 
    //                         case 4:
    //                         x = 8;
    //                         y = 2;
    //                         break;
    // 
    //                         case 5:
    //                         x = 4;
    //                         y = 9;
    //                         break;
    // 
    //                         case 6:
    //                         x = 0;
    //                         y = 16;
    //                         break;
    // 
    //                         default:
    //                         x = 0;
    //                         y = 0;
    //                     }
    //                     //check if player touches left side of spike
    //                     Player p2 = (Player)getOneObjectAtOffset(-x, y, Player.class);
    //                     if (p2 == null)
    //                         p2 = (Player)getOneObjectAtOffset(x, y, Player.class); //check right side of spike
    //                     if (p2 != null) {
    //                         p2.onGround(); //call this method so player won't accelerate downward anymore
    //                         p2.setAlive(false); //set player's alive variable to false
    //                         p2.setRestriction(true); //don't allow the player to move anymore
    //                         break; //break out of for loop
    //                     }
    //                 }
    //             } else if (rotation == 90) {
    //                 //if spike is pointing right
    //                 for (int i = 1; i < 7; i++) {
    //                     //check coordinates of the outside of the spike to make collision detection precise
    //                     switch (i) {
    //                         case 1:
    //                         x = -16;
    //                         y = 16;
    //                         break;
    // 
    //                         case 2:
    //                         x = -8;
    //                         y = 13;
    //                         break;
    // 
    //                         case 3:
    //                         x = -2;
    //                         y = 10;
    //                         break;
    // 
    //                         case 4:
    //                         x = 2;
    //                         y = 8;
    //                         break;
    // 
    //                         case 5:
    //                         x = 9;
    //                         y = 4;
    //                         break;
    // 
    //                         case 6:
    //                         x = 16;
    //                         y = 0;
    //                         break;
    // 
    //                         default:
    //                         x = 0;
    //                         y = 0;
    //                     }
    //                     //check if player touches top side of spike
    //                     Player p2 = (Player)getOneObjectAtOffset(x, -y, Player.class);
    //                     if (p2 == null)
    //                         p2 = (Player)getOneObjectAtOffset(x, y, Player.class); //check bottom side of spike
    //                     if (p2 != null) {
    //                         p2.onGround(); //call this method so player won't accelerate downward anymore
    //                         p2.setAlive(false); //set player's alive variable to false
    //                         p2.setRestriction(true); //don't allow the player to move anymore
    //                         break; //break out of for loop
    //                     }
    //                 }
    //             } else if (rotation == 270) {
    //                 //if spike is pointing left
    //                 for (int i = 1; i < 7; i++) {
    //                     //check coordinates of the outside of the spike to make collision detection precise
    //                     switch (i) {
    //                         case 1:
    //                         x = 16;
    //                         y = 16;
    //                         break;
    // 
    //                         case 2:
    //                         x = 8;
    //                         y = 13;
    //                         break;
    // 
    //                         case 3:
    //                         x = 2;
    //                         y = 10;
    //                         break;
    // 
    //                         case 4:
    //                         x = -2;
    //                         y = 8;
    //                         break;
    // 
    //                         case 5:
    //                         x = -9;
    //                         y = 4;
    //                         break;
    // 
    //                         case 6:
    //                         x = -16;
    //                         y = 0;
    //                         break;
    // 
    //                         default:
    //                         x = 0;
    //                         y = 0;
    //                     }
    //                     //check if player touches top side of spike
    //                     Player p2 = (Player)getOneObjectAtOffset(x, -y, Player.class);
    //                     if (p2 == null)
    //                         p2 = (Player)getOneObjectAtOffset(x, y, Player.class); //check bottom side of spike
    //                     if (p2 != null) {
    //                         p2.onGround(); //call this method so player won't accelerate downward anymore
    //                         p2.setAlive(false); //set player's alive variable to false
    //                         p2.setRestriction(true); //don't allow the player to move anymore
    //                         break; //break out of for loop
    //                     }
    //                 }
    //             }
    //         }
    //     }
}