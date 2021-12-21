package serverapi.tables.coin.coin_collect_ways;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.coin.coin_transaction_history.CoinTransactionHistory;
import serverapi.tables.forum.post_category.PostCategory;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "coin_collect_way")
public class CoinCollectWay {
    @Id
    @SequenceGenerator(
            name = "coin_collect_way_sequence",
            sequenceName = "coin_collect_way_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coin_collect_way_sequence" // same as NAME in SequenceGenerator
    )
    private Long collect_way_id;

    @JsonBackReference
    @OneToMany(mappedBy = "coinCollectWay", cascade = CascadeType.ALL)
    private Collection<CoinCollectWay> coinCollectWays;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String collect_way;

}