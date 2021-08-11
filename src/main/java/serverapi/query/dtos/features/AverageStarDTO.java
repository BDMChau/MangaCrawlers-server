package serverapi.query.dtos.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AverageStarDTO {

    private double star;
    private Long manga_id;

    public AverageStarDTO(double star, Long manga_id) {
        this.star = star;
        this.manga_id = manga_id;
    }
}
