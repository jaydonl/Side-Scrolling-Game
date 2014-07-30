import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * DK is another immobile canon type enemy, launching barrels across the screen
 * 
 * @author Jaydon Lau and Jason Fok
 * @version June 2014
 */
public class DK extends Enemy
{
    //declare variables
    private GreenfootImage[] animation = new GreenfootImage[4];
    // Integers for controlling the speed of the movement and animation
    private int animationCounter = 0;
    private int animationDelay = 50;
    private int animationDelayCounter = 0;
    
    Player p1;
    /**
     * Constructor for DK class.
     */
    public DK() 
    {
        // Three Strings together to build file name
        hp = 10;
        alive = true;
        p1 = PlatformWorld.getPlayer();
        String fileName;
        String fileNamePrefix = "DK";
        String fileNameSuffix = ".png";
        //populate arrays with images
        for (int i = 0; i < 4; i++) {
            //build file name for facing right image
            fileName = fileNamePrefix + (i+1) + fileNameSuffix;
            //put file into appropriate array
            animation[i] = new GreenfootImage(fileName);
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
            if (p1.getRespawn()) {
                p1 = PlatformWorld.getPlayer();
            }
            if (this.intersects(p1)) {
                p1.setAlive(false);
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
            if (animationCounter > animation.length -1)
                animationCounter = 0;
            animate();
        }
        else {
            getWorld().removeObject(this);
        }
    }

    /**
     * Cycles through the DK animation, firing a barrel upon reaching the proper frame 
     */
    public void animate() {
        setImage (animation[animationCounter]);
        if (animationCounter == 3 && animationDelayCounter == 49 ) {
            getWorld().addObject(new Barrel(), getX(), getY());
        }
    }

    /**
     * Accessor method to get the current animation counter
     */
    public int getAnimationCounter() {
        return animationCounter;
    }
}
