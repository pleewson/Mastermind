package com.mastermind.service;

import com.mastermind.model.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    GameService gameService;
    @BeforeEach
    void setUp() {
        gameService = new GameService(new Game());
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
}