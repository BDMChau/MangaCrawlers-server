package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class ReportUserDTO {

   private int total_user =0;
   private int month =0;



    public ReportUserDTO(int total_user, int month) {
        this.total_user = total_user;
        this.month = month;
    }
}
