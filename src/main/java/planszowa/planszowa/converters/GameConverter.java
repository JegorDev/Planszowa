package planszowa.planszowa.converters;

import org.springframework.stereotype.Component;

import org.springframework.core.convert.converter.Converter;
import planszowa.planszowa.dto.GameDto;
import planszowa.planszowa.models.Game;

@Component
public class GameConverter implements Converter<Game, GameDto> {
    @Override
    public GameDto convert(Game game){
        return GameDto.builder()
                .name(game.getName())
                .description(game.getDescription())
                .image(game.getImage())
                .maxPlayers(game.getMaxPlayers())
                .minPlayers(game.getMinPlayers())
                .thumbnail(game.getThumbnail())
                .id(game.getId())
                .favourite(game.isFavourite())
                .build();
    }
}
