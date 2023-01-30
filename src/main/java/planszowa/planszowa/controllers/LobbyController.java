package planszowa.planszowa.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import planszowa.planszowa.models.Lobby;
import planszowa.planszowa.models.User;
import planszowa.planszowa.repositories.LobbyRepository;
import planszowa.planszowa.repositories.UserRepository;
import planszowa.planszowa.services.LobbyService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/lobbies")
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Lobby> getAllOpenLobbies(){
        return lobbyService.getAllLobbies();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Lobby createNewLobby(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return lobbyService.createNewLobby(user);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String deleteLobby(@PathVariable Integer id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return lobbyService.deleteLobby(id, user);
    }

    @PutMapping("/join/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String joinLobby(@PathVariable Integer id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return lobbyService.joinLobby(id, user);
    }

    @PutMapping("/leave/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String leaveLobby(@PathVariable Integer id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        return lobbyService.leaveLobby(id, user);
    }


}
