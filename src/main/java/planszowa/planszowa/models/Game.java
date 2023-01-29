package planszowa.planszowa.models;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(	name = "games")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView
    private String name;

    @JsonView
    private String thumbnail;

    @JsonView
    private String image;

    @JsonView
    private String description;

    @JsonView
    private Integer minPlayers;

    @JsonView
    private Integer maxPlayers;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
