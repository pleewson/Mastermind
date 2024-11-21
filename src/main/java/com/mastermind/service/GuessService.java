package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.GameStatus;
import com.mastermind.model.Guess;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuessService {

    private GameService gameService;

    public GuessService(GameService gameService) {
        this.gameService = gameService;
    }

    public Guess checkGuess(List<String> playerGuess, Game game) {
        List<String> secretCode = game.getSecretCode();
        List<String> copyOfSecretCode = List.copyOf(secretCode);
        List<String> playerGuessCopy = List.copyOf(playerGuess);


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

        checkIfPlayerLose(game);
        checkIfPlayerWon(game, blackHits);

        System.out.println("PLAYER GUESS HUUUUUUUUUUUU COPY " + playerGuessCopy);
        Guess guess = new Guess(playerGuessCopy, blackHits, whiteHits);
        gameService.addGuessToHistory(guess, game);

        game.setSecretCode(copyOfSecretCode); //restore original names

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
        if (game.getGuessHistory().size() == 10) {
            game.setStatus(GameStatus.PLAYER_LOST);
        }
    }

}
