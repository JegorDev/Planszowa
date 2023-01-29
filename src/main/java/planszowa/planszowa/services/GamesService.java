package planszowa.planszowa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import planszowa.planszowa.converters.GameConverter;
import planszowa.planszowa.dto.GameDto;
import planszowa.planszowa.models.Game;
import planszowa.planszowa.models.User;
import planszowa.planszowa.repositories.GameRepository;

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
}
