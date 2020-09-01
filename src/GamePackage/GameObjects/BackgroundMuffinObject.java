package GamePackage.GameObjects;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.DT;

public class BackgroundMuffinObject extends GameObject {

    public BackgroundMuffinObject() {
        super(new Vector2D(), new Vector2D());
        this.img = ImageManager.getImage("Muffin");
    }

    @Override
    void individualUpdate() {
        position.addScaled(velocity, DT);
    }

    @Override
    void renderObject(Graphics2D g) {

    }


    public BackgroundMuffinObject revive(){
        super.revive();

        return this;
    }
}
