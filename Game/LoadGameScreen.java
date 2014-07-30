import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Gives the option to the user to either start a new game or resume the last level they were on before closing the game
 * 
 * @author Jason Fok 
 * @version June 2014
 */
public class LoadGameScreen extends World
{
    //declare variables
    private NewGameButton newGameButton;
    private LoadPreviousGameButton loadPreviousGameButton;
    private Scanner scan;
    private int level;

    /**
     * Constructor for objects of class TitleScreen.
     * 
     */
    public LoadGameScreen()
    {    
        // Create a new world with 1024x576 cells with a cell size of 1x1 pixels.
        super(1024, 576, 1); 
        //initialize and add play button to screen
        newGameButton = new NewGameButton();
        addObject(newGameButton, 298, 226);
        //initialize and add instructions button to screen
        loadPreviousGameButton = new LoadPreviousGameButton();
        addObject(loadPreviousGameButton, 733, 226);

        try {
            //load the text file into a 2D array
            scan = new Scanner (new File ("savedLevel.txt"));
            while (scan.hasNext())
            {
                level = Integer.parseInt(scan.nextLine());
            }
        }
        catch (FileNotFoundException e)
        {
        }
        finally
        {
            if (scan != null)
            {
                //close the scanner
                scan.close();
            }
        }
        //When the level does not exist, set the level to 1
        if (level < 1 || level > 6 ) {
            level = 1;
        }
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
        newGameButton.changeSize();
        //start the game if the player clicks on the New Game Button
        if(Greenfoot.mouseClicked(newGameButton))
            Greenfoot.setWorld(new PlatformWorld());
        //change size of the instructions button if the mouse is on it
        loadPreviousGameButton.changeSize();
        //go to level where the game was last exited if player clicks on Load previous game button
        if(Greenfoot.mouseClicked(loadPreviousGameButton))
            Greenfoot.setWorld(new PlatformWorld(level));
    }

}
