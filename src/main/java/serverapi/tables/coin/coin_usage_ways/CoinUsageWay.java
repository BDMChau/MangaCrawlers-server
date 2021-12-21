package serverapi.tables.coin.coin_usage_ways;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serverapi.tables.coin.coin_collect_ways.CoinCollectWay;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "coin_usage_way")
public class CoinUsageWay {
    @Id
    @SequenceGenerator(
            name = "coin_usage_way_sequence",
            sequenceName = "coin_usage_way_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "coin_usage_way_sequence" // same as NAME in SequenceGenerator
    )
    private Long usage_way_id;

    @JsonBackReference
    @OneToMany(mappedBy = "coinUsageWay", cascade = CascadeType.ALL)
    private Collection<CoinUsageWay> coinUsageWays;

//    @ManyToMany(mappedBy = "genre") // variable genre in manga class
//    private Collection<MangaGenre> mangaGenre;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String usage_way;

}