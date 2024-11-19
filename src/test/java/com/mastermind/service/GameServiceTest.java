package com.mastermind.service;

import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }


    @Test
    void generateRandomColors_givenAmount_returnCorrect() {
        List<String> colorList = gameService.generateRandomColors(4);
        assertEquals(colorList.size(), 4);

        colorList = gameService.generateRandomColors(5);
        assertEquals(colorList.size(), 5);
    }


    @Test
    void generateRandomColors_givenIncorrectAmount_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(3));
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(6));
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(-1));
    }


    @Test
    void addGuessToHistory() {
        Game game = new Game();

        Guess guess = new Guess(new ArrayList<>(Arrays.asList("red", "blue", "green", "yellow")), 2, 1);
        gameService.addGuessToHistory(guess, game);

        guess = new Guess(new ArrayList<>(Arrays.asList("red", "blue", "green", "green")), 3, 0);
        gameService.addGuessToHistory(guess, game);

        guess = new Guess(new ArrayList<>(Arrays.asList("red", "blue", "black", "green")), 4, 0);
        gameService.addGuessToHistory(guess, game);


        assertEquals(3, game.getGuessHistory().size());

        assertEquals(2, game.getGuessHistory().get(0).getBlackHits()); //black hits in 1st tour
        assertEquals(1, game.getGuessHistory().get(0).getWhiteHits()); //white hits in 1st tour

        assertEquals(3, game.getGuessHistory().get(1).getBlackHits()); //black hits in 2nd tour
        assertEquals(0, game.getGuessHistory().get(1).getWhiteHits()); //white hits in 2nd tour

        assertEquals(0, game.getGuessHistory().get(2).getWhiteHits()); //black hits in 3rd tour
        assertEquals(0, game.getGuessHistory().get(2).getWhiteHits()); //white hits in 3rd tour
    }
}