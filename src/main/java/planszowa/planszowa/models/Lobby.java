package planszowa.planszowa.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany
    @JoinColumn(name = "user_id")
    private List<User> usersInLobby;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
