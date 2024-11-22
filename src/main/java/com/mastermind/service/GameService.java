package com.mastermind.service;

import com.mastermind.DTO.AnswerDTO;
import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private static final String[] AVAILABLE_COLORS = {"red", "blue", "green", "yellow", "white", "black"};


    public Game startGameWithComputer(int amount){
        Game game = new Game();
        game.setSecretCode(generateRandomColors(amount));
        return game;
    }


    public Game startGameWithPlayer(List<String> colors){
        Game game = new Game();
        game.setSecretCode(colors);
        return game;
    }


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


    public AnswerDTO getAnswer(Game game, Guess guess){
        AnswerDTO answer = new AnswerDTO();
        answer.setGameStatus(game.getStatus());
        answer.setBlackHits(guess.getBlackHits());
        answer.setWhiteHits(guess.getWhiteHits());

        return answer;
    }


    public void addGuessToHistory(Guess guess, Game game) {
        game.getGuessHistory().add(guess);
    }
}
