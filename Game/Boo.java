import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Boo is an enemy that moves and turns towards the player if the player is facing away from the Boo
 * If the player is facing towards the Boo, Boo will go into a sleeping animation
 * 
 * @author Jaydon Lau 
 * @version June 2014
 */
public class Boo extends Enemy
{
    //boolean to check boo's direction and the player's direction
    private boolean facingPlayer;
    private boolean facingRight;
    private boolean awake;
    private boolean inRange;
    private Player p1;
    private int distanceX;

    private int playerRespawnTimer = 100;
    private int booTimeOutCounter = 0;
    private boolean attackPlayer = true;

    /**
     * Constructor for Boo, allowing for the direction that Boo is facing to be set
     * @param direction The direction Boo is facing 
     */
    public Boo (boolean direction) {
        // sets all the proper information
        facingRight = direction;
        hp = 3;
        alive = true;
        mySpeed = 3;
        p1 = PlatformWorld.getPlayer();
        // sets the Boo image according to the direction facing 
        if (facingRight) {
            setImage ("Boo2Right.png");
        }
        else {
            setImage ("Boo2Left.png");
        }
    }

    /**
     * Act - do whatever the Boo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (hp <= 0) {
            alive = false;
        }
        // Add your action code here.
        if (alive) {
            // resets player reference if the player respawns 
            if (p1.getRespawn()) {
                p1 = PlatformWorld.getPlayer();
                attackPlayer = false;
            }
            // doesnt allow boo to kill player once they respawned for a set time
            if (!attackPlayer) {
                booTimeOutCounter++;
                if (booTimeOutCounter == playerRespawnTimer) {
                    attackPlayer = true;
                }
                else if (booTimeOutCounter > playerRespawnTimer) {
                    booTimeOutCounter = 0;
                }

            }
            // checks for change in direction and sleep
            checkSleep();
            checkChangeDirection();
            // checks for the player range
            Actor temp = getOneObjectAtOffset (30, 30, Player.class);
            if (temp != null) {
                inRange = true;
                mySpeed = 5;
            } 
            else {
                inRange = false;
            }
            resetImage();
            if (awake) {
                // changes direction upon waking up 
                resetImage();
                // kills player upon contact 
                if (this.intersects(p1)){
                    if (attackPlayer) {
                        p1.setAlive (false);
                    }
                }
                else {
                    moveTowardsPlayer();
                }
            }
        }

        else {
            getWorld().removeObject(this);
        }
    }    

    /**
     * Checks if Boo is facing the same direction as the player.
     * If so, then puts Boo to sleep; otherwise, Boo wakes up 
     */
    private void checkSleep () {
        boolean playerDirection = p1.facingRight();
        if (playerDirection == facingRight) {
            awake = true;
        }
        else {
            awake = false;
        }
    }

    /**
     * Turns Boo towards the player and moves Boo closer to the player
     */
    private void moveTowardsPlayer() {
        int playerX = p1.getX();
        int playerY = p1.getY();
        if (facingRight) {
            turnTowards (playerX, playerY); 
            move (mySpeed);
        }
        else {
            turnTowards (playerX, playerY);
            setRotation (getRotation() +180);
            move (-mySpeed);
        }

    }

    /**
     * Checks if Boo needs to change direction depending on the player's new location
     * 
     */
    private void checkChangeDirection () {
        if (facingRight) {
            // if player's coordinate is less than Boo, then direction needs to be changed
            if (p1.getX() < this.getX()) {
                facingRight = false;
            }
        }
        else {
            // vice versa
            if (p1.getX() > this.getX()) {
                facingRight = true;
            }
        }
    }

    /**
     * Resets the image according to specific information 
     * Ex. direction, awake, and if boo is in range of the player
     */
    private void resetImage () {
        if (facingRight && awake && !inRange) {
            setImage ("Boo2Right.png");
        }
        else if (facingRight && awake && inRange) {
            setImage ("Boo3Right.png");
        }
        else if (facingRight && !awake) {
            setImage ("Boo4Left.png");
        }
        else if (!facingRight && awake && !inRange) {
            setImage ("Boo2Left.png");
        }
        else if (!facingRight && awake && inRange) {
            setImage ("Boo3Left.png");
        }
        else {
            setImage ("Boo4Right.png");
        }
    }
}
