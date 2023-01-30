package planszowa.planszowa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import planszowa.planszowa.converters.GameConverter;
import planszowa.planszowa.dto.GameDto;
import planszowa.planszowa.models.Game;
import planszowa.planszowa.models.User;
import planszowa.planszowa.payload.request.FindGameRequest;
import planszowa.planszowa.repositories.GameRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GamesService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameConverter gameConverter;

    public List<GameDto> getAllGamesForUser(User user){
        List<GameDto> games =gameRepository.findByUserId(user.getId()).stream().map(gameConverter::convert).collect(Collectors.toList());
        return gameRepository.findByUserId(user.getId()).stream()
                .map(gameConverter::convert)
                .collect(Collectors.toList());
    }

    public void markGameAsFavourite(Integer id){
        List<Game> games = gameRepository.findAll();
        for (Game game : games){
            if (game.isFavourite()){
                game.setFavourite(false);
                gameRepository.save(game);
                break;
            }
        }
        Game game = gameRepository.findById(id).get();
        game.setFavourite(true);
        gameRepository.save(game);
    }
    public List<GameDto> searchForGamesByApi(@RequestBody FindGameRequest findGameRequest) throws ParserConfigurationException, IOException, SAXException {
        String url = "https://boardgamegeek.com/xmlapi2/search?query="+findGameRequest.getGameToSearch().strip()+"&type=boardgame&exact="+findGameRequest.getExact();

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
                tempGame.setYearPublished(yearPublished);
                gameList.add(tempGame);
            }
        }
        return gameList;
    }
}
