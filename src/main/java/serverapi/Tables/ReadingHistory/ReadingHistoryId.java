//package serverapi.ReadingHistory;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Embeddable
//public class ReadingHistoryId implements Serializable {
//    @Column(name = "readingHistory_id", nullable = false)
//    @SequenceGenerator(
//            name = "readinghistory_sequence",
//            sequenceName = "readinghistory_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "readinghistory_sequence" // same as NAME in SequenceGenerator
//    )
//    private Long readingHistory_id;
//
//    private Long manga_id;
//    private Long user_id;
//    private Long chapter_id;
//}
