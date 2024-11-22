package com.mastermind.controller;

import com.mastermind.DTO.AnswerDTO;
import com.mastermind.DTO.ColorsDTO;
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

    @GetMapping("/")
    public String getHome() {
        return "home";
    }


    @PostMapping("/start-game-vs-computer")
    public String startGameVsComputer(HttpSession session, Model model) {
        Game game = gameService.startGameWithComputer(4);
        session.setAttribute("game", game);
        log.info("colors in game: {}", game.getSecretCode());
        return "game";
    }


    @GetMapping("/secret-code")
    String getGameVsPlayer() {
        return "secret-code";
    }


    @PostMapping("/start-game-vs-player")
    public ResponseEntity<String> submitResponses(@RequestBody ColorsDTO colorsDTO, HttpSession session) {
        Game game = new Game();
        game.setSecretCode(colorsDTO.getColors()); //todo separate to service
        session.setAttribute("game", game);

        log.info("response secretCode: {}", colorsDTO.getColors());
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/get-game")
    public String getGame(){
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
    public ResponseEntity<AnswerDTO> submitGuess(@RequestBody ColorsDTO colorsDTO, HttpSession session, Model model) {
        Game game = (Game) session.getAttribute("game");
        Guess resultGuess = guessService.checkGuess(colorsDTO.getColors(), game);
        log.info("FRONTEND COLORS: {}", colorsDTO.getColors());
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
