import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
/**
 * THWidget is a health bar that displays the current amount of health.
 * The bar increases and decreases in size depending on whether HP is increasing or decreasing.
 * There is also text on the bar that shows the amount of health.
 * 
 * @author Terrence Hung 
 * @version February 2014
 */
public class THWidget extends Actor
{
    //declare class variables
    private GreenfootImage myImage;
    private int backgroundLength;
    private int backgroundHeight;
    private Color backgroundColor;
    private int currentHP;
    private int maxHP;
    private int fillLength;
    private int fillHeight;
    private Color fillColorOriginal;
    private Color fillColor;
    private int borderWidth;

    /**
     * Constructs an HP bar 250px by 50px that starts at 100 HP, with a 5px wide black border and green fill.
     */
    public THWidget()
    {
        //ASSIGNING ALUES TO VARIABLES
        backgroundLength = 250; //set background length to 250
        backgroundHeight = 50; //set background height to 50
        backgroundColor = Color.BLACK; //set background color to black
        fillLength = backgroundLength - 5 * 2; //set fill length to background length minus border width twice
        fillHeight = backgroundHeight - 5 * 2; //set fill height to background height minus border width twice
        fillColorOriginal = Color.GREEN; //set fill color original to green
        fillColor = Color.GREEN; //set fill color to green
        borderWidth = 5; //set border width to 5
        maxHP = 100; //set max HP to 100
        currentHP = 100; //set current HP to full

        //AFTER ASSIGNING VALUES TO VARIABLES
        myImage = new GreenfootImage(backgroundLength, backgroundHeight); //construct an image with dimensions from parameters
        setImage(myImage); //set the image to the actor
        myImage.setColor(Color.BLACK); //set color to black
        myImage.fillRect(0, 0, backgroundLength, backgroundHeight); //fill a rectangle with dimensions from parameters, will be used as outline
        myImage.setColor(Color.GREEN); //change color to green
        myImage.fillRect(borderWidth, borderWidth, fillLength, fillHeight); //fill a rectangle that will be inside other rectangle, leaving a border

        myImage.setColor(Color.WHITE); //change color to white
        myImage.setFont(new Font("Courier", Font.BOLD, fillHeight / 2)); //set a new font
        myImage.drawString(currentHP + "/" + maxHP, fillLength / 3, fillHeight); //put the text displaying hp on screen
    }

    /**
     * Constructs an HP bar with a 5:1 length to height ratio that starts at full HP by drawing two rectangles with the bigger one acting as the border and background.
     * 
     * @param bgLength Length of the entire bar in pixels
     * @param hpMax Maximum amount of HP that the bar can have
     * @param bgColor Color of the back rectangle
     * @param fColor Color of the bar that will be inside the border
     */
    public THWidget(int bgLength, int hpMax, Color bgColor, Color fColor)
    {
        //ASSIGNING VALUES TO VARIABLES
        backgroundLength = bgLength; //set background length to value from parameter  
        backgroundHeight = bgLength / 5; //set background height to backgroundLength divided by 5
        backgroundColor = bgColor; //set background color to color from parameter
        borderWidth = backgroundLength / 50; //set border width to backgroundLength divided by 50
        fillLength = backgroundLength - borderWidth * 2; //set fill length to the background length minus border width twice
        fillHeight = backgroundHeight - borderWidth * 2; //set fill height to background height minus border width twice
        fillColorOriginal = fColor; //set fill color original to color from parameter
        fillColor = fColor; //set fill color to color from parameter        
        maxHP = hpMax; //set max hp to value from parameter
        currentHP = hpMax; //set current hp to full

        //AFTER ASSIGNING VALUES TO VARIABLES
        myImage = new GreenfootImage(backgroundLength, backgroundHeight); //construct an image with dimensions from parameters
        setImage(myImage); //set the image to the actor
        myImage.setColor(backgroundColor); //set color to the color from the parameter
        myImage.fillRect(0, 0, backgroundLength, backgroundHeight); //fill a rectangle with dimensions from parameters, will be used as outline
        myImage.setColor(fillColor); //change color to color from parameter
        myImage.fillRect(borderWidth, borderWidth, fillLength, fillHeight); //fill a rectangle that will be inside other rectangle, leaving a border

        //         myImage.setColor(Color.WHITE); //change color to white
        //         myImage.setFont(new Font("Courier", Font.BOLD, fillHeight / 2)); //set a new font
        //         myImage.drawString(currentHP + "/" + maxHP, fillLength / 3, fillHeight); //put the text displaying hp on screen
    }

