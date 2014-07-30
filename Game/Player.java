import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * This is the character that the player controls.
 * The player is able to move around, jump, and shoot projectiles.
 * The player also has multiple sprites to allow for smooth animation.
 * Animation code was taken from Mr. Cohen's animation example from ICS3U1.
 * 
 * @author Terrence Hung
 * @version June 13, 2014
 */
public class Player extends Actor
{
    //gravity constant
    public static final float GRAVITY = 8e-1f;
    //declare variables
    private GreenfootImage[] movingLeft = new GreenfootImage[5];
    private GreenfootImage[] movingRight = new GreenfootImage[5];
    private GreenfootImage standingLeft = new GreenfootImage("player left.png");
    private GreenfootImage standingRight = new GreenfootImage("player right.png");
    // Two booleans for controlling the flow of the action
    private boolean moving = false;
    private boolean facingRight = true;
    // Integers for controlling the speed of the movement and animation
    private int animationCounter = 0;
    private int animationDelay = 5;
    private int animationDelayCounter = 0;
    private int speed = 3;
    //variables to control player falling
    private boolean airborne = false;
    private float vSpeed = 0;

    private boolean doubleJump = false;
    private GreenfootSound jump1 = new GreenfootSound("jump 1.mp3");
    private GreenfootSound jump2 = new GreenfootSound("jump 2.mp3");
    private GreenfootSound land = new GreenfootSound("land reduced.mp3");
    private boolean playedLandingSound = true;
    private boolean hitWall = false;
    private GrassBlock g;
    private Block b;
    private int transparency = 255;
    private boolean rotated = false;
    private boolean restrictMovement = false;
    private boolean respawn = false;
    private GreenfootSound scream = new GreenfootSound("scream.wav");
    private boolean alive = true;
    private boolean playedScream = false;
    private int shootDelay = 0;
    private boolean shot = false;
    private boolean isSaved = false;
    private Brick br;
    private int floorSpeed = 0;
    private CannonBullet cb;
    private Cannon c;
    /**
     * Constructor for Player class.
     * 
     * @param isSaved True if the player has reached a save point.
     */
    public Player(boolean isSaved) 
    {
        // Three Strings together to build file name
        String fileName;
        String fileNamePrefix = "player move ";
        String fileNameSuffix = ".png";
        //populate arrays with images
        for (int i = 0; i < 5; i++) {
            //build file name for facing left image
            fileName = fileNamePrefix + "left " + (i+1) + fileNameSuffix;
            //put file into appropriate array
            movingLeft[i] = new GreenfootImage(fileName);
            //build file name for facing right image
            fileName = fileNamePrefix + "right " + (i+1) + fileNameSuffix;
            //put file into appropriate array
            movingRight[i] = new GreenfootImage(fileName);
        }
        this.isSaved = isSaved; //set value for isSaved according to parameter
    }

    /**
     * Controls all of the player's actions.
     */
    public void act() 
    {
        if (alive) {
            //control animation delays
            animationDelays();
            //make player accelerate downward if they are in the air
            fall();
            //prevents player from moving into a wall
            checkWall();
            //prevents player from going through the bottom of a block while jumping
            checkCeiling();
            //prevent the player from sinking into the floor when they land
            checkFloor();
            //prevents player from being partially inside the block after falling
            fixInsideBlock();
            //update the player's location, only has an effect if player is standing on top of a moving platform
            setLocation(getX() + floorSpeed, getY());
            //controls the player's shooting
            //if the player has shot a projectile
            if (shot)
                shootDelay++; //increase shoot delay
            //if shoot delay equals 15
            if (shootDelay == 15) {
                shootDelay = 0; //reset shoot delay
                shot = false; //allow player to shoot another projectile
            }
            //if player touched a save button, then set isSaved to true and remove save button
            SaveButton sb = (SaveButton)getOneIntersectingObject(SaveButton.class);
            if (sb != null) {
                isSaved = true;
                sb.setLocation(-69,-69); //IF U KNOW WHAT I MEAN
            }
        }
        //if player isn't alive then call dead method
        if (!alive)
            dead();
    }

