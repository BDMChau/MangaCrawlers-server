package serverapi.tables.manga_tables.image_chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.ImgChapterRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.tables.manga_tables.chapter.Chapter;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class ImageChapterService {

    private Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private final ChapterRepos chapterRepos;
    private final MangaRepos mangaRepos;
    private final ImgChapterRepos imgChapterRepos;

    @Autowired
    public ImageChapterService(ChapterRepos chapterRepos, MangaRepos mangaRepos, ImgChapterRepos imgChapterRepos) {
        this.chapterRepos = chapterRepos;
        this.mangaRepos = mangaRepos;
        this.imgChapterRepos = imgChapterRepos;
    }

    public ResponseEntity addImages(long chapterID) {


        String[] imagesList = new String[]{
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907938/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p1-ef13aa4c8e9a2bc75671609d4c7edb0f09deae9e91145aa654d7ad7889ee69f8.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907939/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p2-a1a8a877ae4dab9dd4a8113568d94d3c4848c66f1ac23538c8c5a4018b7e5a97.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907941/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p3-918e363f6c34f805fddc3124be254937a499aace2ef68ca313e9adaba3f7df4a.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907942/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p4-a38cbb5d0155cf749cd87016f9c1f48ad238f7c25cf02f21a67ece9565570ba9.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907943/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p5-696a9f0d54911dcf0416dd74dbe35a3a5fb2888dccd0ab0b1c9cc3ddbd78d66a.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907944/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p6-fe3b830f398b62724bc1a9bde586b0808b3bea096e59a795bcae10e0ade78446.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907946/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p7-6a63ab6c5dad3690321d2bc621bb5e2100dc4c59f89090cea5240c67074632b5.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907947/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p8-bd8b4db7c4b7b2942bc3033be85a141fabdf21d9a8bb2da84b897fa0b7747cd8.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907949/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p9-ed416009fea9d401cf6eadc1b29de771cc08d93f3894e1ae2018036dac73d273.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907950/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p10-658f70258a52b212d6334ac6fa23f350bf0863120410f1de3b79ae1b9487fff2.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907951/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p11-ab3d6493efbf8c13bca568eb49c5eb1fc604c62352f2aa9565503e16be8b5f46.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907953/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p12-033294aa06c8161cec34e13b537890ee38d3e6f6d9ef9f290437276164fa282f.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907954/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p13-34299113d0bf03ca045fa4a76b04ca74bac20c0745e2a6f613e3891d1c822bca.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907955/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%200:%20Prologue/p14-624db91cd655b90a94a17e767d87f5bdc841c9d2484e24d33176df05bae615a9.png"
        };

        Optional<Chapter> chapterImgOptional = chapterRepos.findById(chapterID);
        if(chapterImgOptional.isEmpty()){
            Map<String, Object> msg = Map.of("err", "Chapter not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        Chapter chapter = chapterImgOptional.get();

        for (int i = 0; i < imagesList.length; i++) {
            ImageChapter imageChapter = new ImageChapter();
            imageChapter.setChapter(chapter);
            imageChapter.setCreated_at(currentTime);
            imageChapter.setImgchapter_url(imagesList[i]);

            imgChapterRepos.saveAndFlush(imageChapter);
        }

        Map<String, Object> msg = Map.of("msg", "Add images successfully!");
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
