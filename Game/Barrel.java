import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Barrel is a class that kills the player on detection, and is launched by the DK class. 
 * Barrel will turn directions if they hit a block, and will not fall through blocks. 
 * Barrel will be removed if it hits a spike. 
 * 
 * @author Jason Fok and Jaydon Lau
 * @version June 2014
 */
public class Barrel extends Actor
{
    // boolean for direction and airborne
    private boolean airborne = false;
    private boolean facingRight;
    private int vSpeed = 0;
    /**
     * Creates a Barrel that is facing right
     */
    public Barrel()
    {
        facingRight = true;
    }

    /**
     * Act - do whatever the Barrel wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        roll();
    }    

    /**
     * Rolls the barrel, detecting blocks and the player
     * The barrel will gradually fall if it is airborne, and continues to move right across the screen, unless it hits something
     */
    public void roll() {
        // detects spike
        Actor spike = getOneIntersectingObject (Spike.class);
        if (spike != null) {
            getWorld().removeObject(this);
        }
        else {
            // detects player, and all the different types of blocks in both directions
            Player player = (Player) getOneIntersectingObject (Player.class);
            GrassBlock gY = (GrassBlock)getOneObjectAtOffset(0, 16, GrassBlock.class);
            GrassBlock gRightX = (GrassBlock)getOneObjectAtOffset(16, 0, GrassBlock.class);
            GrassBlock gLeftX = (GrassBlock) getOneObjectAtOffset (-16, 0, GrassBlock.class);
            Block bY = (Block)getOneObjectAtOffset(0, 13, Block.class);
            Block bRightX = (Block)getOneObjectAtOffset(13, 0, Block.class);
            Block bLeftX = (Block) getOneObjectAtOffset (-13, 0, Block.class);
            Brick brRightX = (Brick) getOneObjectAtOffset (13, 0, Brick.class);
            Brick brLeftX = (Brick) getOneObjectAtOffset (-13, 0, Brick.class);
            Brick brY = (Brick) getOneObjectAtOffset (0, 13, Brick.class);
            if (player != null) {
                player.setAlive(false);
            }
            // falls gradually when it is airborne 
            if (airborne) {
                vSpeed++;
                setLocation(getX(), getY() + vSpeed);
            }
            // reverses direction upon hitting a block
            if (facingRight) {
                if (gRightX != null || bRightX != null || brRightX != null) {
                    facingRight = false;
                }
            }
            else {
                if (gLeftX != null || bLeftX != null || brLeftX != null) {
                    facingRight = true;
                }
            }
            // if a block is hit, barrel is no longer airborne 
            if (gRightX != null || gLeftX != null || gY != null || bRightX != null || bLeftX != null || bY != null || brRightX != null || brLeftX != null || brY != null) {
                vSpeed = 0;
                airborne = false;
            } else {
                airborne = true;
                vSpeed = 5;
            }
            // moves the barrel in the x direction
            if (facingRight) {
                setLocation (getX() + 5, getY());
            }
            else {
                setLocation (getX() - 5, getY());
            }
        }
    }
}
