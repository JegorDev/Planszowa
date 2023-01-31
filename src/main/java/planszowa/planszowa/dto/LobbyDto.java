package planszowa.planszowa.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LobbyDto {
    private Integer id;

    private List<UserDto> usersInLobby;

    private UserDto owner;
}
