package serverapi.tables.user_tables.trans_group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.manga.AuthorRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.user.TransGroupRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.manga_tables.author.Author;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.manga_tables.manga.pojo.MangaInfoPOJO;
import serverapi.tables.user_tables.user.User;
import serverapi.tables.user_tables.user.user.UserService;

import java.util.Map;
import java.util.Optional;

@Service
public class TransGroupService {
    private final UserRepos userRepos;
    private final TransGroupRepos transGroupRepos;
    private final MangaRepos mangaRepos;
    private final AuthorRepos authorRepos;
    private final UserService userService;

    @Autowired
    public TransGroupService(UserRepos userRepos, TransGroupRepos transGroupRepos, MangaRepos mangaRepos, AuthorRepos authorRepos, UserService userService) {
        this.userRepos = userRepos;
        this.transGroupRepos = transGroupRepos;
        this.mangaRepos = mangaRepos;
        this.authorRepos = authorRepos;
        this.userService = userService;
    }

    public ResponseEntity updateManga(Long userId, MangaInfoPOJO mangaInfoPOJO) {
        Optional<User> userOptional = userRepos.findById(userId);
        if(userOptional.isPresent()){
            if(!userOptional.get().getTransgroup().getTransgroup_id().equals(mangaInfoPOJO.getTransGroup().getTransgroup_id())){
                Map<String, Object> err = Map.of("err", "You don't have permission!");
                return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
            }
        }
        String mangaName = mangaInfoPOJO.getManga_name();
        String thumbnail = mangaInfoPOJO.getThumbnail();
        String description = mangaInfoPOJO.getDescription();
        String status = mangaInfoPOJO.getStatus();
        String authorName = mangaInfoPOJO.getManga_authorName();
        if(!authorName.equals(mangaInfoPOJO.getAuthor().getAuthor_name())){
            Author newAuthor = new Author();
            newAuthor.setAuthor_name(authorName);
            authorRepos.saveAndFlush(newAuthor);
        }else {
            authorName = mangaInfoPOJO.getAuthor().getAuthor_name();
        }
        Optional<Author> authorOptional = authorRepos.findAuthorByName(authorName);
        if(mangaName.isEmpty() || thumbnail.isEmpty()
           || description.isEmpty() || status.isEmpty() || authorOptional.isEmpty()){
            Map<String, Object> err = Map.of("err", "Missing credential!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        Author author = authorOptional.get();
        Long mangaId = Long.valueOf(mangaInfoPOJO.getManga_id());
        Long transGroupId = mangaInfoPOJO.getTransGroup().getTransgroup_id();
        Optional<Manga> mangaOptional = transGroupRepos.findMangaByTransIdaAndMangaId(transGroupId, mangaId);
        if(mangaOptional.isEmpty()){
            Map<String, Object> err = Map.of("err", "Manga not found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        ////////////////////////
        Manga manga = mangaOptional.get();
        manga.setAuthor(author);
        manga.setThumbnail(thumbnail);
        manga.setManga_name(mangaName);
        manga.setDescription(description);
        manga.setStatus(status);
        mangaRepos.save(manga);

        return userService.getMangaInfoUploadPage(transGroupId, mangaId);
    }
}
