import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Ryu acts as a immobile canon type enemy, only firing hadouken across the screen
 * 
 * @author Jason Fok
 * @version June 2014
 */
public class Ryu extends Enemy
{
    //declare variables
    private GreenfootImage[] hadoukenRight = new GreenfootImage[3];
    // Integers for controlling the speed of the movement and animation
    private int animationCounter = 0;
    private int animationDelay = 50;
    private int animationDelayCounter = 0;


    private GreenfootSound hadouken = new GreenfootSound("hadouken.mp3");
    /**
     * Constructor for Ryu class.
     */
    public Ryu() 
    {
        // Three Strings together to build file name
        hp = 5;
        alive = true;
        String fileName;
        String fileNamePrefix = "Ryu";
        String fileNameSuffix = ".png";
        //populate arrays with images
        for (int i = 0; i < 3; i++) {
            //build file name for facing right image
            fileName = fileNamePrefix + (i+1) + fileNameSuffix;
            //put file into appropriate array
            hadoukenRight[i] = new GreenfootImage(fileName);
        }
    }

    /**
     * Act - do whatever the Guy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (hp <= 0) {
            alive = false;
        }
        if (alive) {
            // animationDelayCounter is used to avoid the animation happening
            // too fast. Each act it increases by 1 until it hits *animationDelay*.
            animationDelayCounter++;
            if(animationDelayCounter == animationDelay)
            {
                animationCounter++;
                animationDelayCounter = 0;
            }
            // Animation counter controls which frame is currently being displayed.
            // If it gets beyond the size of our array of images, reset it to zero
            if (animationCounter > hadoukenRight.length -1)
                animationCounter = 0;
            hadoukenRight();
        }
        else {
            getWorld().removeObject(this);
        }
    }

    /**
     * Moves the player to the right and controls right movement animation.
     */
    public void hadoukenRight() {
        setImage (hadoukenRight[animationCounter]);

        if (animationCounter == 1 && animationDelayCounter == 25) {
            if (hadouken.isPlaying()){
                hadouken.stop();
            }
            hadouken.play();
        }
        if (animationCounter == 2 && animationDelayCounter == 0 ) {
            getWorld().addObject(new Fireball(true), getX(), getY());
        }

    }

}
