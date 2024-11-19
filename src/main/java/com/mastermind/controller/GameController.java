package com.mastermind.controller;

import com.mastermind.model.Game;
import com.mastermind.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
    public String startGame(@RequestParam int amount, HttpSession session) {
        Game game = gameService.startNewGame(amount);
        session.setAttribute("game", game);
        log.info("colors in game: {}", game.getSecretCode());
        return "game";
    }


    //TODO list/4or5params????
    @PostMapping("/submit")
    public String submitResponses(@RequestParam List<String> response) {
        log.info("response: {}", response);
        return "game";
    }

}
