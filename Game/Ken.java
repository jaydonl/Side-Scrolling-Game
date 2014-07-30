import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
/**
 * Ken is the final boss of the game, and is the strongest enemy by far.
 * Ken is able to punch, kick and fire hadouken, all of which will kill the player
 * 
 * @author Jaydon Lau
 * @version June 2014
 */
public class Ken extends Enemy
{
    //declare variables
    private GreenfootImage[] movingLeft = new GreenfootImage[4];
    private GreenfootImage[] movingRight = new GreenfootImage[4];
    private GreenfootImage[] hadoukenLeft = new GreenfootImage[4];
    private GreenfootImage[] hadoukenRight = new GreenfootImage[4];
    private GreenfootImage[] punchingLeft = new GreenfootImage[2];
    private GreenfootImage[] punchingRight = new GreenfootImage[2];
    private GreenfootImage[] kickingLeft = new GreenfootImage[3];
    private GreenfootImage[] kickingRight = new GreenfootImage[3];
    private GreenfootImage standingLeft = new GreenfootImage("KenStandingLeft1.png");
    private GreenfootImage standingRight = new GreenfootImage("KenStandingRight1.png");

    // Two booleans for controlling the flow of the action
    private boolean moving = false;
    private boolean facingRight = true;
    // Integers for controlling the speed of the movement and animation
    private int animationCounter = 0;
    private int animationDelay = 25;
    private int animationDelayCounter = 0;

    // sequence of moves that ken will perform: 1 for punch, 2 for kick, 3 for hadouken
    private int [] sequence = {1, 1, 2, 3};
    // the element of the sequence currently used
    private int sequenceCounter = 0; 

    // counters for each frame of action
    private int maxAnimationCounter = 4;
    private int hadoukenCounter;
    private int punchCounter;
    private int kickCounter;

    // booleans to activate each move
    private boolean hadouken = false;
    private boolean punch = false;
    private boolean kick = false;
    private boolean action = false;

    private boolean nearEdge = false;
    private boolean alive = true;

    // variables to control death animation
    private boolean playedScream = false;
    private boolean rotated = false;
    private GreenfootSound scream = new GreenfootSound("scream.wav");
    private int transparency = 255;

    private Player p1;
    private int distanceX;

    private GreenfootSound hadoukenSound = new GreenfootSound("kenHadouken.mp3");
    private THWidget healthBar = new THWidget(40, 50, Color.BLACK, Color.GREEN);
    private boolean healthBarSpawn = false;
    /**
     * Creates a Ken class
     */
    public Ken() 
    {
        mySpeed = 7;
        hp = 50;
        p1 = PlatformWorld.getPlayer();
        alive = true;
        // Three Strings together to build file name
        String fileNameStanding;
        String fileNamePrefixStanding = "KenStanding";
        String fileNameSuffixStanding = ".png";
        //populate arrays with images
        for (int i = 0; i < 4; i++) {
            //build file name for facing left image
            fileNameStanding = fileNamePrefixStanding + "Left" + (i+1) + fileNameSuffixStanding;
            //put file into appropriate array
            movingLeft[i] = new GreenfootImage(fileNameStanding);
            //build file name for facing right image
            fileNameStanding = fileNamePrefixStanding + "Right" + (i+1) + fileNameSuffixStanding;
            //put file into appropriate array
            movingRight[i] = new GreenfootImage(fileNameStanding);
        }
        // Three Strings together to build file name
        // Three Strings together to build file name
        String fileNameHadouken;
        String fileNamePrefixHadouken = "KenHadouken";
        String fileNameSuffixHadouken = ".png";
        //populate arrays with images
        for (int i = 0; i < 4; i++) {
            //build file name for facing left image
            fileNameHadouken = fileNamePrefixHadouken + "Left" + (i+1) + fileNameSuffixHadouken;
            //put file into appropriate array
            hadoukenLeft[i] = new GreenfootImage(fileNameHadouken);
            //build file name for facing right image
            fileNameHadouken = fileNamePrefixHadouken + "Right" + (i+1) + fileNameSuffixHadouken;
            //put file into appropriate array
            hadoukenRight[i] = new GreenfootImage(fileNameHadouken);
        }
        // Three Strings together to build file name
        // Three Strings together to build file name
        String fileNamePunching;
        String fileNamePrefixPunching = "KenPunching";
        String fileNameSuffixPunching = ".png";
        //populate arrays with images
        for (int i = 0; i < 2; i++) {
            //build file name for facing left image
            fileNamePunching = fileNamePrefixPunching + "Left" + (i+1) + fileNameSuffixPunching;
            //put file into appropriate array
            punchingLeft[i] = new GreenfootImage(fileNamePunching);
            //build file name for facing right image
            fileNamePunching = fileNamePrefixPunching + "Right" + (i+1) + fileNameSuffixPunching;
            //put file into appropriate array
            punchingRight[i] = new GreenfootImage(fileNamePunching);
        }
        // Three Strings together to build file name
        // Three Strings together to build file name
        String fileNameKicking;
        String fileNamePrefixKicking = "KenKicking";
        String fileNameSuffixKicking = ".png";
        //populate arrays with images
        for (int i = 0; i < 3; i++) {
            //build file name for facing left image
            fileNameKicking = fileNamePrefixKicking + "Left" + (i+1) + fileNameSuffixKicking;
            //put file into appropriate array
            kickingLeft[i] = new GreenfootImage(fileNameKicking);
            //build file name for facing right image
            fileNameKicking = fileNamePrefixKicking + "Right" + (i+1) + fileNameSuffixKicking;
            //put file into appropriate array
            kickingRight[i] = new GreenfootImage(fileNameKicking);
        }
    }

