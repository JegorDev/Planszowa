package planszowa.planszowa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import planszowa.planszowa.models.Lobby;
import planszowa.planszowa.models.User;

import java.util.List;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Integer> {
//    public List<Lobby> findByUser(User user);
//    public List<Game> findByUserId(Long userId);

}
