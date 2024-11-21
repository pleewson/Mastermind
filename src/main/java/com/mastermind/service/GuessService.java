package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.GameStatus;
import com.mastermind.model.Guess;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GuessService {

    private GameService gameService;

    public GuessService(GameService gameService) {
        this.gameService = gameService;
    }

    public Guess checkGuess(List<String> playerGuess, Game game) {
        List<String> secretCode = game.getSecretCode();
        List<String> copyOfSecretCode = new ArrayList<>(secretCode);
        List<String> playerGuessCopy = new ArrayList<>(playerGuess);


        int blackHits = 0;
        int whiteHits = 0;


        for (int i = 0; i < secretCode.size(); i++) {
            if (playerGuessCopy.get(i).equals(copyOfSecretCode.get(i))) {
                blackHits++;
                playerGuessCopy.set(i, "correct");
                copyOfSecretCode.set(i, "correct");
            }
        }

        for (int i = 0; i < copyOfSecretCode.size(); i++) {
            for (int j = 0; j < copyOfSecretCode.size(); j++) {
                if (!playerGuessCopy.get(i).equals("correct") && !copyOfSecretCode.get(j).equals("correct")) {
                    if (playerGuessCopy.get(i).equals(copyOfSecretCode.get(j))) {
                        copyOfSecretCode.set(j, "correct");
                        whiteHits++;
                        break;
                    }
                }
            }
        }

        checkIfPlayerLose(game);
        checkIfPlayerWon(game,blackHits);


        Guess guess = new Guess(playerGuessCopy, blackHits, whiteHits);
        gameService.addGuessToHistory(guess, game);

        return guess;
    }


    //todo test
    private void checkIfPlayerWon(Game game, int blackHits) {
        if (game.getSecretCode().size() == blackHits) {
            game.setStatus(GameStatus.PLAYER_WON);
        }
    }


    //todo test
    private void checkIfPlayerLose(Game game) {
        if (game.getGuessHistory().size() == 9) {
            game.setStatus(GameStatus.PLAYER_LOST);
        }
    }

}
