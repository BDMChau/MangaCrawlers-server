package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.ReadingHistory.ReadingHistory;

@Repository
public interface ReadingHistoryRepos extends JpaRepository<ReadingHistory, Long> {
}