    /**
     * Moves the player to the left and controls left movement animation.
     */
    public void moveLeft() {
        // Check if direction is changing, and if so, reset counter
        if (facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = true;
        facingRight = false;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (movingLeft[animationCounter]);
        //move player if they are not touching a wall on their left
        if (!hitWall) {
            //if player is on a moving floor going in the opposite direction then let the player move more
            if (floorSpeed > 0)
                setLocation(getX() - floorSpeed - speed, getY());  
            else
                setLocation(getX() - speed, getY());
        }
    }

    /**
     * Moves the player to the right and controls right movement animation.
     */
    public void moveRight() {
        // Check if direction is changing, and if so, reset counter
        if (!facingRight)
            animationCounter = 0;
        // Set control booleans to facing right and walking
        moving = true;
        facingRight = true;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (movingRight[animationCounter]);
        //move player if they are not touching a wall on their right
        if (!hitWall) {
            //if player is on a moving floor going in the opposite direction then let the player move more
            if (floorSpeed < 0)
                setLocation(getX() - floorSpeed + speed, getY());
            else
                setLocation(getX() + speed, getY());
        }
    }

    /**
     * Changes actor's image to a picture of the player standing still.
     */
    public void stopAnimation() {
        // Stop moving
        moving = false;
        // Reset animation counter
        animationCounter = 0;
        // Reset animation delay caounter
        animationDelayCounter = 0;
        // Set appropriate image based on which way player was most recently
        // moving
        if (facingRight)
            this.setImage (standingRight);
        else
            this.setImage (standingLeft);
    }

    /**
     * Returns whether the player is facing towards the right or not.
     * 
     * @return boolean Return true if facing right, false if facing left.
     */
    public boolean facingRight() {
        if (facingRight)
            return true;
        else
            return false;
    }

    /**
     * Makes the player jump, and optionally plays a sound.
     * 
     * @param playSound True if sound is to be played, false if not.
     */
    public void jump(boolean playSound) {
        vSpeed = -9; //make vertical speed negative so player can go up
        setLocation(getX(), Math.round(getY() + vSpeed)); //set new location
        if (playSound) {
            jump1.stop(); //stop playing sound if it was still playing from before
            jump1.play(); //play jump sound if parameter allows it
        }
    }

    /**
     * Returns whether or not the player is in the air.
     * 
     * @return boolean Return true if they are airborne, false if not.
     */
    public boolean getAirborne() {
        if (airborne)
            return true;
        else
            return false;
    }

    /**
     * Returns whether or not the player has double jumped.
     * 
     * @return boolean Return true if player has double jumped, false if not.
     */
    public boolean getDoubleJump() {
        return doubleJump;
    }

    /**
     * Makes the player jump again while they are in the air and plays a sound.
     */
    public void doubleJump() {
        jump(false); //jump but don't play jump sound
        jump2.stop(); //stop playing sound if it was still playing from before
        jump2.play(); //play double jump sound
        doubleJump = true; //set doubleJump to true
    }

    /**
     * Makes the player fall if they are in the air.
     */
    private void fall() {
        if (airborne) {
            vSpeed += GRAVITY; //increase vertical speed
            setLocation(getX(), Math.round(getY() + vSpeed)); //set the new location
        }
    }

    /**
     * Changes appropriate variables when player is standing on a block.
     */
    public void onGround() {
        vSpeed = 0; //set vertical speed to 0
        airborne = false; //set airborne to false
        doubleJump = false; //reset double jump
    }

    /**
     * Plays the landing sound if it hasn't been played already.
     */
    private void playLandingSound() {
        if (!playedLandingSound) {
            land.stop(); //stop playing if it is still playing
            land.play(); //play the sound
            playedLandingSound = true;
        }
    }

    /**
     * Controls death animation and allows player to respawn when animation is finished.
     */
    private void dead() {
        restrictMovement = true;
        //play scream if it hasn't been played
        if (!playedScream) {
            scream.play();
            playedScream = true;
        }
        //rotate the player if it hasn't been rotated already
        if (!rotated) {
            if (facingRight)
                turn(-90);
            else
                turn(90);
            rotated = true;
        }
        //decrease transparency until it becomes 0, then set respawn to true
        transparency -= 2;
        if (transparency < 0) {
            transparency = 0;
            respawn = true;
        }
        getImage().setTransparency(transparency); //change image transparency
    }

    /**
     * Accessor for restrictMovement boolean.
     * 
     * @return boolean Return true if movement is to be restricted.
     */
    public boolean restrictedMovement() {
        return restrictMovement;
    }

    /**
     * Accessor for respawn boolean.
     * 
     * @return boolean Return true if player can be respawned.
     */
    public boolean getRespawn() {
        return respawn;
    }

    /**
     * Creates a projectile to the left or to the right of the player.
     */
    public void shoot() {
        //only shoot if there has been 15 frames since the last shot
        if (!shot) {
            if (facingRight) { //spawn projectile to the right of the player
                getWorld().addObject(new PlayerShot(true), getX()+7, getY());
            }
            else { //spawn projectile to the left of the player
                getWorld().addObject(new PlayerShot(false), getX()-7, getY());
            }
            shot = true;
        }
    }

    /**
     * Accessor for boolean isSaved.
     * 
     * @return boolean Return true if player has reached a save point.
     */
    public boolean isSaved() {
        return isSaved;
    }

    /**
     * Checks if the player is touching an object and prevents the player from walking into it.
     */
    private void checkWall() {
        if (facingRight) {
            //find objects to the right of the player sprite
            g = (GrassBlock)getOneObjectAtOffset(7, 0, GrassBlock.class);
            b = (Block)getOneObjectAtOffset(7, 0, Block.class);
            br = (Brick)getOneObjectAtOffset(7, 0, Brick.class);
            cb = (CannonBullet)getOneObjectAtOffset(7, 0, CannonBullet.class);
            c = (Cannon)getOneObjectAtOffset(7, 0, Cannon.class);
            //if the player is touching a grass block
            if (g != null) {
                //if the top of the grass block is higher than the player's feet; so player doesn't get stuck mid air
                if (g.getY()-16 < getY()+10)
                    hitWall = true; //set hitWall to true
                //if player is touching a dirt block
            } else if (b != null) {
                //if the top of the dirt block is higher than the player's feet; so player doesn't get stuck mid air
                if (b.getY()-16 < getY()+10)
                    hitWall = true; //set hitWall to true
            } else if (br != null) {
                //if the top of the brick is higher than the player's feet; so player doesn't get stuck mid air
                if (br.getY()-16 < getY()+10)
                    hitWall = true;
            } else if (cb != null) {
                //if the top of the bullet is higher than the player's feet; so player doesn't get stuck mid air
                if (cb.getY()-11 < getY()+10)
                    hitWall = true;
            } else if (c != null) {
                //if the top of the cannon is higher than the player's feet; so player doesn't get stuck mid air
                if (c.getY()-16 < getY()+10)
                    hitWall = true;
            } else
                hitWall = false; //otherwise, set hitWall to false
        } else if (!facingRight) {
            //find objects to the left of the player sprite
            g = (GrassBlock)getOneObjectAtOffset(-8, 0, GrassBlock.class);
            b = (Block)getOneObjectAtOffset(-8, 0, Block.class);
            br = (Brick)getOneObjectAtOffset(-8, 0, Brick.class);
            cb = (CannonBullet)getOneObjectAtOffset(-8, 0, CannonBullet.class);
            //if the player is touching a grass block
            if (g != null) {
                //if the top of the grass block is higher than the player's feet so player doesn't get stuck mid air
                if (g.getY()-16 < getY()+10)
                    hitWall = true; //set hitWall to true
                //if player is touching a dirt block
            } else if (b != null) {
                //if the top of the dirt block is higher than the player's feet so player doesn't get stuck mid air
                if (b.getY()-16 < getY()+10)
                    hitWall = true; //set hitWall to true
            } else if (br != null) {
                //if the top of the brick is higher than the player's feet so player doesn't get stuck mid air
                if (br.getY()-16 < getY()+10)
                    hitWall = true;
            } else if (cb != null) {
                //if the top of the bullet is higher than the player's feet; so player doesn't get stuck mid air
                if (cb.getY()-11 < getY()+10)
                    hitWall = true;
            } else
                hitWall = false; //otherwise, set hitWall to false
        }
    }

    /**
     * Controls the player animation delays.
     */
    private void animationDelays() {
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
        if (animationCounter > movingRight.length -1)
            animationCounter = 0;
    }

    /**
     * Prevents the player from going through the bottom of a block when jumping.
     */
    private void checkCeiling() {
        if (airborne) {
            //find objects on top of the player sprite
            for (int i = -5; i < 7; i++) {
                g = (GrassBlock)getOneObjectAtOffset(i, -10, GrassBlock.class);
                b = (Block)getOneObjectAtOffset(i, -10, Block.class);
                br = (Brick)getOneObjectAtOffset(i, -10, Brick.class);
                cb = (CannonBullet)getOneObjectAtOffset(i, -10, CannonBullet.class);
                //if the player is touching a grass block
                if (g != null) {
                    //if the bottom of the grass block is lower than the top of the player's head and the player was jumping up
                    if (g.getY()+16 > getY()-10 && vSpeed < 0) {
                        vSpeed = 0;
                        setLocation(getX(), g.getY()+26);
                        break;
                    }
                    //if the player is touching a dirt block
                } else if (b != null) {
                    //if the bottom of the dirt block is lower than the top of the player's head and the player was jumping up
                    if (b.getY()+16 > getY()-10 && vSpeed < 0) {
                        vSpeed = 0;
                        setLocation(getX(), b.getY()+26);
                        break;
                    }
                    //if the player is touching a brick
                } else if (br != null) {
                    //if the bottom of the brick is lower than the top of the player's head and the player was jumping up
                    if (br.getY()+16 > getY()-10 && vSpeed < 0) {
                        vSpeed = 0;
                        setLocation(getX(), br.getY()+26);
                        break;
                    }
                    //if the player is touching a cannon bullet
                } else if (cb != null) {
                    //if the bottom of the cannon bullet is lower than the top of the player's head and the player was jumping up
                    if (cb.getY()+11 > getY()-10 && vSpeed < 0) {
                        vSpeed = 0;
                        setLocation(getX(), cb.getY()+21);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Prevents the player from sinking into the floor when landing and allows player to stand on objects.
     */
    private void checkFloor() {
        //player's feet are 6 pixels wide, so use these as conditions for the for loop
        for (int i = -3; i < 4; i++) {
            //see if the current x coordinate of the player as dictated by the for loop is touching a block
            g = (GrassBlock)getOneObjectAtOffset(i, 10, GrassBlock.class);
            b = (Block)getOneObjectAtOffset(i, 10, Block.class);
            br = (Brick)getOneObjectAtOffset(i, 10, Brick.class);
            cb = (CannonBullet)getOneObjectAtOffset(i, 10, CannonBullet.class);
            c = (Cannon)getOneObjectAtOffset(i, 10, Cannon.class);
            //if the player is touching a grass block
            if (g != null) {
                onGround();
                playLandingSound(); //play the landing sound once
                //if the top of the grass block is higher than the player's feet
                if (g.getY()-16 < getY()+10 && !airborne) {
                    setLocation(getX(), g.getY()-26); //move player up so feet are on top of block
                }
                //if block is moving then take the block's speed and use that as floor speed
                if (g.isMove())
                    floorSpeed = g.getSpeed() * -g.getDirection();
                break;
            } else if (b != null) {
                onGround();
                playLandingSound(); //play the landing sound once
                //if the top of the dirt block is higher than the player's feet
                if (b.getY()-16 < getY()+10 && !airborne) {
                    setLocation(getX(), b.getY()-26); //move player up so feet are on top of block
                }
                break;
            } else if (br != null) {
                onGround();
                playLandingSound(); //play the landing sound once
                //if the top of the brick is higher than the player's feet
                if (br.getY()-16 < getY()+10 && !airborne) {
                    setLocation(getX(), br.getY()-26); //move player up so feet are on top of block
                }
                break;
            } else if (cb != null) {
                onGround();
                playLandingSound(); //play the landing sound once
                //if the top of the cannon bullet is higher than the player's feet
                if (cb.getY()-11 < getY()+10 && !airborne) {
                    setLocation(getX(), cb.getY()-21); //move player up so feet are on top of bullet
                }
                //if bullet is moving then take the bullet's speed and use that as floor speed
                if (cb.isMove())
                    floorSpeed = cb.getSpeed() * -cb.getDirection();
                break;
            } else if (c != null) {
                onGround();
                playLandingSound(); //play the landing sound once
                //if the top of the cannon is higher than the player's feet
                if (c.getY()-16 < getY()+10 && !airborne) {
                    setLocation(getX(), c.getY()-26); //move player up so feet are on top of cannon
                }
            }
            //if no floor has been detected
            if (i == 3) {
                //if player isn't touching block, set airborne to true, allow landing sound to be played again and reset floor speed
                airborne = true;
                playedLandingSound = false;
                floorSpeed = 0;
            }
        }
    }

    /**
     * Prevents the player from being partially inside a block when falling and landing.
     */
    private void fixInsideBlock() {
        //check for a block to the left of the player
        g = (GrassBlock)getOneObjectAtOffset(-7, 0, GrassBlock.class);
        b = (Block)getOneObjectAtOffset(-7, 0, Block.class);
        br = (Brick)getOneObjectAtOffset(-7, 0, Brick.class);
        c = (Cannon)getOneObjectAtOffset(-7, 0, Cannon.class);
        cb = (CannonBullet)getOneObjectAtOffset(-7, 0, CannonBullet.class);
        //if player is touching a grass block
        if (g != null) {
            //if the left of the player is inside the grass block which is to the left of the player
            if (getX()-7 < g.getX()+16) {
                setLocation(g.getX()+23, getY()); //set the player's location to right of the grass block
                hitWall = true; //set hitWall to true
            } 
            //if player is touching a dirt block
        } else if (b != null) {
            //if the left of the player is inside the block which is to the left of the player
            if (getX()-7 < b.getX()+16) {
                setLocation(b.getX()+23, getY()); //set the player's location to right of the dirt block
                hitWall = true; //set hitWall to true
            }
        } else if (br != null) {
            //if the left of the player is inside the brick which is to the left of the player
            if (getX()-7 < br.getX()+16) {
                setLocation(br.getX()+23, getY()); //set the player's location to the right of the brick
                hitWall = true; //set hitWall to true
            }
        } else if (c != null) {
            //if the left of the player is inside the cannon which is to the left of the player
            if (getX()-7 < c.getX()+16) {
                setLocation(c.getX()+23, getY()); //set the player's location to the right of the cannon
                hitWall = true; //set hitWall to true
            }
        } else if (cb != null) {
            //if the left of the player is inside the bullet which is to the left of the player
            if (getX()-7 < cb.getX()+18) {
                setLocation(cb.getX()+25, getY()); //set the player's location to the right of the bullet
                hitWall = true; //set hitWall to true
            }
        } else {
            //if there wasn't a block to the left, check for a block to the right of the player
            g = (GrassBlock)getOneObjectAtOffset(7, 0, GrassBlock.class);
            b = (Block)getOneObjectAtOffset(7, 0, Block.class);
            br = (Brick)getOneObjectAtOffset(7, 0, Brick.class);
            c = (Cannon)getOneObjectAtOffset(7, 0, Cannon.class);
            cb = (CannonBullet)getOneObjectAtOffset(7, 0, CannonBullet.class);
            //if player is touching a grass block
            if (g != null) {
                //if the right of the player is inside the grass block which is to the right of the player
                if (getX()+7 > g.getX()-16) {
                    setLocation(g.getX()-23, getY()); //set the player's location to left of the grass block
                    hitWall = true; //set hitWall to true
                }
                //if player is touching a dirt block
            } else if (b != null) {
                //if the right of the player is inside the dirt block which is to the right of the player
                if (getX()+7 > b.getX()-16) {
                    setLocation(b.getX()-23, getY()); //set the player's location to the left of the dirt block
                    hitWall = true; //set hitWall to true
                }
            } else if (br != null) {
                //if the right of the player is inside the brick which is to the right of the player
                if (getX()+7 > br.getX()-16) {
                    setLocation(br.getX()-23, getY()); //set the player's location to the left of the brick
                    hitWall = true; //set hitWall to true
                }
            } else if (c != null) {
                //if the right of the player is inside the cannon which is to the right of the player
                if (getX()+7 > c.getX()-16) {
                    setLocation(c.getX()-23, getY()); //set the player's location to the left of the block
                    hitWall = true; //set hitWall to true
                }
            } else if (cb != null) {
                //if the right of the player is inside the bullet which is to the right of the player
                if (getX()+7 > cb.getX()-18) {
                    setLocation(cb.getX()-25, getY()); //set the player's location to the right of the block
                    hitWall = true; //set hitWall to true
                }
            }
        }
    }

    /**
     * Mutator for boolean alive.
     * 
     * @param inAlive True if player is alive, false if not.
     */
    public void setAlive(boolean inAlive) {
        alive = inAlive;
    }
}