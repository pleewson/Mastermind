package com.mastermind.controller;

import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import com.mastermind.service.GameService;
import com.mastermind.service.GuessService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class GameController {

    private GameService gameService;
    private GuessService guessService;

    public GameController(GameService gameService, GuessService guessService) {
        this.gameService = gameService;
        this.guessService = guessService;
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }


    @PostMapping("/startGameWithComputer")
    public String startGame(@RequestParam int amount, HttpSession session, Model model) {
        Game game = gameService.startGameWithComputer(amount);
        session.setAttribute("game", game);
        model.addAttribute("difficulty", amount);
        log.info("colors in game: {}", game.getSecretCode());
        return "secret-code"; //todo change address
    }


    @PostMapping("/secretCode")
    public String submitResponses(@RequestParam List<String> colors, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        game.setSecretCode(colors);

        model.addAttribute("difficulty", game.getSecretCode().size());
        model.addAttribute("currentRound", 1);


        log.info("response: {}", colors);
        return "game";
    }


    //1
    @PostMapping("/game")
    public String beginGame(HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        model.addAttribute("currentRound", game.getGuessHistory().size() + 1);
        model.addAttribute("guessHistory", game.getGuessHistory());
        model.addAttribute("difficulty", game.getSecretCode().size());

        return "game";
    }


    //2
    @PostMapping("/submitGuess")
    public String submitGuess(@RequestParam List<String> colors, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");

        guessService.checkGuess(colors, game);
        log.info("FRONTEND COLORS: {}", colors);
        log.info("GAME INFO: SECRET CODE{}", game.getSecretCode());
        log.info("GAME INFO: HISTORY SIZE{}", game.getGuessHistory().size());
        log.info("HISTORY [0] {}", game.getGuessHistory().get(0));

//        log.info("GAME INFO: STATUS:{}", game.getStatus());

        model.addAttribute("currentRound", game.getGuessHistory().size() + 1);
        model.addAttribute("guessHistory", game.getGuessHistory());
        model.addAttribute("difficulty", game.getSecretCode().size());

//        model.addAttribute("gameStatus", game.getStatus());


        return "game";
    }


}
