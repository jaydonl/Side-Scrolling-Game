import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Instructions button that appears on the title screen.
 * Increases in size when the user puts their mouse of the button.
 * <p>
 * Changelog v0.1:
 * Gets bigger when mouse goes over it but clicking it does not do anything.
 * 
 * @author Terrence Hung
 * @version 0.1
 */
public class InstructionsButton extends Actor
{
    /**
     * Changes size of the instructions button depending whether or not the mouse is on it.
     */
    public void changeSize()
    {
        //if mouse is on the instructions button, make it bigger
        if(Greenfoot.mouseMoved(this))
            setImage("instructions button bigger.png");
        //if mouse is on the background, return the instructions button back to normal
        else if(Greenfoot.mouseMoved(getWorld()))
            setImage("instructions button.png");
    }    
}
