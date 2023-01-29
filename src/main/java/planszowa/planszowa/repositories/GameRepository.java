package planszowa.planszowa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planszowa.planszowa.models.Game;
import planszowa.planszowa.models.User;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    public List<Game> findByUser(User user);
    public List<Game> findByUserId(Long userId);

}
