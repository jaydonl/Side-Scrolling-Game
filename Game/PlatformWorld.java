import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PlatformWorld extends World
{
    //How many tile are there going to be along x and y coordinates
    private int xTile = 32;
    private int yTile = 18;
    //current level of the map
    private int currentLevel = 1;
    //x and y coordinate of the saved location
    private int savedX, savedY;
    private int shootingDelay = 0;
    private Scanner scan;
    //2D array for the map
    private char[][]map = new char[yTile][xTile];
    private boolean movingSideways;
    //boolean that check if the platform is moving left or not
    private boolean movingLeft = true;
    private GrassBlock[] platform = new GrassBlock[10];
    private static Player player;
    private SaveButton saveButton;
    private ArrayList<Spike> movingSpikes = new ArrayList<Spike>();
    private Ken ken;
    private GreenfootSound bossMusic = new GreenfootSound("Guile's Theme.mp3");
    /**
     * Declares variables, adds the player and creates the map starting at level 1
     */
    public PlatformWorld() //LEVEL 1
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1024, 576, 1); 
        player = new Player(false);
        saveButton = new SaveButton();
        //Initialize the moving platform
        for (int i = 0; i < 5; i++)
        {
            platform[i] = new GrassBlock ();
        }
        //add player to the world
        addObject (player, 1024, 245);
        //Save the current level into a file 
        dataToWriteToFile();
        createMap();
    }

    /**
     * Declares variables, adds the player and creates the map
     * 
     * @param currentLevel The level that the game should start with
     */
    public PlatformWorld(int level) //Last level the player was on before exiting
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1024, 576, 1); 
        player = new Player(false);
        saveButton = new SaveButton();
        //Initialize the moving block
        for (int i = 0; i < 5; i++)
        {
            platform[i] = new GrassBlock ();
        }
        currentLevel = level;
        switch (level) {
            case 1:
            addObject(player, 1024, 245);
            case 2:
            addObject(player, 1024, 245);
            case 3:
            addObject(player, 1024, 85);
            case 4:
            addObject(player, 1024, 0);
            case 5:
            addObject(player, 1024, 245);
            case 6:
            addObject(player, 512, 0);
        }
        createMap();
    }

    public void act()
    {
        //check if player presses keys (up, down, left, right, shoot)
        checkKeys();
        //change the level if the player reaches a certain location 
        changeLevel();
        //respawn player if it died
        respawnSystem();
        //move the platform in level 1 and level 2
        moveBlock(platform);
        //feature for levels with moving spikes
        moveSpike();
        //level 5 feature
        fireCannon();
        checkGameOver();
    }

    /**
     * Create the map by reading the text file and import the content into an 2-D array
     * 
     * @author Ernest Wong
     */
    private void createMap()
    {
        try
        {
            //a counter that keep track of the y-tile of the map.
            int counter = 0; 
            //load the text file into a 2D array
            scan = new Scanner (new File ("map" + currentLevel + ".txt"));
            while (scan.hasNext())
            {
                String temp = scan.nextLine();
                for (int i = 0; i < xTile; i++)
                {
                    map[counter][i] = temp.charAt(i);
                }
                counter++;
            }
            //import the tiles here
            importTiles(); 

            //Add unique objects for every level
            if (currentLevel == 1)
            {
                //add a platform using an array of grass block
                for (int i = 0; i < 5; i++)
                {
                    platform[i].moving();
                    addObject (platform[i], 500+i*32, 225);
                }
                movingSpikes.clear();
                //add two flying spikes
                movingSpikes.add(new Spike(90, true, 1, 40));
                movingSpikes.add(new Spike(90, true, 1, 40));
                addObject(movingSpikes.get(0), 271, 177);
                addObject(movingSpikes.get(1), 271, 209);
                addObject (new Boo(true), 80, 150);
                addObject (new Boo(true), 120, 100);
                addObject (new Boo (true), 80, 450);
                addObject (new Boo (true), 120, 450);
            }
            else if (currentLevel == 2)
            {
                //add a platform using an array of grass block
                for (int i = 0; i < 3; i++)
                {
                    platform[i].moving();
                    addObject (platform[i], 300+i*32,460);
                }
                movingSpikes.clear();
                //add two flying spikes
                movingSpikes.add(new Spike(0, false, 1, 20));
                movingSpikes.add(new Spike(180, false, -1, 30));
                addObject (movingSpikes.get(0), 848,496);
                addObject (movingSpikes.get(1), 527,367);
                addObject (new Boo(true), 80, 150);
                addObject (new Boo(true), 120, 100);
                addObject (new Boo (true), 80, 450);
                addObject (new Boo (true), 120, 450);
            }
            else if (currentLevel == 3)
            {
                movingSpikes.clear();
                //add one flying spike
                movingSpikes.add(new Spike(180, false, -1, 30));
                //add a save button
                addObject (saveButton, 950,310);
                addObject (new Ryu (), 112, 403);
                //spawn at these coordinate if the player saved the game and died
                savedX = 950;
                savedY = 310;
            }
            else if (currentLevel == 4)
            {
                movingSpikes.clear();
                //add two flying spikes
                movingSpikes.add(new Spike(0, false, 1, 15));
                movingSpikes.add(new Spike(180, false, -1, 40));
                addObject (movingSpikes.get(0), 752,528);
                addObject (movingSpikes.get(1), 659,47);
                addObject (new DK(), 20, 115);
            }
            else if (currentLevel == 6) 
            {
                ken = new Ken();
                addObject (ken, 175, 345);
                bossMusic.setVolume(50);
                bossMusic.playLoop();
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
    }

    /**
     * Checks for user input, moves the character, and allows the animation to be played when the correct keys are pressed.
     * 
     * @author Terrence Hung
     */
    private void checkKeys()
    {
        //only move if player is allowed to
        if (!player.restrictedMovement())
        {
            // boolean to check if either direction was pressed
            movingSideways = false;
            //move right if right arrow key is pressed
            if (Greenfoot.isKeyDown("right")) 
            {
                player.moveRight();
                movingSideways = true;
            }
            //move left if left arrow key is pressed
            if (Greenfoot.isKeyDown("left")) 
            {
                player.moveLeft();
                movingSideways = true;
            }
            //jump if up is pressed
            if (Greenfoot.isKeyDown("up")) 
            {
                if (!player.getAirborne())
                    player.jump(true);
                //double jump is player is airborne and hasn't double jumped yet
                if ("up".equals(Greenfoot.getKey()) && player.getAirborne() && !player.getDoubleJump())
                {
                    player.doubleJump();
                }
            }
            if (Greenfoot.isKeyDown("z"))
                player.shoot();
            //if player isn't moving left or right
            if (!(movingSideways)) {
                player.stopAnimation();
            }
        }
    }

    /**
     * Import various tiles (blocks, grass blocks, spikes, brick) into the world by reading the 2-D array map[][]
     * 
     * b - block
     * g - grass block
     * o - brick
     * 1,2,3,4 - spikes with different rotation angles
     * c - canon
     * 
     * @author Ernest Wong
     */
    private void importTiles()
    {
        //fill the background with various tiles
        for (int i = 0; i < yTile; i++)
        {
            for (int j = 0; j < xTile; j++)
            {
                if (map[i][j] == 'b')
                {
                    addObject (new Block(), j*32+16, i*32+16);
                }
                else if (map[i][j] == 'g')
                {
                    addObject (new GrassBlock(), j*32+16, i*32+16);
                }
                else if (map[i][j] == '1')
                {
                    addObject (new Spike(0), j*32+16, i*32+16);
                }
                else if (map[i][j] == '2')
                {
                    addObject (new Spike(180), j*32+15, i*32+15);
                }
                else if (map[i][j] == '3')
                {
                    addObject (new Spike(90), j*32+15, i*32+16);
                }
                else if (map[i][j] == '4')
                {
                    addObject (new Spike(270), j*32+17, i*32+16);
                }
                else if (map[i][j] == 'o')
                {
                    addObject (new Brick(), j*32+16, i*32+16);
                }
                else if (map[i][j] == 'c')
                {
                    addObject (new Cannon(), j*32+16, i*32+16);
                }
            }
        }
    }

    /**
     * Removes all the blocks, enemies, and projectiles that are in the world.
     * 
     * @author Ernest Wong, Terrence Hung
     */
    private void removeAll()
    {
        List remove = getObjects(Background.class);
        List remove2 = getObjects(Enemy.class);
        List remove3 = getObjects(Projectile.class);
        for (Object objects : remove)
        {
            removeObject((Background)objects);
        }
        for (Object objects : remove2)
        {
            removeObject((Enemy)objects);
        }
        for (Object objects : remove3)
        {
            removeObject((Projectile)objects);
        }
    }

    /**
     * Removes all enemies or all projectiles in the world.
     * 
     * @param cls The class of objects that is to be removed.
     * @author Terrence Hung
     */
    private void removeAll(Class cls) {
        List remove;
        if (cls == Enemy.class) {
            remove = getObjects(Enemy.class);
            for (Object objects : remove) {
                removeObject((Enemy)objects);
            }
        } else if (cls == Projectile.class) {
            remove = getObjects(Projectile.class);
            for (Object objects : remove) {
                removeObject((Projectile)objects);
            }
        }
    }

    /**
     * Changes the current level, and spawns the player in the correct position.
     * 
     * @author Ernest Wong
     */
    private void changeLevel()
    {
        //clear level 1
        if (player.getX() == 0 && player.getY() < 192 && currentLevel == 1)
        {
            removeAll(); //remove all background
            currentLevel++; //level up
            createMap(); //create a new map
            player.setLocation (1024,245); //spawn the player at a certain location
        }
        //clear level 2
        else if (player.getX() == 0 && player.getY() < 350 && currentLevel == 2)
        {
            removeAll();
            currentLevel++;
            createMap();
            player.setLocation (1024,85);
        }
        //clear level 3
        else if (player.getX() == 0 && player.getY() < 550 && currentLevel == 3)
        {
            removeAll();
            saveButton.remove();
            currentLevel++;
            createMap();
            player.setLocation (1024,0);
        }
        //clear level 4
        else if (player.getX() == 0 && currentLevel == 4)
        {
            removeAll();
            currentLevel++;
            createMap();
            player.setLocation (1024,245);
        }
        //clear level 5
        else if (player.getY() >= 575 && currentLevel == 5)
        {
            removeAll();
            currentLevel++;
            createMap();
            player.setLocation (512,0);
        }
        //Save the current level into a file after the level has been changed
        dataToWriteToFile();
    }

    /**
     * Move the block and create a moving platform
     * 
     * @param platform  an array of grass block that made the platform
     * @author Ernest Wong
     */
    private void moveBlock(Background[] platform)
    {     
        if (currentLevel == 1)
        {
            if (movingLeft) //move left
            {
                //move the platform
                for (int i = 0; i < 5; i++)
                {
                    platform[i].move(1,2);
                }
            }
            else //move right
            {
                for (int i = 0; i < 5; i++)
                {
                    platform[i].move(-1,2);
                }
            }

            if (platform[0].getX() <= 310)
            {
                movingLeft = false;
            }
            else if (platform[4].getX() >= 1000)
            {
                movingLeft = true;
            }
        }
        else if (currentLevel == 2)
        {
            if (movingLeft)
            {
                for (int i = 0; i < 3; i++)
                {
                    platform[i].move(1,2);
                }
            }
            else
            {
                for (int i = 0; i < 3; i++)
                {
                    platform[i].move(-1,2);
                }
            }

            if (platform[0].getX() <= 240)
            {
                movingLeft = false;
            }
            else if (platform[2].getX() >= 500)
            {
                movingLeft = true;
            }
        }
    }

    /**
     * Respawn the player if it died, also resets the moving spikes.
     * 
     * @author Ernest Wong, Terrence Hung
     */
    private void respawnSystem()
    {
        if (player.getRespawn() && currentLevel == 1) {
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 1024, 245);
            //add the moving spikes back to where they originally were and set their variables to the appropriate values
            addObject(movingSpikes.get(0), 271, 177);
            movingSpikes.get(0).setInWorld(true);
            movingSpikes.get(0).setMoving(false);
            addObject(movingSpikes.get(1), 271, 209);
            movingSpikes.get(1).setInWorld(true);
            movingSpikes.get(1).setMoving(false);
            //remove boos from level if they are still there and respawn them
            removeAll(Enemy.class);
            addObject (new Boo(true), 80, 150);
            addObject (new Boo(true), 120, 100);
            addObject (new Boo (true), 80, 450);
            addObject (new Boo (true), 120, 450);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
        } else if (player.getRespawn() && currentLevel == 2) {
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 1024, 245);
            //add the moving spikes back to where they originally were and set their variables to the appropriate values
            addObject (movingSpikes.get(0), 848,496);
            movingSpikes.get(0).setInWorld(true);
            movingSpikes.get(0).setMoving(false);
            addObject (movingSpikes.get(1), 527,367);
            movingSpikes.get(1).setInWorld(true);
            movingSpikes.get(1).setMoving(false);
            //remove boos from level if they are still there and respawn them
            removeAll(Enemy.class);
            addObject (new Boo(true), 80, 150);
            addObject (new Boo(true), 120, 100);
            addObject (new Boo (true), 80, 450);
            addObject (new Boo (true), 120, 450);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
        } else if (player.getRespawn() && currentLevel == 3 && player.isSaved()) { //if the player save the lcoation       
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(true);
            movingSpikes.get(0).setInWorld(true);
            movingSpikes.get(0).setMoving(false);
            addObject(player, savedX, savedY); //respawn to the saved location
            //remove ryu if he is still in the world and respawn him
            removeAll(Enemy.class);
            addObject (new Ryu (), 112, 403);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
        } else if (player.getRespawn() && currentLevel == 3 && !player.isSaved()) {
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 1024, 85);
            movingSpikes.get(0).setInWorld(true);
            movingSpikes.get(0).setMoving(false);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
        } else if(player.getRespawn() && currentLevel == 4){
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 1024, 0);
            //add the moving spikes back to where they originally were and set their variables to the appropriate values
            addObject (movingSpikes.get(0), 752,528);
            movingSpikes.get(0).setInWorld(true);
            movingSpikes.get(0).setMoving(false);
            addObject (movingSpikes.get(1), 659,47);
            movingSpikes.get(1).setInWorld(true);
            movingSpikes.get(1).setMoving(false);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
            //remove enemies if they are still in level and respawn them
            removeAll(Enemy.class);
            addObject (new DK(), 20, 115);
        } else if(player.getRespawn() && currentLevel == 5) {
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 1024, 245);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
        } else if(player.getRespawn() && currentLevel == 6) {
            //remove the player, then respawn it by creating a new instance to reset all variables
            removeObject(player);
            player = new Player(false);
            addObject(player, 512, 0);
            //remove projectiles if they are still in level
            removeAll(Projectile.class);
            //reset ken
            ken.removeHealthBar();
            removeObject(ken);
            ken = new Ken();
            addObject (ken, 175, 345);
        } 
    }

    /**
     * Accessor for the player.
     * 
     * @return Player The player that the user controls.
     */
    public static Player getPlayer () {
        return player;
    } 

    /**
     * Move the spike when the players walks near a certain spike
     * 
     * @author Ernest Wong, Terrence Hung
     */
    private void moveSpike()
    {
        if (currentLevel == 1) {
            if (player.getX() < 700 && movingSpikes.get(0).getInWorld()) {
                movingSpikes.get(0).setMoving(true);
                movingSpikes.get(1).setMoving(true);
            }   
        } else if (currentLevel == 2) {
            if (player.getX() < 890 && movingSpikes.get(0).getInWorld())
                movingSpikes.get(0).setMoving(true);
            if (player.getX() < 550 && movingSpikes.get(1).getInWorld())
                movingSpikes.get(1).setMoving(true);
        } else if (currentLevel == 3) {
            if (player.getX() > 900 && player.getY() > 250 && !player.isSaved()) {
                addObject(movingSpikes.get(0), 930, 2);
                movingSpikes.get(0).setMoving(true);
            }
        } else if (currentLevel == 4) {
            if (player.getX() < 765 && movingSpikes.get(0).getInWorld())
                movingSpikes.get(0).setMoving(true);
            if (player.getX() < 670 && movingSpikes.get(1).getInWorld())
                movingSpikes.get(1).setMoving(true);
        }
    }

    /**
     * Fire cannon bullet at level 5
     * 
     * @author Ernest Wong
     */
    private void fireCannon()
    {
        if (currentLevel == 5 && shootingDelay == 0)
        {
            addObject (new CannonBullet(),145,267);
        }
        shootingDelay++;
        if (shootingDelay == 55)
        {
            shootingDelay = 0;
        }
    }

    /**
     * Sets the level the player was on before exiting the game to be written to a file
     * 
     * @author Jason Fok
     * @version June 2014
     */
    public void dataToWriteToFile () {
        try {
            writeToFile(Integer.toString (currentLevel) );
        }
        catch (IOException e) {
        }
    }

    /**
     * Writes the level the player was on before exiting the game to a file
     * 
     * @author Jason Fok
     * @version June 2014
     */
    public void writeToFile( String textLine ) throws IOException {
        FileWriter write = new FileWriter( "savedLevel.txt" ,false);
        PrintWriter print_line = new PrintWriter( write );
        print_line.printf( "%s" + "%n" , textLine);
        print_line.close();
    }

    /**
     * Checks to see if the game is over
     * @author Jason Fok
     * @version June 2014
     */
    public void checkGameOver() {
        if (currentLevel == 6) {
            if (ken.getAlive() == false) {
                Greenfoot.setWorld(new EndingScreen());
                bossMusic.stop();
            }
        }
    }
}
