package com.strikes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This method exercises the ScoreAMatic class to ensure that the
 * correct final score is calculated for various test cases.
 *
 *  @author       Jerry Mahone
 *  @version      %I%, %G%
 *
 */
class ScoreAMaticTest {

    /**
     * Perfect game test.
     */
    @Test
    void processScoreAllStrikes() {
        String[] args = {"X-X-X-X-X-X-X-X-X-XXX"};
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

          scoreAMatic.processScore(args);
        assertEquals(300, scoreAMatic.finalScore);
    }

    /**
     * All spares test.
     */
    @Test
    void processScoreAllFivesAndSpares() {
        String[] args = {"5/-5/-5/-5/-5/-5/-5/-5/-5/-5/5"};
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

        scoreAMatic.processScore(args);
        assertEquals(150, scoreAMatic.finalScore);
    }

    /**
     * Maximum completely open game test.
     */
    @Test
    void processScoreAllNines() {
        String[] args = {"45-54-36-27-09-63-81-18-90-72"};
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

        scoreAMatic.processScore(args);
        assertEquals(90, scoreAMatic.finalScore);
    }

    /**
     * Wish the alley had gutter guards test.
     */
    @Test
    void processScoreAllGutterBalls() {
        String[] args = {"00-00-00-00-00-00-00-00-00-00"};
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

        scoreAMatic.processScore(args);
        assertEquals(0, scoreAMatic.finalScore);
    }

    /**
     * A typical game test.
     */
    @Test
    void processScoreTypical() {
        String[] args = {"9/-0/-54-70-X-X-53-62-6/-63"};
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

        scoreAMatic.processScore(args);
        assertEquals(125, scoreAMatic.finalScore);
    }
}