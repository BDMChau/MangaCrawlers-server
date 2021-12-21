package serverapi.tables.coin.coin_transaction_history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.coin.coin_collect_ways.CoinCollectWay;
import serverapi.tables.coin.coin_usage_ways.CoinUsageWay;
import serverapi.tables.user_tables.trans_group.TransGroup;
import serverapi.tables.user_tables.user.User;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "coin_transaction_history")
public class CoinTransactionHistory {
    @Id
    @SequenceGenerator(
            name = "coin_transaction_history_sequence",
            sequenceName = "coin_transaction_history_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coin_transaction_history_sequence" // same as NAME in SequenceGenerator
    )
    private Long transaction_history_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transgroup_id")
    private TransGroup transGroup;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="collect_way_id")
    private CoinCollectWay coinCollectWay;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="usage_way_id")
    private CoinUsageWay coinUsageWay;

    @Column(
            updatable = true,
            columnDefinition = "timestamp with time zone"
    )
    private Calendar created_at;


}