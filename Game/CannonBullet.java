import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A bullet for the canon.
 * 
 * @author Ernest Wong 
 * @version June 2014
 */
public class CannonBullet extends Background
{
    public CannonBullet()
    {
        speed = 4;
        isMove = true;
        direction = -1;
    }

    public void act() 
    {
        move(speed,direction); //move the cannon
        if (atWorldEdge()) //if at world edge
        {
            getWorld().removeObject(this);  //remove the cannon from the world
        }
    }

    /**
     * Detect if the bullet is located at the end of the map or not.
     * 
     * @return true if the bullet is at world edge. Otherwise, false.
     */
    private boolean atWorldEdge()
    {
        if (getX() >= 985)
            return true;        
        else
            return false;
    }
}
