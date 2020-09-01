package GamePackage;

import GamePackage.GameObjects.*;
import utilities.HighScoreHandler;
import utilities.ImageManager;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static GamePackage.Constants.*;

public class Game extends Model{

    private final PlayerObject you;


    private double score;
    private final AttributeStringObject<Integer> scoreText;

    private double muffinStreak;

    private final Stack<Muffin> muffinStack;

    private final Stack<CupcakeBot> cupcakeBotStack;

    private final List<StringObject> hudObjects;
    private final List<StringObject> aliveHUD;

    private final ArrayList<Muffin> tMuffins;
    private final ArrayList<CupcakeBot> tCupcakes;
    private ArrayList<Muffin> tAliveMuffins;
    private ArrayList<CupcakeBot> tAliveCupcakes;

    private final ArrayList<Muffin> rMuffins;
    private final ArrayList<CupcakeBot> rCupcakes;
    private ArrayList<Muffin> rAliveMuffins;
    private ArrayList<CupcakeBot> rAliveCupcakes;

    private final ArrayList<Muffin> bMuffins;
    private final ArrayList<CupcakeBot> bCupcakes;
    private ArrayList<Muffin> bAliveMuffins;
    private ArrayList<CupcakeBot> bAliveCupcakes;

    private final ArrayList<Muffin> lMuffins;
    private final ArrayList<CupcakeBot> lCupcakes;
    private ArrayList<Muffin> lAliveMuffins;
    private ArrayList<CupcakeBot> lAliveCupcakes;

    private final ArrayList<CupcakeSpawnerObject> cupcakeSpawners;


    private int cutsceneState;
    private int cutsceneTimer;
    private final int CUTSCENE_STATE_LENGTH = 50;
    private boolean stillInCutscene;

    private boolean canUseMuffins;
    private boolean gameHasProperlyStarted;

    private int cupcakeSpawnTimer;
    private static final int MIN_CUPCAKE_SPAWN_TIME = 25; //5
    private static final int RANGE_CUPCAKE_SPAWN_TIME = 85; //25

    private static final int MAX_MUFFIN_OBJECTS = 15;
    private static final int MAX_CUPCAKE_OBJECTS = 15;

    private boolean gameJustEnded;

    private final Image background;



    public Game(Controller ctrl, HighScoreHandler hs) {
        super(ctrl, hs);
        you = new PlayerObject(ctrl);


        scoreText = new AttributeStringObject<>(
                new Vector2D(HALF_WIDTH, 20),
                new Vector2D(),
                "Score: ",
                0,
                StringObject.MIDDLE_ALIGN
        );

        muffinStack = new Stack<>();

        cupcakeBotStack = new Stack<>();


        cupcakeSpawners = new ArrayList<>();

        tMuffins = new ArrayList<>();
        tCupcakes = new ArrayList<>();
        tAliveMuffins = new ArrayList<>();
        tAliveCupcakes = new ArrayList<>();
        rMuffins = new ArrayList<>();
        rCupcakes = new ArrayList<>();
        rAliveMuffins = new ArrayList<>();
        rAliveCupcakes = new ArrayList<>();
        bMuffins = new ArrayList<>();
        bCupcakes = new ArrayList<>();
        bAliveMuffins = new ArrayList<>();
        bAliveCupcakes = new ArrayList<>();
        lMuffins = new ArrayList<>();
        lCupcakes = new ArrayList<>();
        lAliveMuffins = new ArrayList<>();
        lAliveCupcakes = new ArrayList<>();

        hudObjects = new ArrayList<>();
        aliveHUD = new ArrayList<>();

        background = ImageManager.getImage("BasicBG");

    }

    @Override
    void drawModel(Graphics2D g) {
        //DRAW BACKGROUND FIRST

        g.drawImage(
                background,
                0,
                0,
                GAME_WIDTH,
                GAME_HEIGHT,
                null
        );

        for (CupcakeBot c: tCupcakes) {
            c.draw(g);
        }
        for (Muffin m: tMuffins){
            m.draw(g);
        }

        //DRAW MUFFIN MACHINE

        you.draw(g);

        for (CupcakeBot c: lCupcakes) {
            c.draw(g);
        }
        for (Muffin m: lMuffins){
            m.draw(g);
        }

        for (CupcakeBot c: rCupcakes) {
            c.draw(g);
        }
        for (Muffin m: rMuffins){
            m.draw(g);
        }

        for (CupcakeBot c: bCupcakes) {
            c.draw(g);
        }
        for (Muffin m: bMuffins){
            m.draw(g);
        }

        for (CupcakeSpawnerObject c: cupcakeSpawners){
            c.draw(g);
        }

        scoreText.draw(g);

    }

