package com.strikes;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ScoreAMatic is a simple class that represents an American Ten-Pin Bowling scoring system.
 * Given an integer array of a valid sequence of throws for one game of American Ten-Pin Bowling,
 * the system produces the total score for the game.
 *
 * Examples:
 * Input                            String                                          Calculated As Score
 * X-X-X-X-X-X-X-X-X-XXX            (10+10+10) + (10+10+10) + (10+10+10) +          300
 *                                  (10+10+10) + (10+10+10) + (10+10+10) +
 *                                  (10+10+10) + (10+10+10) + (10+10+10) +
 *                                  (10+10+10)
 *
 * 45-54-36-27-09-63-81-18-90-72    9 + 9 + 9 + 9 + 9 + 9 + 9 + 9 + 9 + 9            90
 *
 * 5/-5/-5/-5/-5/-5/-5/-5/-5/-5/-5/5  (10+5) + (10+5) + (10+5) + (10+5) + (10+5) +  150
 *
 * @author       Jerry Mahone
 * @version      %I%, %G%
 *
 */
public class ScoreAMatic {

    public String[] inputArgs;                   // Input argument: expect a String array with one element
    List<String> frames = new ArrayList<>();     // frames - a list of strings containing the 10 frames for a single game

    public int finalScore = -1;                  // Init to an invalid score
    public ScoreAMatic(String[] args) {
        this.inputArgs = args;
    }

    private static final Logger logger = Logger.getLogger(ScoreAMatic.class.getName());

    /**
     * This method validates the line score input, the syntax of the score, and calculates the score.
     * Errors are logged when necessary.
     *
     * @param args  String[]  The input string array contains the line score for the bowler.
     *              For this version, expect args to be a single element; that is, args contains
     *              one bowler's line score for one game.
     *
     */
    public void processScore(String[] args) {
        // Validate the input argument
        boolean isArgsListValid = validateArgs(args);
        if (isArgsListValid == false) {
            logger.log(Level.SEVERE, "Input error: more than one entry in the input\n");
            return;
        }

        // Pull score string from the list of args and validate for proper scoring characters
        String score = args[0];
        boolean isScoreValid = validateScore(score);
        if (isScoreValid == false) {
            logger.log(Level.SEVERE, "Input error: line score message is invalid\n");
            return;
        }

        // Calculate the final score for the user's game
        finalScore = calculateScore();
        if ((finalScore < 0) || (finalScore >300)) {
            // This is an invalid final score
            logger.log(Level.SEVERE, "Output error: the final score is invalid");
            return;
        } else {
            String finalScoreString = "The final score is " + finalScore + "\n";
            logger.log(Level.INFO, finalScoreString);
        }
    }


    /**
     * This method validates that there is just one element in the input String array args.
     * Subsequent versions may allow for multiple players, games, or both.
     *
     * @param args  String[]  The input string array contains the line score for the bowler.
     *              For this version, expect args to be a single element; that is, args contains
     *              one bowler's line score for one game.
     * @return boolean  Indicates if the input String array contains one element or not.
     *
     */
    private boolean validateArgs(String[] args) {
        boolean isArgListValid = false; //Assume false until shown true

        // Confirm that there is just one string to process
        if ( args.length == 1) {
            isArgListValid = true;
        }

        return isArgListValid;
    }

    /**
     * This method validates the input String to ensure that only valid bowling scoring characters
     * are used. The method also verifies that ten frames are present and stores the parsed line
     * score in an ArrayList, frames.
     *
     * @param score  String  Contains one player's single game line score.
     * @return  boolean  Indicates if the line score is valid.
     *
     */
    private boolean validateScore( String score) {
        boolean isValidScore = false;  // Assume false until the score is shown to be valid

        // Need to validate that the score string contains only numerics, X, /, and - characters.
        if (!StringUtils.containsOnly( score, "0123456789X/-")) {
            // Looks like we spotted invalid bowling notation characters
            isValidScore = false;
            return isValidScore;
        }

        // Tokenize the validated input string into
        // - could use Collections and the Stream API if speed was bigger concern here
        StringTokenizer gameST = new StringTokenizer( score, "-");

        while (gameST.hasMoreElements()) {
            frames.add(gameST.nextToken());
        }

        int numOfFrames = frames.size();
        if ( numOfFrames != 10) {
            // Incorrect number of frames, so this is invalid
            isValidScore = false;
            return isValidScore;
        }

        // ToDo (v2.0) Validate that each frame sums to a valid subscore (0-9 or marked frame)

        isValidScore = true;
        return isValidScore;
    }

