package planszowa.planszowa.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import planszowa.planszowa.dto.LobbyDto;
import planszowa.planszowa.dto.UserDto;
import planszowa.planszowa.models.Lobby;
import planszowa.planszowa.models.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class LobbyConverter implements Converter<Lobby, LobbyDto> {
    @Override
    public LobbyDto convert(Lobby lobby) {
        UserConverter  userConverter = new UserConverter();

        List<UserDto> usersInLobby = new ArrayList<>();
        for (User user: lobby.getUsersInLobby()){
            usersInLobby.add(userConverter.convert(user));
        }
        return LobbyDto.builder()
                .id(lobby.getId())
                .usersInLobby(usersInLobby)
                .owner(userConverter.convert(lobby.getOwner()))
                .build();
    }
}
