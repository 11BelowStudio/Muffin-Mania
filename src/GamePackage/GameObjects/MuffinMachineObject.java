package GamePackage.GameObjects;

import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.HALF_HEIGHT;
import static GamePackage.Constants.HALF_WIDTH;

public class MuffinMachineObject extends GameObject {

    //TODO: this
    //spritesheet for the muffin machine
    //co-ordinates for each frame
    //boolean for whether it's destroyed or not
    //etc

    public MuffinMachineObject() {
        super(new Vector2D(HALF_WIDTH,HALF_HEIGHT), new Vector2D());
    }

    @Override
    void individualUpdate() {

    }

    @Override
    void renderObject(Graphics2D g) {

    }
}