    @Override
    void refreshModelLists() {
        tMuffins.clear();
        tMuffins.addAll(tAliveMuffins);
        tAliveMuffins.clear();
        tCupcakes.clear();
        tCupcakes.addAll(tAliveCupcakes);
        tAliveCupcakes.clear();

        rMuffins.clear();
        rMuffins.addAll(rAliveMuffins);
        rAliveMuffins.clear();
        rCupcakes.clear();
        rCupcakes.addAll(rAliveCupcakes);
        rAliveCupcakes.clear();

        bMuffins.clear();
        bMuffins.addAll(bAliveMuffins);
        bAliveMuffins.clear();
        bCupcakes.clear();
        bCupcakes.addAll(bAliveCupcakes);
        bAliveCupcakes.clear();

        lMuffins.clear();
        lMuffins.addAll(lAliveMuffins);
        lAliveMuffins.clear();
        lCupcakes.clear();
        lCupcakes.addAll(lAliveCupcakes);
        lAliveCupcakes.clear();


        hudObjects.clear();
        hudObjects.addAll(aliveHUD);
        aliveHUD.clear();
    }

    @Override
    void endThis(){
        hs.recordHighScore((int)score);
        super.endThis();
    }

    @Override
    public Game revive() {
        super.revive();
        return this;
    }

    void startModelMusic(){ }

    void stopModelMusic(){  }

    @Override
    void updateLoop() {


        if (stillInCutscene){
            cutsceneHandler();
        }

        //firstly, update the lanes.
        laneUpdater(UP_INT);
        laneUpdater(RIGHT_INT);
        laneUpdater(DOWN_INT);
        laneUpdater(LEFT_INT);

        //update player
        you.update();

        //the laneUpdater dictates whether or not the game will now be over.
        //therefore, only after that's done, can we proceed to updating the stuff that's important in the gameplay
        if (gameOver){
            //stuff that happens if the game is now over

            //this stuff happens on the first frame of the game being over
            if (gameJustEnded){

                //pretty much kills 'you' and stops the muffin machine from doing its thing
                you.kill();

                gameJustEnded = false;
            }

            //end it when the any button is pressed
            if (ctrl.getTheAnyButton()){
                endThis();
            }


        } else{ //stuff that happens if the game isn't over

            if (canUseMuffins) { //if you've reached the state of the game where you are allowed to use muffins

                //if you used a muffin
                if (you.checkIfUsedMuffin()) {
                    //get the current state
                    int lane = you.getCurrentState();
                    switch (lane) {
                        case MID_INT:
                            //don't allow user to eat muffins until the game has properly started
                            if (gameHasProperlyStarted) {
                                //if you're at mid, confirm the muffin usage, increase the score by 1, apply muffinStreak to the score, update score display
                                you.confirmMuffinUsage();
                                score += 1;
                                score += muffinStreak;
                                updateScoreDisplay();
                                //then increase the muffinStreak modifier by 0.5
                                muffinStreak += 0.5;
                            }
                            break;
                        case UP_INT:
                        case RIGHT_INT:
                        case DOWN_INT:
                        case LEFT_INT:
                            //if you're at a lane, make sure there's stuff in the muffinStack.
                            //if there is, confirm the muffin usage, reset muffinStreak, and then add the muffin to the appropriate lane
                            muffinAdder(lane);
                            break;
                    }
                }
            }

            // CUPCAKE BOT SPAWNING

            //only spawn them in once the game has properly started
            if (gameHasProperlyStarted) {
                if (cupcakeSpawnTimer < 1) {
                    reviveACupcake();
                } else {
                    cupcakeSpawnTimer--;
                }
            }
        }

        //update HUD
        /*
        for (StringObject o: hudObjects){
            o.update();
            if (o.stillAlive()){
                aliveHUD.add(o);
            }
        }*/
        aliveHUD.add(scoreText);

    }

