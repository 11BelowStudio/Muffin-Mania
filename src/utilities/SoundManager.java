package utilities;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.Arrays;
//import java.io.File;


// SoundManager class

//le ce218 sample code has arrived (Provided by Dr Dimitri Ognibene)

//edited slightly by me

public class SoundManager {



    // this may need modifying
    private final static String path = "resources/audio/";

    // methods which do not modify any fields

    private static void play(Clip clip) {
        //clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }


    private static Clip getClip(String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample = AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }


    //TODO: audio

    //actually obtaining the clips

    //sfx
    private final static Clip eatingNoise = getClip("nomnom");
    private final static Clip throwingNoise = getClip("throw");
    private final static Clip botHitNoise = getClip("botDown");
    private final static Clip leftBot = getClip("botLeft");
    private final static Clip upBot = getClip("botAbove");
    private final static Clip rightBot = getClip("botRight");
    private final static Clip downBot = getClip("botUnder");
    private final static Clip muffinRechargeNoise = getClip("MuffinRechargeNoise");
    private final static Clip no = getClip("no");
    private final static Clip mweh = getClip("mweh");
    private final static Clip fanfare = getClip("BattleKazoo");
    private final static Clip boom = getClip("explosion");
    private final static Clip unusedBot = getClip("botAppears");
    //private final static Clip clap = getClip("clap");
    //private final static Clip pink = getClip("pink");
    //private final static Clip blue = getClip("blue");
    //private final static Clip ba = getClip("ba");
    //private final static Clip happyToot = getClip("happy toot");
    //private final static Clip sadToot = getClip("sad toot");
    //private final static Clip newToot = getClip("new toot");
    //private final static Clip du = getClip("du");

    public static void playEatingNoise(){ play(eatingNoise);}
    public static void playThrow(){play(throwingNoise);}
    public static void playBotHit(){play(botHitNoise);}
    public static void playLeftBot(){play(leftBot);}
    public static void playUpBot(){play(upBot);}
    public static void playRightBot(){play(rightBot);}
    public static void playDownBot(){play(downBot);}
    public static void playMuffinRechargeNoise(){play(muffinRechargeNoise);}
    public static void playNo(){play(no);}
    public static void playMovement(){play(mweh);}
    public static void playFanfare(){play(fanfare);}
    public static void playBoom(){play(boom);}
    public static void playUnusedBot(){play(unusedBot);}

    //loops
    private final static Clip introLoop = getClip("IntroLoop");
    private final static Clip gameLoop = getClip("gameTheme");
    private final static Clip menuLoop = getClip("mainMenu");
    //private final static Clip backingLoop = getClip("backing loop");
    //private final static Clip reverbLoop = getClip("reverb backing loop");
    //private final static Clip secondLoop = getClip("the second loop");
    //private final static Clip thirdLoop = getClip("loop the third");
    //private final static Clip fourthLoop = getClip("loop number four");
    //private final static Clip resultsLoop = getClip("results loop");

    //recording whether or not these are looping
    private static boolean introLooping = false;
    private static boolean gameLooping = false;
    private static boolean menuLooping = false;
    private static boolean backingLooping = false;
    private static boolean reverbLooping = false;
    private static boolean secondLooping = false;
    private static boolean thirdLooping = false;
    private static boolean fourthLooping = false;
    private static boolean resultsLooping = false;

    public static void startIntroLoop(){
        if (!introLooping){
            introLoop.setFramePosition(0);
            introLoop.loop(Clip.LOOP_CONTINUOUSLY);
            introLooping = true;
        }
    }

    public static void endIntroLoop(){
        if (introLooping){
            introLoop.loop(0);
            //introLoop.stop();
            introLooping = false;
        }
    }

    public static void startGameLoop(){
        if (!gameLooping){
            gameLoop.setFramePosition(0);
            gameLoop.loop(Clip.LOOP_CONTINUOUSLY);
            gameLooping = true;
        }
    }

    public static void endGameLoop(){
        if(gameLooping){
            gameLoop.loop(0);
            gameLoop.stop();
            gameLooping = false;
        }
    }

    public static void startMenuLoop(){
        if (!menuLooping){
            menuLoop.setFramePosition(0);
            menuLoop.loop(Clip.LOOP_CONTINUOUSLY);
            menuLooping = true;
        }
    }

    public static void endMenuLoop(){
        if (menuLooping){
            menuLoop.loop(0);
            menuLoop.stop();
            menuLooping= false;
        }
    }


    /*
    //arrays for clips that may be played multiple times at once
    private final static Clip[] CLAP_ARRAY = new Clip[6];
    private final static Clip[] PINK_ARRAY = new Clip[6];
    private final static Clip[] BLUE_ARRAY = new Clip[6];

    //cursor values for these arrays
    private static int clapCursor = 0;
    private static int pinkCursor = 0;
    private static int blueCursor = 0;

    //filling these arrays
    static{
        Arrays.fill(CLAP_ARRAY, clap);
        Arrays.fill(PINK_ARRAY,pink);
        Arrays.fill(BLUE_ARRAY,blue);
    }

     */





    /*
    public static void startBacking(){
        if (!backingLooping){
            if (reverbLooping){
                backingLoop.setFramePosition(reverbLoop.getFramePosition());
                endReverb();
            }
            backingLoop.loop(-1);
            backingLooping = true;
        }
    }

    public static void endBacking(){
        if (backingLooping) {
            backingLoop.loop(0);
            backingLoop.stop();
            backingLooping = false;
        }
    }


    public static void startReverb(){
        if (!reverbLooping){
            if (backingLooping){
                reverbLoop.setFramePosition(backingLoop.getFramePosition());
                endBacking();
            }
            reverbLoop.loop(-1);
            reverbLooping = true;
        }
    }

    public static void endReverb(){
        if (reverbLooping) {
            reverbLoop.loop(0);
            reverbLoop.stop();
            reverbLooping = false;
        }
    }

    public static void startSecondLoop(){
        if (!secondLooping){
            if (backingLooping){
                secondLoop.setFramePosition(backingLoop.getFramePosition());
                //endBacking();
            }
            secondLoop.loop(-1);
            secondLooping = true;
        }
    }

    public static void endSecondLoop(){
        if (secondLooping) {
            secondLoop.loop(0);
            secondLoop.stop();
            secondLooping = false;
        }
    }

    public static void startThirdLoop(){
        if(!thirdLooping){
            if(secondLooping){
                thirdLoop.setFramePosition(secondLoop.getFramePosition());
                endSecondLoop();
            }
            thirdLoop.loop(-1);
            thirdLooping = true;
        }
    }

    public static void endThirdLoop(){
        if (thirdLooping){
            thirdLoop.loop(0);
            thirdLoop.stop();
            thirdLooping = false;
        }
    }

    public static void startFourthLoop(){
        if (!fourthLooping){
            if (thirdLooping){
                fourthLoop.setFramePosition(thirdLoop.getFramePosition());
                endThirdLoop();
            }
            fourthLoop.loop(-1);
            fourthLooping = true;
        }
    }

    public static void endFourthLoop(){
        if (fourthLooping){
            fourthLoop.loop(0);
            fourthLoop.stop();
            fourthLooping = false;
        }
    }

    public static void startResultsLoop(){
        if (!resultsLooping){
            endFourthLoop();
            endBacking();
            resultsLoop.loop(-1);
            resultsLooping = true;
        }
    }

    public static void endResultsLoop(){
        if (resultsLooping){
            resultsLoop.loop(0);
            resultsLoop.stop();
            resultsLooping = false;
        }
    }

    */




    /*
    //playing a particular sound
    public static void playPink(){play(pink);}
    public static void playBlue(){play(blue);}

    public static void playClap(){play(clap);}
    public static void playBa(){play(ba);}

    public static void playCorrect(){play(happyToot);}
    public static void playWrong(){play(sadToot);}
    public static void playSpawnNoise(){play(newToot);}

    public static void playDu(){play(du);}
    */

    /*
    //playing the clips that are held in an array of Clips
    private static int playClipHeldInArray(Clip[] clipArray, int arrayCursor){
        //play the clip at the position the cursor points to, increment the cursor value, and return it.
        play(clipArray[arrayCursor]);
        return ((++arrayCursor) % clipArray.length);
    }
     */

    /*

    //public static void playClap() { clapCursor = playClipHeldInArray(CLAP_ARRAY, clapCursor); }

    //public static void playPink(){ pinkCursor = playClipHeldInArray(PINK_ARRAY,pinkCursor); }

    //public static void playBlue(){ blueCursor = playClipHeldInArray(BLUE_ARRAY,blueCursor); }

     */






}