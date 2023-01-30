package planszowa.planszowa.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindGameRequest {

    @NotBlank
    private String gameToSearch;

    @NotBlank
    private Integer exact;
}
