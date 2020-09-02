package GamePackage.GameObjects;

import GamePackage.Action;
import GamePackage.Controller;
import utilities.ImageManager;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.*;

public class PlayerObject extends GameObject {

    private final Controller controller;

    /*
    private static final Vector2D[] STATE_POSITIONS = {
            new Vector2D(HALF_WIDTH,HALF_HEIGHT-64),
            new Vector2D(HALF_WIDTH+64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH, HALF_HEIGHT+64),
            new Vector2D(HALF_WIDTH-64, HALF_HEIGHT),
            new Vector2D(HALF_WIDTH,HALF_HEIGHT)
    };*/

    private int currentState;

    private static final int MUFFIN_COOLDOWN_LENGTH = 50;
    private int muffinTimer;
    private static final int MUFFIN_COOLDOWN_ARC_SEGMENT = 360/MUFFIN_COOLDOWN_LENGTH;
    private int muffinArcAngle;
    private static final float MUFFIN_COOLDOWN_ARC_SEGMENT_HUE = 0.33f/MUFFIN_COOLDOWN_LENGTH;
    private static final Color FULL_MUFFIN_TIMER_COLOR = Color.getHSBColor(0.33f, 0.66f, 0.9f);
    private Color muffinTimerColor;

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

    private final StringObject speechText;
    private boolean showingSpeech;
    private static final int SPEECH_TIMER_LENGTH = 50;
    private int speechTimer;

    private boolean usedMuffin;

    private final Image muffinMachineImage;

    private final Image muffineMachineStatusImage;

    private static final int[][] STATUS_SPRITESHEET_COORDS = {
            {0,0,56,16},
            {0,16,56,32}
    };
    //X: state [0]: alive [1]: dead
    //Y: [0]: sx1 [1]: sy1 [2]: sx2 [3]: sy2

    public PlayerObject(Controller c){
        super(MID_VECTOR, new Vector2D());
        controller = c;
        this.alive = true;
        this.img = ImageManager.getImage("PlayerSpritesheet");
        this.currentState = MID_INT;
        usedMuffin = false;
        muffinCount = 0;
        muffinTimer = MUFFIN_COOLDOWN_LENGTH;
        showTimerAsArc = true;
        muffinCountText = new AttributeStringObject<>(
                new Vector2D(64, 64),
                new Vector2D(),
                "",
                muffinCount,
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_30
        );
        speechText = new StringObject(
                new Vector2D(0,-128),
                new Vector2D(),
                "",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_25
        );
        showingSpeech = false;

        this.muffinMachineImage = ImageManager.getImage("MuffinMachine");
        this.muffineMachineStatusImage = ImageManager.getImage("MachineStatus");

    }

    public PlayerObject revive(){
        super.revive();
        this.currentState = MID_INT;
        usedMuffin = false;
        muffinCount = 0;
        muffinTimer = MUFFIN_COOLDOWN_LENGTH;
        showTimerAsArc = true;
        SoundManager.playMuffinRechargeNoise();
        showingSpeech = false;
        updateMuffinCountText();
        this.alive = true;
        return this;
    }

    @Override
    void individualUpdate() {

        usedMuffin = false;

        if(this.alive) {
            //only does this stuff if alive

            //obtains the current frame action
            Action a = controller.getAction();

            //respond appropriately to directional input
            if (a.checkForDirectionalInput()) {
                int directionToGo = a.getDirectionalInput();
                boolean moved = false;
                if (directionToGo != currentState) {
                    switch (directionToGo) {
                        case UP_INT:
                            switch (currentState) {
                                case RIGHT_INT:
                                case LEFT_INT:
                                case MID_INT:
                                    this.currentState = directionToGo;
                                    moved = true;
                                    break;
                                case DOWN_INT:
                                    this.currentState = MID_INT;
                                    moved = true;
                                    break;
                            }
                            break;
                        case RIGHT_INT:
                            switch (currentState) {
                                case UP_INT:
                                case DOWN_INT:
                                case MID_INT:
                                    this.currentState = directionToGo;
                                    moved = true;
                                    break;
                                case LEFT_INT:
                                    this.currentState = MID_INT;
                                    moved = true;
                                    break;
                            }
                            break;
                        case DOWN_INT:
                            switch (currentState) {
                                case RIGHT_INT:
                                case LEFT_INT:
                                case MID_INT:
                                    this.currentState = directionToGo;
                                    moved = true;
                                    break;
                                case UP_INT:
                                    this.currentState = MID_INT;
                                    moved = true;
                                    break;
                            }
                            break;
                        case LEFT_INT:
                            switch (currentState) {
                                case UP_INT:
                                case DOWN_INT:
                                case MID_INT:
                                    this.currentState = directionToGo;
                                    moved = true;
                                    break;
                                case RIGHT_INT:
                                    this.currentState = MID_INT;
                                    moved = true;
                                    break;
                            }
                            break;
                    }
                    if (moved){
                        SoundManager.playMovement();
                    }
                    altFrameTimer = 0; //reset the altFrameTimer
                }

            }

            //update muffinTimer and such if player doesn't have max muffins
            if (muffinCount < MAX_MUFFINS) {
                showTimerAsArc = true;
                if (muffinTimer > 0) {
                    muffinTimer--;
                } else {
                    muffinCount++;
                    muffinTimer = MUFFIN_COOLDOWN_LENGTH;
                    //double-check whether or not the muffin timer still needs to be shown as an arc
                    if(showTimerAsArc = (muffinCount < MAX_MUFFINS)){
                        //play the muffin recharge noise again if there's still more muffins to recharge
                        SoundManager.playMuffinRechargeNoise();
                    }
                    updateMuffinCountText();
                }
                //update the angle for the muffin timer
                muffinArcAngle = 360 - (muffinTimer * MUFFIN_COOLDOWN_ARC_SEGMENT);
                muffinTimerColor = Color.getHSBColor(0.33f-(muffinTimer * MUFFIN_COOLDOWN_ARC_SEGMENT_HUE), 0.66f, 0.9f);
            }

            //use a muffin if the spacebar is pressed
            if (muffinCount > 0) {
                if (a.checkForSpacePress()) {
                    usedMuffin = true;
                    //muffinCount--;
                    //altFrameTimer = ALT_FRAME_LENGTH;
                }
            }

        }

        //count down the altFrameTimer if it's above 0
        if (altFrameTimer > 0) {
            altFrameTimer--;
        }


        //decrease the speech timer if it's currently showing speech
        if (showingSpeech){
            speechTimer--;
            //check if it's still supposed to be showing speech
            showingSpeech = (speechTimer > 0);
        }

    }

