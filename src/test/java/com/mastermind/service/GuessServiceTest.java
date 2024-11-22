package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.GameStatus;
import com.mastermind.model.Guess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GuessServiceTest {

    private GuessService guessService;
    private Game game;
    private List<String> playerGuess;
    private List<String> colors;

    @BeforeEach
    void setUp() {
        GameService gameService = mock(GameService.class);
        game = new Game();
        guessService = new GuessService(gameService);
        playerGuess = new ArrayList<>();
        colors = new ArrayList<>();
    }

    @Test
    void checkGuess_when1black2white() {
        List<String> playerGuess = new ArrayList<>();
        playerGuess.add("white");
        playerGuess.add("blue");
        playerGuess.add("yellow");
        playerGuess.add("green");

        List<String> colors = new ArrayList<>();
        colors.add("green");
        colors.add("blue");
        colors.add("green");
        colors.add("yellow");
        game.setSecretCode(colors);

        Guess guess = guessService.checkGuess(playerGuess, game);

        assertEquals(1, guess.getBlackHits());
        assertEquals(2, guess.getWhiteHits());
    }

    @Test
    void checkGuess_whenPlayerMissesAllColors() {
        playerGuess.add("white");
        playerGuess.add("blue");
        playerGuess.add("blue");
        playerGuess.add("black");

        colors.add("yellow");
        colors.add("yellow");
        colors.add("green");
        colors.add("red");

        game.setSecretCode(colors);

        Guess guess = guessService.checkGuess(playerGuess, game);
        assertEquals(0, guess.getBlackHits());
        assertEquals(0, guess.getWhiteHits());
    }


    @Test
    void checkGuess_3_whenPlayerHitAllColors() {
        playerGuess.add("white");
        playerGuess.add("blue");
        playerGuess.add("green");
        playerGuess.add("red");

        colors.add("white");
        colors.add("blue");
        colors.add("green");
        colors.add("red");

        game.setSecretCode(colors);

        Guess guess = guessService.checkGuess(playerGuess, game);

        assertEquals(4, guess.getBlackHits());
        assertEquals(0, guess.getWhiteHits());
    }


    @Test
    void checkGuess_4_whenPlayerHitAllWhite() {
        playerGuess.add("yellow");
        playerGuess.add("green");
        playerGuess.add("blue");
        playerGuess.add("green");

        colors.add("green");
        colors.add("blue");
        colors.add("green");
        colors.add("yellow");

        game.setSecretCode(colors);

        Guess guess = guessService.checkGuess(playerGuess, game);

        assertEquals(0, guess.getBlackHits());
        assertEquals(4, guess.getWhiteHits());
    }


    @Test
    public void testCheckIfPlayerWon() {
        game.setSecretCode(List.of("red", "blue", "green", "yellow"));

        guessService.checkIfPlayerWon(game, 4);

        assertEquals(GameStatus.PLAYER_WON, game.getStatus());
    }



    @Test
    public void testCheckIfPlayerLose() {
        List<String> testAnswer = new ArrayList<>();
        testAnswer.add("black");
        testAnswer.add("black");
        testAnswer.add("black");
        testAnswer.add("black");

        Guess tempGuess = new Guess(testAnswer,0,0);

        for (int i = 0; i < 10; i++) {
            game.getGuessHistory().add(tempGuess);
        }

        guessService.checkIfPlayerLose(game);

        assertEquals(GameStatus.PLAYER_LOST, game.getStatus());
    }
}