    /**
     * This method calculates the final score of a single player's game based on their line score.
     * The method works off of the ArrayList frames, which contains the parsed line score broken
     * down into individual frames, which are the elements of the list.
     *
     * @return  int The final score.
     *
     */
    private int calculateScore() {

        // Walk the frame array and keep a running score.
        int runningScore = 0;
        int numOfFrames = frames.size();
        int currentFrame = 1;
        int currentFrameIdx = 0;      // Zero-based
        while (currentFrame <= numOfFrames) {
            // Until the tenth frame, there will be two characters per frame:
            // 1st: [0-9] or X
            // 2nd: [0-9] or /
            // - so, check the frame count first, since the tenth frame is handled differently
            // - next, check if the first ball is a strike or a value between 0 and 9
            // - also, check if the second ball is a spare or a value between 0 and 9

            // First, split the frame into two balls, if frame has more than one ball
            String currentRoll = "";
            String currentRollPlusOne = "";
            String currentRollPlusTwo = "";

            int numBallsInFrame = frames.get(currentFrameIdx).length();
            String[] frameRolls = frames.get(currentFrameIdx).split("");
            if (numBallsInFrame == 1) {
                // First ball is a strike
                currentRoll = frameRolls[0];

                // The next ball is the first bonus ball
                // If this is the current frame is the 9th, then special handling for the bonus balls
                //    must be considered.
                int numBallsInNextFrame = frames.get(currentFrameIdx + 1).length();
                if (numBallsInNextFrame >= 2) {
                    String[] theNextFrameRolls = frames.get(currentFrameIdx + 1).split("");
                    currentRollPlusOne = theNextFrameRolls[0];
                    currentRollPlusTwo = theNextFrameRolls[1];
                } else if (numBallsInNextFrame == 1) {
                    // The first bonus ball is a strike
                    currentRollPlusOne = frames.get(currentFrameIdx+1);

                    // Get the second bonus ball
                    String[] theNextNextFrameRolls = frames.get(currentFrameIdx + 2).split("");
                    currentRollPlusTwo = theNextNextFrameRolls[0]; // We only care about the first one
                }
            } else if (numBallsInFrame == 2) {
                // Standard non-tenth frame where first ball is not a strike or
                // an open tenth frame
                currentRoll = frameRolls[0];
                currentRollPlusOne = frameRolls[1];

                // Get the third ball which may be used if the second ball is a spare
                if (currentFrame < 10) {
                    String[] theNextFrameRolls = frames.get(currentFrameIdx + 1).split("");
                    currentRollPlusTwo = theNextFrameRolls[0];
                }
            } else {
                // The tenth frame with at least one mark
                currentRoll = frameRolls[0];
                currentRollPlusOne = frameRolls[1];
                currentRollPlusTwo = frameRolls[2];
            }

            // Now that we have the correct current and next two rolls determined, we start or continue scoring
            if (currentFrame == numOfFrames) {
                // Tenth Frame - so there may be a third roll in the frame
                if ( currentRoll.equalsIgnoreCase("X")) {
                    // There was a strike in the tenth frame, so there will be two more rolls
                    runningScore += 10;
                    // Add the first bonus ball
                    if (currentRollPlusOne.equalsIgnoreCase("X")) {
                        // First bonus ball also a strike
                        runningScore += 10;
                    }  else {
                        runningScore += Integer.parseInt(currentRollPlusOne);
                    }
                    // Add the second bonus ball
                    if (currentRollPlusTwo.equalsIgnoreCase("X")) {
                        // Second bonus ball also a strike - finished strong
                        runningScore += 10;
                    } else if ( currentRollPlusTwo.equalsIgnoreCase("/")) {
                        // Second bonus ball picked up the spare - well done
                        // - take away the previous ball and add ten
                        runningScore -= Integer.parseInt(currentRollPlusOne);  // Check on this for eg, X6/ scoring
                        runningScore += 10;
                    } else {
                        runningScore += Integer.parseInt(currentRollPlusTwo);
                    }
                } else {
                    // First ball was open, so add it
                    runningScore += Integer.parseInt(currentRoll);

                    // Second ball, tenth frame
                    if ( currentRollPlusOne.equalsIgnoreCase("/")) {
                        // Second ball picked up the spare, so take off prev ball, and add ten
                        runningScore -= Integer.parseInt(currentRoll);
                        runningScore += 10;

                        // Player gets the third roll as well
                        if (currentRollPlusTwo.equalsIgnoreCase("X")) {
                            // Second bonus ball also a strike - finished strong
                            runningScore += 10;
                        } else {
                            runningScore += Integer.parseInt(currentRollPlusTwo);
                        }
                    } else {
                        runningScore += Integer.parseInt(currentRollPlusOne);
                    }

                }
            } else {
                // First through Ninth Frames

                // First ball of frame
                if ( currentRoll.equalsIgnoreCase("X")) {
                    // There was a strike in the frame, so add 10 plus next two values to runningScore
                    runningScore += 10;
                    // Add the first bonus ball
                    if (currentRollPlusOne.equalsIgnoreCase("X")) {
                        runningScore += 10;
                    } else {
                        runningScore += Integer.parseInt(currentRollPlusOne);
                    }
                    // Add the second bonus ball
                    if ( currentRollPlusTwo.equalsIgnoreCase("/")) {
                        // Second ball picked up the spare, so take off prev ball, and add ten
                        runningScore -= Integer.parseInt(currentRollPlusOne);
                        runningScore += 10;
                    } else if ( currentRollPlusTwo.equalsIgnoreCase("X")){
                        // Second ball was a strike, so add ten
                        runningScore += 10;
                    } else {
                        // The second ball was an open value, so add it to the running total
                        runningScore += Integer.parseInt(currentRollPlusTwo);
                    }
                } else {
                    // Add first ball to runningScore
                    runningScore += Integer.parseInt(currentRoll);

                    // Now, add second ball to runningScore, checking for a spare first
                    if ( currentRollPlusOne.equalsIgnoreCase("/")) {
                        // Second ball picked up the spare, so take off prev ball, and add ten
                        runningScore -= Integer.parseInt(currentRoll);
                        runningScore += 10;

                        if (currentRollPlusTwo.equalsIgnoreCase("X")) {
                            // Add the strike as the bonus
                            runningScore += 10;
                        } else {
                            // Picked up the spare, but the ball was open, so add it
                            runningScore += Integer.parseInt(currentRollPlusTwo);
                        }
                    } else {
                        // An open frame, so just add the value of the second ball of the frame
                        runningScore += Integer.parseInt(currentRollPlusOne);
                    }
                }
            }
            currentFrame += 1;  // Going to the next frame
            currentFrameIdx += 1; // Bump to the next frame
        }

        finalScore = runningScore;
        return finalScore;
    }

}