    private void updateMuffinCountText(){
        muffinCountText.setValue(muffinCount);
        switch (muffinCount){
            case 0:
            case 1:
                muffinCountText.setPredefinedColour(StringObject.RED_NUM);
                break;
            case 2:
            case 3:
            case 4:
                muffinCountText.setPredefinedColour(StringObject.ORANGE_NUM);
                break;
            case 5:
            case 6:
            case 7:
                muffinCountText.setPredefinedColour(StringObject.YELLOW_NUM);
                break;
            case 8:
            case 9:
            case 10:
                muffinCountText.setPredefinedColour(StringObject.GREEN_NUM);
                break;
        }
    }


    @Override
    void renderObject(Graphics2D g) {

        //draw muffin machine
        g.drawImage(
                muffinMachineImage,
                -96,
                -96,
                192,
                192,
                null
        );

        //draw the appropriate status indicator for the muffin machine
        if (alive){
            g.drawImage(
                    muffineMachineStatusImage,
                    36,
                    -72,
                    92,
                    -56,
                    STATUS_SPRITESHEET_COORDS[0][0],
                    STATUS_SPRITESHEET_COORDS[0][1],
                    STATUS_SPRITESHEET_COORDS[0][2],
                    STATUS_SPRITESHEET_COORDS[0][3],
                    null
            );

            //draw the timer shape (if still alive ofc)

            if(showTimerAsArc) {
                g.setColor(muffinTimerColor);
                //show an arc for the timer if it's supposed to be shown as an arc (not full)
                g.fillArc(
                        -88,
                        40,
                        48,
                        48,
                        90,
                        muffinArcAngle
                );
            } else{
                g.setColor(FULL_MUFFIN_TIMER_COLOR);
                //just draw it as a full white circle if the timer is full
                g.fillOval(
                        -88,
                        40,
                        48,
                        48
                );
            }

        } else{
            //draw the broken machine image if it's dead
            g.drawImage(
                    muffineMachineStatusImage,
                    36,
                    -72,
                    92,
                    -56,
                    STATUS_SPRITESHEET_COORDS[1][0],
                    STATUS_SPRITESHEET_COORDS[1][1],
                    STATUS_SPRITESHEET_COORDS[1][2],
                    STATUS_SPRITESHEET_COORDS[1][3],
                    null
            );
            //don't bother with the timer
        }

        int framePos = 0;
        if (altFrameTimer > 0){ framePos = 1;}

        //draw the player
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


        //draw the muffinCountString object
        muffinCountText.draw(g);

        //show speechText (if appropriate)
        if (showingSpeech){
            speechText.draw(g);
        }
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
            //play the muffin recharge noise
            SoundManager.playMuffinRechargeNoise();
        }
        muffinCount--;
        updateMuffinCountText();
        altFrameTimer = ALT_FRAME_LENGTH;
    }

    public void speak(String s){
        this.speechText.setText(s);
        showingSpeech = true;
        speechTimer = SPEECH_TIMER_LENGTH;
    }

    public PlayerObject kill(){
        this.alive = false; //this is dead
        this.speak("no! my muffins!");
        SoundManager.playNo();
        SoundManager.playBoom();
        return this;
    }

    public void shutUp(){
        showingSpeech = false;
    }
}
