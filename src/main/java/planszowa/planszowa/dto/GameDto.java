package planszowa.planszowa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {
    private Integer id;

    private String name;

    private String thumbnail;

    private String image;

    private String description;

    private Integer minPlayers;

    private Integer maxPlayers;

    private String yearPublished;

    private boolean favourite;
}
