package serverapi.tables.user_tables.user.user_requests_pending;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user_requests_pending")
public class userRequestsPending {
    @Id
    @SequenceGenerator(
            name = "user_requests_pending_sequence",
            sequenceName = "user_requests_pending_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_requests_pending_sequence" // same as NAME in SequenceGenerator
    )
    private Long user_requests_pending_id;

//    @JsonManagedReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_id")
//    private User sender_id;
//
//    @JsonManagedReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "reciever_id")
//    private User reciever_id;

}

