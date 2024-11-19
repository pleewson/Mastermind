package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.Guess;

import java.util.List;

public class GuessService {

    public Guess checkGuess(List<String> playerGuess, Game game) {
        List<String> secretCode = game.getSecretCode();

        int blackHits = 0;
        int whiteHits = 0;


        for (int i = 0; i < secretCode.size(); i++) {
            if (playerGuess.get(i).equals(secretCode.get(i))) {
                blackHits++;
                playerGuess.set(i, "correct");
                secretCode.set(i, "correct");
            }
        }

        for (int i = 0; i < secretCode.size(); i++) {
            for (int j = 0; j < secretCode.size(); j++) {
                if (!playerGuess.get(i).equals("correct") && !secretCode.get(j).equals("correct")) {
                    if (playerGuess.get(i).equals(secretCode.get(j))) {
                        secretCode.set(j, "correct");
                        whiteHits++;
                        break;
                    }
                }
            }
        }


        return new Guess(playerGuess, blackHits, whiteHits);
    }

}
