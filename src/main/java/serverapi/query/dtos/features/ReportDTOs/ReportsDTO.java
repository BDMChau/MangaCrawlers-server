package serverapi.query.dtos.features.ReportDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportsDTO {

   private int values =0;
   private int month =0;




    public ReportsDTO(int values, int month) {
        this.values = values;
        this.month = month;
    }
}
