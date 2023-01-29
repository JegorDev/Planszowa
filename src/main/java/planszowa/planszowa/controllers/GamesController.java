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
    public List<GameDto> getAvailableGameFromBgg() throws ParserConfigurationException, IOException, SAXException {
        String url = "https://boardgamegeek.com/xmlapi2/search?query=nemesis&type=boardgame&exact=0";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new URL(url).openStream());

        Element itemsElement = document.getDocumentElement();
        NodeList itemNodes = itemsElement.getElementsByTagName("item");
        Integer maxLimit =(itemNodes.getLength() > 10 ? 10 : itemNodes.getLength());
        List<GameDto> gameList = new ArrayList<GameDto>();
        for (int i = 0; i < maxLimit; i++) {
            Element itemElementFirst = (Element) itemNodes.item(i);
            String id = itemElementFirst.getAttribute("id");
            String name = itemElementFirst.getElementsByTagName("name").item(0).getAttributes().getNamedItem("value").getNodeValue();
            String yearPublished = itemElementFirst.getElementsByTagName("yearpublished").item(0).getAttributes().getNamedItem("value").getNodeValue();
            String newUrl = "https://boardgamegeek.com/xmlapi2/thing?id="+id;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documentLocal = dBuilder.parse(new URL(newUrl).openStream());

            Element itemsElementLocal = documentLocal.getDocumentElement();
            NodeList itemNodesLocal = itemsElementLocal.getElementsByTagName("item");

            for (int j = 0; j < itemNodesLocal.getLength(); j++) {
                Element itemElementLocal = (Element) itemNodesLocal.item(j);
                String thumbnail = itemElementLocal.getElementsByTagName("thumbnail").item(0).getFirstChild().getNodeValue();
                String image = itemElementLocal.getElementsByTagName("image").item(0).getFirstChild().getNodeValue();
                String description = itemElementLocal.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
                Integer minPlayers = Integer.parseInt(itemElementLocal.getElementsByTagName("minplayers").item(0).getAttributes().getNamedItem("value").getNodeValue());
                Integer maxPlayers = Integer.parseInt(itemElementLocal.getElementsByTagName("maxplayers").item(0).getAttributes().getNamedItem("value").getNodeValue());
                System.out.println("DUPSKO");
                GameDto tempGame = new GameDto();
                tempGame.setName(name);
                tempGame.setThumbnail(thumbnail);
                tempGame.setImage(image);
                tempGame.setDescription(description);
                tempGame.setMinPlayers(minPlayers);
                tempGame.setMaxPlayers(maxPlayers);
                gameList.add(tempGame);
                }
            }

        return gameList;
    }
}
