import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Projectile superclass contains the subclasses PlayerShot and Fireball.
 * Each projectile is spawned by an object and is removed when it touches the edge of the world.
 * 
 * @author Terrence Hung
 * @version June 13, 2014
 */
public class Projectile extends Actor
{
    //declare variable
    protected boolean inWorld = true;

    /**
     * Checks if the projectile is at the edge of the world.
     */
    protected void checkWorldEdge() {
        if (getX() >= 1023)
            inWorld = false;
        else if (getX() <= 1)
            inWorld = false;
    }

    /**
     * Removes the object if it isn't supposed to be in the world anymore.
     */
    protected void remove() {
        if (!inWorld)
            getWorld().removeObject(this);
    }
}
