import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Fireballs are launched by Ken and Ryu, and shoot across the screen, killing the player upon contact
 * 
 * @author Jason Fok
 * @version June 2014
 */
public class Fireball extends Projectile
{

    //declare variables
    private GreenfootImage[] movingLeft = new GreenfootImage[2];
    private GreenfootImage[] movingRight = new GreenfootImage[2];
    // Two booleans for controlling the flow of the action
    private boolean facingRight = true;
    // Integers for controlling the speed of the movement and animation
    private int animationCounter = 0;
    private int animationDelay = 5;
    private int animationDelayCounter = 0;

    private Player p1;
    private boolean checkPlayer;
    /**
     * Constructor for Fireball class.
     */
    public Fireball(boolean facingRight)
    {
        p1 = PlatformWorld.getPlayer();
        this.facingRight = facingRight;
        // Three Strings together to build file name
        String fileName;
        String fileNamePrefix = "fireball";
        String fileNameSuffix = ".png";
        //populate arrays with images
        for (int i = 0; i < 2 ; i++) {
            //build file name for facing left image
            fileName = fileNamePrefix + "Left" + (i+1) + fileNameSuffix;
            //put file into appropriate array
            movingLeft[i] = new GreenfootImage(fileName);
            //build file name for facing right image
            fileName = fileNamePrefix + "Right" + (i+1) + fileNameSuffix;
            //put file into appropriate array
            movingRight[i] = new GreenfootImage(fileName);
        }
    }

    /**
     * Act - do whatever the Fireball wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (p1.getRespawn()) {
            p1 = PlatformWorld.getPlayer();
        }
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
        if (animationCounter > movingRight.length -1) {
            animationCounter = 0;
        }
        if (facingRight) {
            moveRight();
        }
        else {
            moveLeft();
        }
        checkHit();
        checkWorldEdge();
        remove();
    }    

    /**
     * Moves the player to the right and controls right movement animation.
     */
    public void moveRight() {
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (movingRight[animationCounter]);
        setLocation (getX() + 5, getY());
    }

    /**
     * Moves the player to the right and controls right movement animation.
     */
    public void moveLeft() {
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (movingRight[animationCounter]);
        setLocation (getX() - 5, getY());
    }

    /**
     * Check if the fireball hit the player and kill the player is so
     */
    public void checkHit() {
        checkPlayer = this.intersects(p1);
        if (checkPlayer) {
            p1.setAlive(false);
            inWorld = false;
        }
    }
}
