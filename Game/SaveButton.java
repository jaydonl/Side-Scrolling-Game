import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * A button that will save the player's current location.
 * Spawn the player at that location if the player dies.
 * 
 * @author Ernest Wong
 * @version June 2014
 */
public class SaveButton extends Actor
{
    public SaveButton()
    {

    }

    public void act() 
    {
        
    }    

    /**
     * Remove the save button from the world.
     */
    public void remove()
    {
        getWorld().removeObject(this);
    }
}