    /**
     * Act - do whatever the Guy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!healthBarSpawn)
            getWorld().addObject(healthBar, getX(), getY()-50);
        // if hp reaches zero, set alive to false
        if (hp <= 0) {
            alive = false;
        }
        if (alive) {
            if (p1.getRespawn()) {
                p1 = PlatformWorld.getPlayer();
            }
            // gets distance from ken to player
            distanceX = this.getX() - p1.getX();
            // if distance is positive, ken is facing left
            if (distanceX > 0) {
                facingRight = false;
            }
            // if distance is negative, ken is facing right
            else {
                facingRight = true;
            }
            // if ken is near the edge, turn directions
            if (nearEdge) {
                if (facingRight) {
                    facingRight = false;
                }
                else {
                    facingRight = true;
                }
            }

            // if action delay is not true, and the distance between ken is below 25 then start an action
            if ((distanceX < 25 && distanceX > 0) || (distanceX > -25 && distanceX < 0)) {
                action = true;
            }

            if (action) {
                if (sequenceCounter > 3) {
                    sequenceCounter = 0;
                }
                if (sequence[sequenceCounter] == 1) {
                    punch = true;
                }
                else if (sequence[sequenceCounter] == 2) {
                    kick = true;
                }
                else if (sequence[sequenceCounter] == 3) {
                    hadouken = true;
                }
                //Set the maximum animination counter to the total number of frames depending on each animation
                if (hadouken) {
                    maxAnimationCounter = 4;
                }
                else if (punch) {
                    maxAnimationCounter = 2;
                }
                else if (kick) {
                    maxAnimationCounter = 3;
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
                if (animationCounter > maxAnimationCounter - 1)
                    animationCounter = 0;

                if (hadouken && facingRight) {
                    hadoukenRight();
                }
                else if (hadouken && !facingRight) {
                    hadoukenLeft();
                }
                else if (punch && facingRight) {
                    punchRight();
                    // hit detection for the punch
                    if (animationCounter == 1) {
                        Player player = (Player) getOneObjectAtOffset (25, -8, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                }
                else if (punch && !facingRight) {
                    punchLeft();
                    // hit detection for the punch
                    if (animationCounter == 1) {
                        Player player = (Player) getOneObjectAtOffset (-25, -8, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                }
                else if (kick && facingRight) {
                    kickRight();
                    // hit detection for the kick
                    if (animationCounter == 2) {
                        Player player = (Player) getOneObjectAtOffset (30, -30, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                    
                    // hit detection for the kick
                    else if (animationCounter == 1) {
                        Player player = (Player) getOneObjectAtOffset (28, 30, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                }
                else if (kick && !facingRight) {
                    kickLeft();
                    // hit detection for the kick
                    if (animationCounter == 2) {
                        Player player = (Player) getOneObjectAtOffset (-30, -30, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                    // hit detection for the kick
                    else if (animationCounter == 1) {
                        Player player = (Player) getOneObjectAtOffset (-28, 30, Player.class);
                        if (player != null) {
                            player.setAlive (false);
                        }
                    }
                }
            }
            // otherwise move around
            else {
                if (facingRight && distanceX < - 10) {
                    moveRight();
                }
                else if (!facingRight && distanceX > 10) {
                    moveLeft();
                }
                else {
                    stopMoving();
                }
            }

            healthBar.setLocation(getX(), getY()-50);
        }
        else {
            // death animation upon hp reaching 0 
            dead();
            if (transparency == 0) {
                getWorld().removeObject(this);
            }
        }

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
        // Move the actor
        setLocation (getX() - mySpeed, getY());  
    }

    /**
     * Moves the player to the right and controls right movement animation.
     */
    public void moveRight() {
        // Check if direction is changing, and if so, reset counter
        if (!(facingRight))
            animationCounter = 0;
        // Set control booleans to facing right and walking
        moving = true;
        facingRight = true;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (movingRight[animationCounter]);
        // Move the actor
        setLocation (getX() + mySpeed, getY());  
    }

