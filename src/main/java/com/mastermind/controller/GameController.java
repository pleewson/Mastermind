package com.mastermind.controller;

import com.mastermind.model.Game;
import com.mastermind.model.GameStatus;
import com.mastermind.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @PostMapping("/start")
    public String startGame(@RequestParam int amount, HttpSession session, Model model) {
        Game game = gameService.startNewGame(amount);
        session.setAttribute("game", game);
        model.addAttribute("difficulty", amount);
        log.info("colors in game: {}", game.getSecretCode());
        return "game";
    }


    @PostMapping("/secretColors")
    public String submitResponses(@RequestParam List<String> colors, HttpSession session) {
        Game game = (Game) session.getAttribute("game");
        game.setSecretCode(colors);
        log.info("response: {}", colors);
        return "home";
    }


    @PostMapping
    public String beginGame(){
        return "beginGame";
    }


    @PostMapping("/playerMove")
    public String playerMove(@RequestParam List<String> colors, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        game.setSecretCode(colors);
        model.addAttribute("gameStatus", game.getStatus());
        log.info("response: {}", colors);
        log.info("GAME INFO: STATUS:{}, HISTORY:{}, SECRET CODE{}", game.getStatus(), game.getGuessHistory().size(), game.getSecretCode());
        return "home";
    }


}
