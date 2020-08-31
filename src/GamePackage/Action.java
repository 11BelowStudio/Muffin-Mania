package GamePackage;

import java.awt.*;

//le ce218 sample code has arrived (Provided by Dr Dimitri Ognibene) (enhanced by me)

public class Action {


    int directionalInput;
    boolean directionPressed;
    boolean space; //whether or not the spacebar has been pressed
    boolean theAnyButton;


    boolean clicked; //whether or not the mouse is clicked

    Point clickLocation; //where the mouse was last clicked



    void noAction(){
        directionalInput = -1;
        space = false;
        clicked = false;
        theAnyButton = false;
    }

    public boolean checkForSpacePress(){
        if (space){
            space = false;
            //need to press the space bar again for the next space press to count
            return true;
        } else{
            return false;
        }
    }

    public boolean checkForDirectionalInput(){
        if (directionPressed){
            directionPressed = false;
            //need to press direction again for next direction press to count
            return true;
        } else{
            return false;
        }
    }

    public int getDirectionalInput(){
        return directionalInput;
    }

    public boolean getTheAnyButton(){
        return theAnyButton;
    }

    public boolean checkForClick(){
        if (clicked){
            clicked = false;
            return true;
        } else{
            return false;
        }
    }

    Point getClickLocation(){
        return clickLocation;
    }

    Action(){ noAction(); }

}