package planszowa.planszowa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import planszowa.planszowa.converters.LobbyConverter;
import planszowa.planszowa.dto.LobbyDto;
import planszowa.planszowa.models.Lobby;
import planszowa.planszowa.models.User;
import planszowa.planszowa.repositories.LobbyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyConverter lobbyConverter;

    public List<LobbyDto> getAllLobbies(){
//                List<LobbyDto> games = lobbyRepository.findAll().stream().map(lobbyConverter::convert).collect(Collectors.toList());

        return lobbyRepository.findAll().stream().map(lobbyConverter::convert).collect(Collectors.toList());
    }

    public LobbyDto createNewLobby(User user){
        Lobby lobby = new Lobby();
        lobby.setOwner(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        lobby.setUsersInLobby(users);
        lobbyRepository.save(lobby);
        return lobbyConverter.convert(lobby);
    }

    public String deleteLobby(Integer lobbyId, User user){
        String message;
        if (!lobbyRepository.existsById(lobbyId)){
            message = "No lobby exists with id "+lobbyId;
        }
        else{
            Lobby lobbyToDelete = lobbyRepository.findById(lobbyId).get();
            if (lobbyToDelete.getOwner().equals(user)){
                lobbyRepository.delete(lobbyToDelete);
                message = "Lobby deleted successfully";
            }
            else {
                message = "You cannot delete a lobby you do not own";
            }
        }
        return message;
    }
    public String joinLobby(Integer lobbyId, User user){
        String message;
        Lobby lobbyToJoin = lobbyRepository.findById(lobbyId).get();
        List<User> usersInLobby = lobbyToJoin.getUsersInLobby();
        if (!lobbyRepository.existsById(lobbyId)){
            message = "No lobby exists with id "+lobbyId;
        }
        else{
            if (!usersInLobby.contains(user)){
                usersInLobby.add(user);
                lobbyToJoin.setUsersInLobby(usersInLobby);
                lobbyRepository.save(lobbyToJoin);
                message = "Successfully joined lobby "+ lobbyId;
            }
            else{
                message = "You are already in lobby "+ lobbyId;
            }
        }
        return message;
    }

    public String leaveLobby(Integer lobbyId, User user){
        String message;
        Lobby lobbyToLeave = lobbyRepository.findById(lobbyId).get();
        List<User> usersInLobby = lobbyToLeave.getUsersInLobby();
        if (usersInLobby.contains(user)){
            usersInLobby.remove(user);
            lobbyToLeave.setUsersInLobby(usersInLobby);
            lobbyRepository.save(lobbyToLeave);
            message = "Successfully left lobby "+ lobbyId;
            if (lobbyToLeave.getOwner().equals(user)){
                lobbyRepository.delete(lobbyToLeave);
                message += " You were the owner of the lobby.It was deleted after you left";
            }
        }
        else{
            message = "You cannot leave the lobby you are not in";
        }
        return message;
    }
}
