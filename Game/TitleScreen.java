import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Title screen that displays the name of the game and gives the user two options, to play or to read the instructions.
 * <p>
 * Changelog v0.1:
 * Title screen has now been included.
 * Play and instructions buttons have been added.
 * Main program starts when play button is clicked on.
 * 
 * @author Terrence Hung
 * @version 0.1
 */
public class TitleScreen extends World
{
    //declare variables
    private PlayButton playButton;
    private InstructionsButton instructionsButton;
    /**
     * Constructor for objects of class TitleScreen.
     * 
     */
    public TitleScreen()
    {    
        // Create a new world with 1024x576 cells with a cell size of 1x1 pixels.
        super(1024, 576, 1); 
        //initialize and add play button to screen
        playButton = new PlayButton();
        addObject(playButton, 285, 506);
        //initialize and add instructions button to screen
        instructionsButton = new InstructionsButton();
        addObject(instructionsButton, 734, 506);
    }

    /**
     * Checks for mouse movement and clicks.
     */
    public void act()
    {
        //check if mouse is on a button or if it clicked a button
        checkMouse();
    }

    /**
     * Checks for mouse movement and clicks.
     */
    public void checkMouse()
    {
        //change the size of the play button if the mouse is on it
        playButton.changeSize();
        //start the game if the player clicks on the play button
        if(Greenfoot.mouseClicked(playButton))
            Greenfoot.setWorld(new LoadGameScreen());
        //change size of the instructions button if the mouse is on it
        instructionsButton.changeSize();
        //go to instructions if player clicks on instructions button
        if(Greenfoot.mouseClicked(instructionsButton))
            Greenfoot.setWorld(new InstructionsMenu());
    }
}
