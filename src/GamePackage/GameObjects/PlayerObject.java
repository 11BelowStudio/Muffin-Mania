package GamePackage.GameObjects;

import GamePackage.Action;
import GamePackage.Controller;
import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.*;

public class PlayerObject extends GameObject {

    private final Controller controller;

    private static final Vector2D[] STATE_POSITIONS = {
            new Vector2D(HALF_WIDTH,HALF_HEIGHT-64),
            new Vector2D(HALF_WIDTH+64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH, HALF_HEIGHT+64),
            new Vector2D(HALF_WIDTH-64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH,HALF_HEIGHT)
    };

    private int currentState;

    private static final int MUFFIN_COOLDOWN_LENGTH = 20;
    private int muffinTimer;
    private static final int MUFFIN_COOLDOWN_ARC_SEGMENT = 360/MUFFIN_COOLDOWN_LENGTH;
    private int muffinArcAngle;

    private boolean showTimerAsArc;

    private static final int MAX_MUFFINS = 10;
    private int muffinCount;

    private static final int ALT_FRAME_LENGTH = 10;
    private int altFrameTimer;

    private static final int[][][] PLAYER_SPRITESHEET_COORDS = {
            {{-32,-96,32,-32, 0,0,64,64},{-32,-96,32,-32, 64,0,128,64}},
            {{32,-32,96,32, 0,64,64,128},{32,-32,96,32, 64,64,128,128}},
            {{-32,32,32,96, 0,128,64,192},{-32,32,32,96, 64,128,128,192}},
            {{-96,-32,-32,32, 0,192,64,256},{-96,-32,-32,32, 64,192,128,256}},
            {{-32,-32,32,32, 0,256,64,320},{-32,-32,32,32, 64,256,128,320}}
    };
    //X: state
    //Y: which frame (0: normal, 1: alt frame)
    //Z: [0]: dx1 [1]: dy1 [2]: dx2 [3]: dy2 [4]: sx1 [5]: sy1 [6]: sx2 [7]: sy2


    private final AttributeStringObject<Integer> muffinCountText;

    private boolean usedMuffin;

    public PlayerObject(Controller c){
        super(STATE_POSITIONS[MID_INT], new Vector2D());
        controller = c;
        this.alive = true;
        this.img = ImageManager.getImage("PlayerSpritesheet");
        this.currentState = MID_INT;
        usedMuffin = false;
        muffinCount = MAX_MUFFINS;
        showTimerAsArc = false;
        muffinCountText = new AttributeStringObject<>(
                new Vector2D(64, 64),
                new Vector2D(),
                "",
                muffinCount,
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_30
        );
    }

    public PlayerObject revive(){
        super.revive();
        this.currentState = MID_INT;
        usedMuffin = false;
        muffinCount = MAX_MUFFINS;
        showTimerAsArc = false;
        updateMuffinCountText();
        return this;
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
            showTimerAsArc = true;
            if (muffinTimer > 0){
                muffinTimer--;
            } else {
                muffinCount++;
                muffinTimer = MUFFIN_COOLDOWN_LENGTH;
                showTimerAsArc = (muffinCount < MAX_MUFFINS);
                updateMuffinCountText();
            }
            muffinArcAngle = 360-(muffinTimer*MUFFIN_COOLDOWN_ARC_SEGMENT);
        }

        if (muffinCount > 0){
            if(a.checkForSpacePress()){
                usedMuffin = true;
                //muffinCount--;
                //altFrameTimer = ALT_FRAME_LENGTH;
            }
        }

    }

    private void updateMuffinCountText(){
        muffinCountText.setValue(muffinCount);
    }


    @Override
    void renderObject(Graphics2D g) {
        int framePos = 0;
        if (altFrameTimer > 0){ framePos = 1;}

        g.drawImage(
                img,
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][0],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][1],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][2],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][3],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][4],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][5],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][6],
                PLAYER_SPRITESHEET_COORDS[currentState][framePos][7],
                null
        );

        g.setColor(Color.white);

        if(showTimerAsArc) {
            //show an arc for the timer if it's supposed to be shown as an arc (not full)
            g.fillArc(
                    -88,
                    40,
                    48,
                    48,
                    -90,
                    muffinArcAngle
            );
        } else{
            //just draw it as a full circle if the timer is full
            g.fillOval(
                    -88,
                    40,
                    48,
                    48
            );
        }

        //draw the muffinCountString object
        muffinCountText.draw(g);
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

    public void confirmMuffinUsage(){
        //called if a muffin was used successfully
        if (muffinCount == MAX_MUFFINS){
            muffinTimer = MUFFIN_COOLDOWN_LENGTH; //reset the cooldown timer if the player had max muffins when they used the first muffin
            muffinArcAngle = 0;
            showTimerAsArc = true;
        }
        muffinCount--;
        updateMuffinCountText();
        altFrameTimer = ALT_FRAME_LENGTH;
    }
}
