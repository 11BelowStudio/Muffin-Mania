package GamePackage.GameObjects;

import GamePackage.Action;
import GamePackage.Constants;
import GamePackage.Controller;
import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.*;

public class PlayerObject extends GameObject {

    private Controller controller;

    private static final Vector2D STATE_POSITIONS[] = {
            new Vector2D(HALF_WIDTH,HALF_HEIGHT-64),
            new Vector2D(HALF_WIDTH+64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH, HALF_HEIGHT+64),
            new Vector2D(HALF_WIDTH-64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH,HALF_HEIGHT)
    };

    private int currentState;

    private static int MUFFIN_COOLDOWN_LENGTH = 20;
    private int muffinTimer;

    private static int MAX_MUFFINS = 10;
    private int muffinCount;

    private static int ALT_FRAME_LENGTH = 10;
    private int altFrameTimer;

    private static final int[][][] PLAYER_SPRITESHEET_COORDS = {
            {{0,0,64,64},{64,0,128,64}},
            {{0,64,64,128},{64,64,128,128}},
            {{0,128,64,192},{64,128,128,192}},
            {{0,192,64,256},{64,192,128,256}},
            {{0,256,64,320},{64,256,128,320}}
    };
    //X: state
    //Y: which frame (0: normal, 1: alt frame)
    //Z: [0]: sx1 [1]: sy1 [2]: sx2 [3]: sy2


    private boolean usedMuffin;

    public PlayerObject(Controller c){
        super(STATE_POSITIONS[MID_INT], new Vector2D());
        controller = c;
        this.alive = true;
        this.img = ImageManager.getImage("PlayerSpritesheet");
        this.currentState = MID_INT;
        usedMuffin = false;
    }

    @Override
    void individualUpdate() {
        Action a = controller.getAction();

        if (a.checkForDirectionalInput()){
            int directionToGo = a.getDirectionalInput();
            if (directionToGo != currentState) {
                switch (directionToGo) {
                    case UP_INT:
                        switch (currentState) {
                            case RIGHT_INT:
                            case LEFT_INT:
                            case MID_INT:
                                this.currentState = directionToGo;
                                break;
                            case DOWN_INT:
                                this.currentState = MID_INT;
                                break;
                        }
                        break;
                    case RIGHT_INT:
                        switch (currentState) {
                            case UP_INT:
                            case DOWN_INT:
                            case MID_INT:
                                this.currentState = directionToGo;
                                break;
                            case LEFT_INT:
                                this.currentState = MID_INT;
                                break;
                        }
                        break;
                    case DOWN_INT:
                        switch (currentState) {
                            case RIGHT_INT:
                            case LEFT_INT:
                            case MID_INT:
                                this.currentState = directionToGo;
                                break;
                            case UP_INT:
                                this.currentState = MID_INT;
                                break;
                        }
                        break;
                    case LEFT_INT:
                        switch (currentState) {
                            case UP_INT:
                            case DOWN_INT:
                            case MID_INT:
                                this.currentState = directionToGo;
                                break;
                            case RIGHT_INT:
                                this.currentState = MID_INT;
                                break;
                        }
                        break;
                }

                altFrameTimer = 0;
                this.position.set(STATE_POSITIONS[currentState]);
            }

        }

        if (altFrameTimer > 0){
            altFrameTimer--;
        }

        if (muffinCount < MAX_MUFFINS){
            if (muffinTimer > 0){
                muffinTimer--;
            } else {
                muffinCount++;
                muffinTimer = MUFFIN_COOLDOWN_LENGTH;
            }
        }

        if (muffinCount > 0){
            if(a.checkForSpacePress()){
                usedMuffin = true;
                muffinCount--;
                altFrameTimer = ALT_FRAME_LENGTH;
            }
        }

    }


    @Override
    void renderObject(Graphics2D g) {
        int framePos = 0;
        if (altFrameTimer > 0){ framePos = 1;}

        g.drawImage(
                img,
                -32,
                -32,
                64,
                64,
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][0],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][1],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][2],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][3],
                null
        );
    }

    public boolean checkIfUsedMuffin(){
        if (usedMuffin){
            usedMuffin = false;
            return true;
        }
        return false;
    }

    public int getCurrentState(){
        return currentState;
    }
}
