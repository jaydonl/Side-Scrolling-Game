import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Ending Screen after the player beats ken
 * 
 * @author (Jason Fok) 
 * @version (June 2014)
 */
public class EndingScreen extends World
{
    private int delay = 0;
    /**
     * Constructor for objects of class EndingScreen.
     * 
     */
    public EndingScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1024, 576, 1); 
    }

    public void act() {
        delay++;
        if (delay > 500) {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
}