    /**
     * Changes actor's image to a picture of the player standing still.
     */
    public void stopMoving() {
        // Stop walking
        moving = false;
        // Reset animation counter
        animationCounter = 0;
        // Reset animation delay caounter
        animationDelayCounter = 0;
        // Set appropriate image based on which way player was most recently
        // moving
        if (facingRight)
            setImage (standingRight);
        else
            setImage (standingLeft);
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
     * Makes Ken perform a hadouken right
     */
    public void hadoukenRight () {
        // Check if direction is changing, and if so, reset counter
        if (!facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = true;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (hadoukenRight[animationCounter]);
        if (animationCounter == 1 && animationDelayCounter == 10) {
            if (hadoukenSound.isPlaying()){
                hadoukenSound.stop();
            }
            hadoukenSound.play();
        }
        if (animationCounter == 2 && animationDelayCounter == 0 ) {
            getWorld().addObject(new Fireball(true), getX()+20 , getY()-10);
        }

        hadoukenCounter++;
        if (hadoukenCounter == 100) {
            hadouken = false;
            hadoukenCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Makes Ken perform a hadouken left
     */
    public void hadoukenLeft () {
        // Check if direction is changing, and if so, reset counter
        if (facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = false;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (hadoukenLeft[animationCounter]);
        if (animationCounter == 1 && animationDelayCounter == 10) {
            if (hadoukenSound.isPlaying()){
                hadoukenSound.stop();
            }
            hadoukenSound.play();
        }
        if (animationCounter == 2 && animationDelayCounter == 0 ) {
            getWorld().addObject(new Fireball(false), getX() - 20, getY() - 10);
        }

        hadoukenCounter++;
        if (hadoukenCounter == 100) {
            hadouken = false;
            hadoukenCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Makes Ken perform a punch right
     */
    public void punchRight(){
        // Check if direction is changing, and if so, reset counter
        if (!facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = true;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (punchingRight[animationCounter]);

        punchCounter++;
        if (punchCounter == 50) {
            punch = false;
            punchCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Makes Ken perform a punch left
     */
    public void punchLeft (){
        // Check if direction is changing, and if so, reset counter
        if (facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = false;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (punchingLeft[animationCounter]);

        punchCounter++;
        if (punchCounter == 50) {
            punch = false;
            punchCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Makes Ken perform a kick
     */
    public void kickRight() {
        // Check if direction is changing, and if so, reset counter
        if (!facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = true;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (kickingRight[animationCounter]);

        kickCounter++;
        if (kickCounter == 75) {
            kick = false;
            kickCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Makes Ken perform a kick
     */
    public void kickLeft() {
        // Check if direction is changing, and if so, reset counter
        if (facingRight)
            animationCounter = 0;
        // Set control booleans to not facing right and walking
        moving = false;
        facingRight = false;
        // Set the appropriate image. Note that animationCounter is
        // controlled by the act() method.
        setImage (kickingLeft[animationCounter]);

        kickCounter++;
        if (kickCounter == 75) {
            kick = false;
            kickCounter = 0;
            sequenceCounter++;
            action = false;
        }
    }

    /**
     * Controls death animation and allows player to respawn when animation is finished.
     * 
     * @author Terrence Hung
     */
    private void dead() {
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
            setLocation (getX(), getY() + 17);
            rotated = true;
        }
        //decrease transparency until it becomes 0, then set respawn to true
        transparency -= 2;
        if (transparency < 0) {
            transparency = 0;
        }
        getImage().setTransparency(transparency); //change image transparency
        alive = false;
    }

    /**
     * Checks to see if Ken is reaching the edge of the platform 
     */
    private void nearEdge () {
        if (facingRight) {
            GrassBlock gRight = (GrassBlock) getOneObjectAtOffset (48, 16, GrassBlock.class);
            if (gRight == null) {
                nearEdge = true;
            }
            else {
                nearEdge = false;
            }
        }
        else {
            GrassBlock gLeft = (GrassBlock) getOneObjectAtOffset (-48, 16, GrassBlock.class);
            if (gLeft == null) {
                nearEdge = true;
            }
            else {
                nearEdge = false;   
            }
        }
    }
    
    public boolean getAlive() {
        return alive;
    }
    
    public THWidget getHealthBar() {
        return healthBar;
    }
    
    public void removeHealthBar() {
        getWorld().removeObject(healthBar);
    }
}
