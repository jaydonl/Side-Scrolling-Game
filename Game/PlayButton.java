import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Play button that appears on the title screen.
 * Increases in size when the user puts their mouse over the button.
 * <p>
 * Changelog v0.1:
 * Play button gets bigger depending on whether or not the mouse is on it.
 * 
 * @author Terrence Hung 
 * @version 0.1
 */
public class PlayButton extends Actor
{
    /**
     * Changes size of the play button depending whether or not the mouse is on it.
     */
    public void changeSize()
    {
        //if mouse is on the play button, make it bigger
        if(Greenfoot.mouseMoved(this))
            setImage("play button bigger.png");
        //if mouse is on the background, return play button to regular size
        else if(Greenfoot.mouseMoved(getWorld()))
            setImage("play button.png");
    }
}
