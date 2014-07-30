import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Enemies contain classes such as Ken, Ryu, DK and Boo
 * Enemies aim to kill the player class, and have a vastly different functionality
 * All enemies have hp, a certain speed and the ability to be killed by the player
 * 
 * @author Jaydon Lau
 * @version June 2014
 */
public abstract class Enemy extends Actor
{
    protected int hp;
    protected int mySpeed;
    protected boolean alive;

    /**
     * Changes the HP value of the enemy
     * 
     * @param hpChanged The value of hp decreased 
     */
    public void changeHP (int hpChanged) {
        hp = hp - hpChanged;
    }

}