    private void laneUpdater(int laneNumber){
        Muffin[] muffins = {};
        CupcakeBot[] cupcakes = {};
        ArrayList<Muffin> aliveMuffins = new ArrayList<>();
        ArrayList<CupcakeBot> aliveCupcakes = new ArrayList<>();


        //working out what muffins and cupcakes are being used (and obtaining them)
        switch (laneNumber){
            case UP_INT:
                muffins = tMuffins.toArray(muffins);
                cupcakes = tCupcakes.toArray(cupcakes);
                break;
            case RIGHT_INT:
                muffins = rMuffins.toArray(muffins);
                cupcakes = rCupcakes.toArray(cupcakes);
                break;
            case DOWN_INT:
                muffins = bMuffins.toArray(muffins);
                cupcakes = bCupcakes.toArray(cupcakes);
                break;
            case LEFT_INT:
                muffins = lMuffins.toArray(muffins);
                cupcakes = lCupcakes.toArray(cupcakes);
                break;
        }


        for (CupcakeBot c: cupcakes) {
            c.update();
        }

        for (Muffin m: muffins){
            m.update();
        }

        int cupcakeCount = cupcakes.length;
        int muffinCount = muffins.length;
        int smallestCount = cupcakeCount;
        if (cupcakeCount > muffinCount){
            smallestCount = muffinCount;
        }


        //if both lists contain stuff, perform hit detection
        if (cupcakeCount > 0 && muffinCount > 0) {
            boolean notDone = false;
            int i = 0;
            do {
                //the first elements in each list have travelled the furthest. therefore, the first elements will be checked for collision first.
                if(muffins[i].hit(cupcakes[i])){
                    //kill both of them if they hit each other
                    cupcakes[i].kill();
                    muffins[i].kill();
                    //move to the next pair
                    i++;
                    notDone = (i < smallestCount);
                    //but make sure the next pair will exist

                } else {
                    //when the first pair that doesn't hit each other is reached, it's done checking collisions
                    notDone = false;
                }
            } while (notDone);
        }

        //finally, checking for alive stuff

        //cupcakes first
        for (CupcakeBot c: cupcakes){
            if (c.stillAlive()){
                //if it's still alive, see if it's reached its destination
                if (c.hasReachedDestination()){
                    //if a cupcakeBot has, the game is now over (but it's dead, so it gets pushed back onto its stack)
                    if (!gameOver){
                        //game has just ended if it wasn't already over
                        gameJustEnded = true;
                    }
                    gameOver = true;
                    pushCupcakeBackOntoStack(c);
                } else{
                    //if it still hasn't reached it's destination, add it to aliveCupcakes
                    aliveCupcakes.add(c);
                }
            } else{
                //if it died, push it back onto the stack
                pushCupcakeBackOntoStack(c);
            }
        }

        //and now, muffins
        for (Muffin m: muffins){
            if (m.stillAlive()){
                if (m.hasReachedDestination()){
                    pushMuffinBackOntoStack(m);
                } else{
                    aliveMuffins.add(m);
                }
            } else{
                pushMuffinBackOntoStack(m);
            }
        }

        //putting the final alive muffins and cupcakes back in the appropriate stacks
        switch (laneNumber){
            case UP_INT:
                tAliveMuffins = aliveMuffins;
                tAliveCupcakes = aliveCupcakes;
                break;
            case RIGHT_INT:
                rAliveMuffins = aliveMuffins;
                rAliveCupcakes = aliveCupcakes;
                break;
            case DOWN_INT:
                bAliveMuffins = aliveMuffins;
                bAliveCupcakes = aliveCupcakes;
                break;
            case LEFT_INT:
                lAliveMuffins = aliveMuffins;
                lAliveCupcakes = aliveCupcakes;
                break;
        }
    }

    private void pushCupcakeBackOntoStack(CupcakeBot c){
        cupcakeBotStack.push(c);
    }

    private void pushMuffinBackOntoStack(Muffin m){
        muffinStack.push(m);
    }

    private void muffinAdder(int lane){
        //first check if there's stuff in the muffin stack
        if(!muffinStack.isEmpty()) {
            //confirm that a muffin has been used
            you.confirmMuffinUsage();
            //reset the muffinStreak
            muffinStreak = 0;
            //pop the top item from muffinStack, revive it into the appropriate lane
            Muffin tempMuffin = muffinStack.pop().revive(lane);
            switch (lane) {
                case UP_INT:
                    tAliveMuffins.add(tempMuffin);
                    break;
                case RIGHT_INT:
                    rAliveMuffins.add(tempMuffin);
                    break;
                case DOWN_INT:
                    bAliveMuffins.add(tempMuffin);
                    break;
                case LEFT_INT:
                    lAliveMuffins.add(tempMuffin);
                    break;
            }
        }
    }

