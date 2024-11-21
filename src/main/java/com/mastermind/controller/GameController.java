package com.mastermind.controller;

import com.mastermind.DTO.AnswerDTO;
import com.mastermind.DTO.GuessDTO;
import com.mastermind.model.Game;
import com.mastermind.model.Guess;
import com.mastermind.service.GameService;
import com.mastermind.service.GuessService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return "newgame"; //todo change address
    }


    @PostMapping("/secretCode")
    @ResponseBody
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
    public ResponseEntity<AnswerDTO> submitGuess(@RequestBody GuessDTO guess, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        Guess resultGuess = guessService.checkGuess(guess.getGuess(), game);
        log.info("FRONTEND COLORS: {}", guess.getGuess());
        log.info("GAME INFO: SECRET CODE{}", game.getSecretCode());
//        log.info("GAME INFO: HISTORY SIZE{}", game.getGuessHistory().size());
//        log.info("HISTORY [0] {}", game.getGuessHistory().get(0));

        log.info("GAME INFO: STATUS:{}", game.getStatus());

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setGameStatus(game.getStatus());
        answerDTO.setBlackHits(resultGuess.getBlackHits());
        answerDTO.setWhiteHits(resultGuess.getWhiteHits());
        //todo clear it

        return ResponseEntity.ok(answerDTO);
    }


}
