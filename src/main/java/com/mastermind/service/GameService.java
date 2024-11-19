package com.mastermind.service;

import com.mastermind.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameService {
    private Game game;

    public GameService(Game game) {
        this.game = game;
    }

    private static final String[] AVAILABLE_COLORS = {"red", "blue", "green", "yellow", "white", "black"};

    public List<String> generateRandomColors(int amount) {

        if (amount < 4 || amount > 5) {
            throw new IllegalArgumentException("Invalid amount of colors: " + amount);
        }

        List<String> colors = new ArrayList<>();
        Random rnd = new Random();

        for (int i = 0; i < amount; i++) {
            colors.add(AVAILABLE_COLORS[rnd.nextInt(AVAILABLE_COLORS.length)]);
        }

        return colors;
    }


}
