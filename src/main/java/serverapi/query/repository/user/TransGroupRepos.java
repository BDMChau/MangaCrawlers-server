package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.trans_group.TransGroup;

import java.util.Optional;

@Repository
public interface TransGroupRepos extends JpaRepository<TransGroup, Long> {

    @Query("SELECT transgr FROM TransGroup transgr WHERE transgr.transgroup_name = ?1")
    Optional<TransGroup> findByName(String groupName);

    @Query("SELECT transgr FROM TransGroup transgr WHERE transgr.transgroup_email = ?1")
    Optional<TransGroup> findByEmail(String groupEmail);

    @Query("""
           SELECT ma FROM Manga ma
           JOIN TransGroup tg ON tg.transgroup_id = ma.transgroup.transgroup_id
           JOIN Author au ON au.author_id = ma.author.author_id
           WHERE tg.transgroup_id = ?1 and ma.manga_id = ?2
           """)
    Optional<Manga> findMangaByTransIdaAndMangaId(Long transgroup_id, Long manga_id);
}
