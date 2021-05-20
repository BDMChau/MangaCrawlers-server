package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SearchCriteriaDTO {
    private String key;
    private String operation;
    private String value;

    public SearchCriteriaDTO(String key, String operation, String value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
