import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Background tiles
 * 
 * @author Ernest Wong 
 * @version June 2014
 */
public abstract class Background extends Actor
{
    //true if the object is moving
    protected boolean isMove;
    //moving direction of the object (1 - left, -1 - right)
    protected int direction;
    //speed of the object
    protected int speed;
    
    protected void move(int direction, int speed)
    {
        this.direction = direction;
        this.speed = speed;
        //move the object
        setLocation(getX()-(speed*direction),getY());
    }

    /**
     * Accessor for the boolean isMove
     * 
     * @return true if the tile is moving. Otherwise, false.
     */
    protected boolean isMove()
    {
        return isMove;
    }

    /**
     * Set the boolean isMove true.
     */
    protected void moving()
    {
        isMove = true;
    }

    /**
     * Accessor for the speed of the tile
     * 
     * @return speed of the tile
     */
    protected int getSpeed()
    {
        return speed;
    }
    
    /**
     * Accessor for the direction of the tile
     * 
     * @return 1 if the tile is moving left. -1 if the tile is moving right.
     */
    protected int getDirection()
    {
        return direction;
    }
}
