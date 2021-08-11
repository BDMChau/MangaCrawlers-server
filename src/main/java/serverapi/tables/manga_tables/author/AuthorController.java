package serverapi.tables.manga_tables.author;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    private  final AuthorService authorService;


    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/getallauthor")
    public ResponseEntity findAllAuthor(){
        return authorService.finaAllAuthor();
    }


    @GetMapping("/getinfo")
    public ResponseEntity getnameAuthorManga(){


       return authorService.getnameAuthorManga();
    }

}
