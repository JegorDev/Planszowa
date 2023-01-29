package planszowa.planszowa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import planszowa.planszowa.models.User;
import planszowa.planszowa.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(@PathVariable("user_id") Long userId, Principal principal) {
        boolean exists = userRepository.existsById(userId);
        if (userRepository.existsById(userId) == false){
            return "Wypierdalaj za brame";
        }
        if (userRepository.findByUsername(principal.getName()).get().getId() == userId){
            return "Jestesi userem tutego de "+ userId;
        }
        return "Spierdalaj to nie jest twoje konto";
    }

//    @GetMapping("/mod")
//    @PreAuthorize("hasRole('MODERATOR')")
//    public String moderatorAccess() {
//        return "Moderator Board.";
//    }
//
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String adminAccess() {
//        return "Admin Board.";
//    }
}