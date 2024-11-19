package com.mastermind.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void generateRandomColors_givenAmount_returnCorrect() {
        GameService gameService = new GameService();

        List<String> colorList = gameService.generateRandomColors(4);
        assertEquals(colorList.size(), 4);

        colorList = gameService.generateRandomColors(5);
        assertEquals(colorList.size(), 5);
    }


    @Test
    void generateRandomColors_givenIncorrectAmount_shouldThrowException() {
        GameService gameService = new GameService();
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(3));
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(6));
        assertThrows(IllegalArgumentException.class, () -> gameService.generateRandomColors(-1));
    }
}