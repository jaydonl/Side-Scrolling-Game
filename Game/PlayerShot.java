import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A projectile that is fired from the player.
 * The projectile is able to deal damage to enemies, and is removed from the world when it touches an enemy or the edge of the world.
 *   
 * @author Terrence Hung
 * @version June 12, 2014
 */
public class PlayerShot extends Projectile
{
    //declare variables
    private int speed;
    private GreenfootImage[] sprites = new GreenfootImage[3];
    //variables for controlling the speed of the animation
    private int animationCounter = 0;
    private int animationDelay = 3;
    private int animationDelayCounter = 0;

    /**
     * Constructor for class PlayerShot.
     * 
     * @param right True if projectile is moving right, false if moving left.
     */
    public PlayerShot(boolean right) {
        //variables used to build file names
        String fileName;
        String fileNamePrefix = "shot ";
        String fileNameSuffix = ".png";
        if (right) {
            speed = 7; //set projectile speed
            //use for loop to populate image array
            for (int i = 0; i < 3; i++) {
                fileName = fileNamePrefix + (i+1) + fileNameSuffix;
                //put file into array
                sprites[i] = new GreenfootImage(fileName);
            }
            setImage(sprites[0]); //set the image the actor will be spawned with
        } else {
            speed = -7; //set projectile speed
            //use for loop to populate image array
            for (int i = 0; i < 3; i++) {
                fileName = fileNamePrefix + (i+1) + fileNameSuffix;
                //put file into array
                sprites[i] = new GreenfootImage(fileName);
                sprites[i].mirrorHorizontally(); //flip the picture because the projectile is facing left
            }
            setImage(sprites[0]); //set the image the actor will be spawned with
        }
    }

    /**
     * Changes the location of the projectile, animates the sprite, and collides with enemies/walls.
     */
    public void act() 
    {
        setLocation(getX()+speed, getY()); //change the projectile's location
        animationDelayCounter++;
        animate();
        checkCollision();
        checkWorldEdge();
        checkObject();
        remove();
    }    

    /**
     * Controls the animation of the projectile.
     */
    private void animate() {
        if (animationDelayCounter == animationDelay) {
            setImage(sprites[animationCounter]);
            animationCounter++;            
            animationDelayCounter = 0;
        }
        if (animationCounter > 2)
            animationCounter = 0;
    }

    /**
     * Checks if the projectile is touching a block, spike, cannon, or cannon bullet.
     */
    private void checkObject() {
        if (inWorld) {
            GrassBlock g = (GrassBlock)getOneIntersectingObject(GrassBlock.class);
            Block b = (Block)getOneIntersectingObject(Block.class);
            Spike s = (Spike)getOneIntersectingObject(Spike.class);
            Brick br = (Brick)getOneIntersectingObject(Brick.class);
            CannonBullet cb = (CannonBullet)getOneIntersectingObject(CannonBullet.class);
            Cannon c = (Cannon)getOneIntersectingObject(Cannon.class);
            if (g != null || b != null || s != null || br != null || cb != null || c != null)
                inWorld = false;
        }
    }

    /**
     * Checks if the projectile touches any enemy and deals damage to them if it is touching.
     */
    private void checkCollision() {
        //check for any enemy that the projectile can hit
        Boo b = (Boo)getOneIntersectingObject(Boo.class);
        Ken k = (Ken)getOneIntersectingObject(Ken.class);
        Ryu r = (Ryu)getOneIntersectingObject(Ryu.class);
        DK dk = (DK)getOneIntersectingObject(DK.class);
        //deal damage to the enemy that the projectile hit
        if (b != null) {
            b.changeHP(1);
            inWorld = false;
        } else if (k != null) {
            k.changeHP(1);
            k.getHealthBar().update(-1);
            inWorld = false;
        } else if (r != null) {
            r.changeHP(1);
            inWorld = false;
        } else if (dk != null) {
            dk.changeHP(1);
            inWorld = false;
        }
    }
}
