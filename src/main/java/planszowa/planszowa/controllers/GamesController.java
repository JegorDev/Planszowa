package planszowa.planszowa.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import planszowa.planszowa.dto.GameDto;
import planszowa.planszowa.models.Game;
import planszowa.planszowa.models.User;
import planszowa.planszowa.payload.request.FindGameRequest;
import planszowa.planszowa.repositories.GameRepository;
import planszowa.planszowa.repositories.UserRepository;
import planszowa.planszowa.services.GamesService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/games")
public class GamesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamesService gamesService;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<GameDto> getAllGamesForUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
//        List<Game> games = gameRepository.findByUserId(user.getId());
        List<GameDto> games = gamesService.getAllGamesForUser(user);
        return games;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void addGame(@RequestBody Game game){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        game.setUser(user);
        gameRepository.save(game);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<GameDto> getAvailableGameFromBgg(@RequestBody FindGameRequest findGameRequest) throws ParserConfigurationException, IOException, SAXException {
        return gamesService.searchForGamesByApi(findGameRequest);
    }
}
