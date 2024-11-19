package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuessServiceTest {

    private GuessService guessService;
    private Game game;
    private List<String> playerGuess;
    private List<String> colors;

    @BeforeEach
    void setUp() {
        guessService = new GuessService();
        game = new Game();
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
}