    private void reviveACupcake(){
        if (canWeSpawnACupcake()){
            cupcakeAdder((int)(Math.random()*4));
            resetCupcakeSpawnTimer();
        }
    }

    private boolean canWeSpawnACupcake(){
        return (!cupcakeBotStack.isEmpty());
    }

    private void resetCupcakeSpawnTimer(){
        cupcakeSpawnTimer = MIN_CUPCAKE_SPAWN_TIME + (int)(Math.random() * RANGE_CUPCAKE_SPAWN_TIME);
    }


    private void cupcakeAdder(int lane){
        CupcakeBot tempCupcake = cupcakeBotStack.pop().revive(lane);
        switch (lane){
            case UP_INT:
                tAliveCupcakes.add(tempCupcake);
                break;
            case RIGHT_INT:
                rAliveCupcakes.add(tempCupcake);
                break;
            case DOWN_INT:
                bAliveCupcakes.add(tempCupcake);
                break;
            case LEFT_INT:
                lAliveCupcakes.add(tempCupcake);
                break;
        }
    }

    @Override
    void setupModel() {
        clearCollections();
        score = 0;
        muffinStreak = 0;

        you.revive();

        cutsceneState = 0;
        cutsceneTimer = CUTSCENE_STATE_LENGTH;
        stillInCutscene = true;

        gameJustEnded = false;

        gameHasProperlyStarted = false;
        canUseMuffins = false;

        for (int i = 0; i < MAX_CUPCAKE_OBJECTS; i++){
            cupcakeBotStack.push(new CupcakeBot());
        }


        for (int i = 0; i < MAX_MUFFIN_OBJECTS; i++) {
            muffinStack.push(new Muffin());
        }


        for (int i = 0; i < 4; i++){
            cupcakeSpawners.add(new CupcakeSpawnerObject(i));
        }

        //aliveCharacters.add(joe.revive());

        updateScoreDisplay();


        resetCupcakeSpawnTimer();

        aliveHUD.add(scoreText.revive());
        //aliveHUD.add(multiplierText.revive());


    }


    @Override
    void clearModelCollections() {
        tCupcakes.clear();
        tMuffins.clear();
        rCupcakes.clear();
        rMuffins.clear();
        bCupcakes.clear();
        bMuffins.clear();
        lMuffins.clear();
        lCupcakes.clear();

        muffinStack.clear();
        cupcakeBotStack.clear();

        cupcakeSpawners.clear();
    }


    private void updateScoreDisplay(){
        scoreText.setValue(scoreToInt());
    }

    private int scoreToInt(){
        return (int)score;
    }


    private void cutsceneHandler(){
        if (cutsceneTimerCheck()){
            switch (cutsceneState){
                case 0:
                    you.speak("oh no");
                    break;
                case 1:
                    you.speak("The cupcake mafia has found me");
                    break;
                case 2:
                    you.speak("and my muffin machine");
                    break;
                case 3:
                    you.speak("If one of their cupcake bots reaches this machine");
                    break;
                case 4:
                    you.speak("I will never be able to eat a muffin again");
                    break;
                case 5:
                    you.speak("But, I can move with the arrow keys to the lane they are coming from");
                    break;
                case 6:
                    you.speak("And then press space to throw a muffin at their robots, destroying it.");
                    break;
                case 7:
                    you.speak("But, that means I can't eat that muffin.");
                    break;
                case 8:
                    you.speak("And I really want to eat it, by pressing space whilst in the middle");
                    break;
                case 9:
                    you.speak("Once I've used a muffin, it takes a moment to return");
                    break;
                case 10:
                    you.speak("and I can only hold up to 10 at once.");
                    break;
                case 11:
                    you.speak("Oh dear. They're starting their attack.");
                    canUseMuffins = true;
                    break;
                case 12:
                    you.speak("YOU'LL NEVER TAKE ME ALIVE!");
                    reviveACupcake();
                    gameHasProperlyStarted = true;
                    break;
                case 21:
                    you.shutUp();
                    stillInCutscene = false;
                    break;
            }
            cutsceneState++;
        }
    }

    private boolean cutsceneTimerCheck(){
        if (cutsceneTimer == 0){
            cutsceneTimer = CUTSCENE_STATE_LENGTH;
            return true;
        } else{
            cutsceneTimer--;
            return false;
        }
    }



}
