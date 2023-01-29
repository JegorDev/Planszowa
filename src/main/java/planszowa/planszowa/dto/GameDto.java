package planszowa.planszowa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {
    private String name;

    private String thumbnail;

    private String image;

    private String description;

    private Integer minPlayers;

    private Integer maxPlayers;
}