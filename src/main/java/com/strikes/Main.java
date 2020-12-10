package com.strikes;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // There are lots of great third party logging jars, but for simplicity, just use built in for now
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(final String[] args) {
        ScoreAMatic scoreAMatic = new ScoreAMatic(args);

        // Process the score
        scoreAMatic.processScore(args);

    }

}