    /**
     * Constructs an HP bar with a 5:1 length to height ratio that starts at a given HP by drawing two rectangles with the bigger one acting as the border and background.
     * 
     * @param bgLength Length of the entire bar in pixels
     * @param hpMax Maximum amount of HP that the bar can have
     * @param hpCurrent Current amount of HP the bar has
     * @param bgColor Color of the back rectangle
     * @param fColor Color of the bar that will be inside the border
     */
    public THWidget(int bgLength, int hpMax, int hpCurrent, Color bgColor, Color fColor)
    {
        //ASSIGNING VALUES TO VARIABLES
        backgroundLength = bgLength; //set background length to value from parameter  
        backgroundHeight = bgLength / 5; //set background height to backgroundLength divided by 5
        backgroundColor = bgColor; //set background color to color from parameter
        borderWidth = backgroundLength / 50; //set border width to backgroundLength divided by 50
        fillLength = backgroundLength - borderWidth * 2; //set fill length to the background length minus border width twice
        fillHeight = backgroundHeight - borderWidth * 2; //set fill height to background height minus border width twice
        fillColorOriginal = fColor; //set fill color original to color from parameter
        fillColor = fColor; //set fill color to color from parameter        
        maxHP = hpMax; //set max hp to value from parameter
        currentHP = hpCurrent; //set current hp to value from parameter

        //AFTER ASSIGNING VALUES TO VARIABLES
        myImage = new GreenfootImage(backgroundLength, backgroundHeight); //construct an image with dimensions from parameters
        setImage(myImage); //set the image to the actor
        myImage.setColor(backgroundColor); //set color to the color from the parameter
        myImage.fillRect(0, 0, backgroundLength, backgroundHeight); //fill a rectangle with dimensions from parameters, will be used as outline
        myImage.setColor(fillColor); //change color to color from parameter
        myImage.fillRect(borderWidth, borderWidth, calculateBarLength(), fillHeight); //fill a rectangle that will be inside other rectangle, leaving a border

        myImage.setColor(Color.WHITE); //change color to white
        myImage.setFont(new Font("Courier", Font.BOLD, fillHeight / 2)); //set a new font
        myImage.drawString(currentHP + "/" + maxHP, fillLength / 3, fillHeight); //put the text displaying hp on screen
    }

    /**
     * Changes color of health bar to red if health is less than 15%, yellow if health is less than 40%, or original color if health is above 40%.
     */
    public void act()
    {
        //if health is less than 15% change color to red
        if((int)((double)currentHP/(double)maxHP * 100) <= 15)
            fillColor = Color.RED;
        //if 15% < health < 40% change color to yellow
        else if((int)((double)currentHP/(double)maxHP * 100) <= 40)
            fillColor = Color.YELLOW;
        //if health is greater than 40% change color back to original color
        else
            fillColor = fillColorOriginal;
    }

    /**
     * Increases or decreases HP and updates the bar accordingly.
     * 
     * @param change How much the health changes by
     */
    public void update(int change)
    {
        myImage.clear(); //clear the image
        myImage.setColor(backgroundColor); //set color to this instance's background color
        myImage.fillRect(0, 0, backgroundLength, backgroundHeight); //redraw rectangle that makes the outline
        myImage.setColor(fillColor); //set color to this instance's fill color
        currentHP += change; //set the new length of the bar
        myImage.fillRect(borderWidth, borderWidth, calculateBarLength(), fillHeight); //draw inner rectangle

        //         myImage.setColor(Color.WHITE); //change color to white
        //         myImage.drawString(currentHP + "/" + maxHP, fillLength / 3, fillHeight); //put the text displaying hp on screen
    }

    /**
     * Changes the color of the HP bar.
     * 
     * @param fColor New color of HP bar
     */
    public void update(Color fColor)
    {
        fillColor = fColor; //set the color of the bar to the color from the parameter
        fillColorOriginal = fillColor; //set fillColorOriginal to fillColor
        update(0); //update bar
    }

    /**
     * Calculates the length of the filled part of the bar.
     * 
     * @return int Length of the filled bar in pixels that will be drawn
     */
    private int calculateBarLength()
    {
        //if the percentage of health is less than or equal to 0
        if((int)((double)currentHP/(double)maxHP * fillLength) <= 0)
        {
            currentHP = 0; //set current hp to 0
            return 0;
        }
        //if the percentage of health is greater than or equal to max length of bar
        else if((int)((double)currentHP/(double)maxHP * fillLength) >= fillLength)
        {
            currentHP = maxHP; //set current hp to max hp
            return fillLength;
        }
        //if the percentage of health is between max and min
        else
            return (int)((double)currentHP/(double)maxHP * fillLength); //calculate how many pixels long the bar should be and return it
    }
}
