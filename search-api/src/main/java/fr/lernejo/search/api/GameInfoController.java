package fr.lernejo.search.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("")
public class GameInfoController {
    private final GameInfoService gameInfoService;

    public GameInfoController(GameInfoService gameInfoService) {
        this.gameInfoService = gameInfoService;
    }

    @GetMapping("/api/games")
    public Iterable<Game> searchQuery(@RequestParam String query) {
        return gameInfoService.searchData(query);

    }
}
