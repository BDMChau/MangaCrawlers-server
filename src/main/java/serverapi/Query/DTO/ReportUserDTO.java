package serverapi.Query.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportUserDTO {

   private int users =0;
   private int month =0;



    public ReportUserDTO(int users, int month) {
        this.users = users;
        this.month = month;
    }
}
