package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.tables.forum.post.Post;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;


@Repository
public interface PostRepos extends JpaRepository<Post, Long> {



}
