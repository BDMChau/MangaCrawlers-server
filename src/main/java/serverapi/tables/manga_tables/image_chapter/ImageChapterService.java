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
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907938/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p1-a8cea15a459e45c52e0ee985002f9aa3bb69e12d4fb3098f6e3a851bc1b19f33.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907939/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p2-725bcf21d5a04a6f7ef25eb5727730764519b64d1a76f13c7f8d060c9c971568.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907941/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p3-c94dfada0ac9c909631a78791cf6bac3a22881bf7a07838c23d4aeba923386a7.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907942/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p4-6d1d645706c39e3b2181e64ed78395252fc60b00493660778e0f145b9f7a7d03.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907943/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p5-71e2f1a55692e0efeddc9f09fc4c5c4e910d694774b74d33dd6c79062f79d3fd.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907945/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p6-d405709a57db6303032584fcfbbc934c67fffad17b38fcfada83b33fa6f028af.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907947/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p7-e15d6785327ebed6645b2fe23e72a0a10dd8942a751f15b876859ded4bfa52a5.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907949/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p8-0908cd029d9fae5cb82eaa97304f563a33f4284ddafeeba2a609813b6dd3225b.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907950/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p9-07407e6eb955201732a961620e4733c66a9e42f54ded0a559906fc346166b59b.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907952/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p10-b92534335b39f88968396156a5c634d835674a966a5307ee1e268211ff1389a9.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907953/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p11-2cefc152289585c7091a0ad7fd250f487f14bae17a1796bef29b4434413f89c5.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907955/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p12-5017a7328b65b007de14b893990635eb36d5b474702867452dad6401b07478af.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907957/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p13-36cf31b52f7477276d78b89e5f64e7b39d614997234dc534f4ba9b31bded3e65.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907959/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p14-d8cd1904240439ddbe4fd5c3fb5bc3f423cd856f7e78275d206dbaae86ede91b.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907960/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p15-c05b14aec856b38ab96581eb776e3b32c4487bee2d680760fd2c7c6cba1189ca.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907962/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p16-31babde1b4b152b6a70c43157ac013d750d660ba48eea2b4b5dff82dfc9c13d3.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907963/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p17-1dfc52463be529cde85ad62f4e91ef014846e2793502af09e3ba28f7ba68e891.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907964/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p18-5e2c3e571ba98729859ea0f0f193107d57e114fdc520e95ce730a51ee90cae52.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907966/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p19-4effbfefd54b45d50e895cd8fb021decc21b6d26b33fb3b29f300ae4556d9fe7.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907967/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p20-01a45d40301fbdca1e4386b8173e3b3dfb5828ae3a2ff88e87cf0d51f5d38030.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907968/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p21-5fb177ac33e01da48626b9ed89103e397088d6e3f9ca50e04566c6d29a9cb6fb.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907970/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p22-9bc15c4a5dc9efbe99e6e2a721a90ebdc400b293bc8536f144bac1feb821d671.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907972/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p23-cfb9fcfa1867edaa8d3e775aff5dae53e9a2c90cc7e257e0f6be636c19f1f615.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907973/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p24-80d5095d87eb4ab5213638e5e01349f608100452a9b55123c1f9cec6ee795ee8.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907974/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p25-a570c243ed83b2c5819bf6263c9fe31a26ee83959b46e56c68f58ee6ecf5cb4f.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907976/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p26-8c63826e7d17501ab47e8330951ee91c1bc924888ccb43f9627b95ea41cbc5c6.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907977/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p27-6a1f177b89a50b1a5b458ea0a4cbb525f65138209e99fe1a91a8483708b1b0b4.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907978/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p28-107bda68957ea8586da4dfe46a48cb71524267a1dd9f325a142e62ea383e6987.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907980/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p29-b78039d6b954e5284e2b1136dba5f65472b7a5bb310ab23477bb0f3875b8eb54.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907981/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p30-12a919ecb2d3508c2ee8fc5d6f7e726977bbd6eacd0471b90c41049c91cc76a4.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907982/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p31-ba8232e5fa025677fb63fd81723bfc355b2fa162129b7a817ffc01830da3fa0a.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907984/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p32-4a1bf0a17bcc5c305668f94a8a009785b2a2b5624f314aeadb86f1195da1ee4b.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907985/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p33-523243a7d76a75aab738e0b9ac8e3426263cf68277e7b59cff3a4eb4a2285d8e.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907987/ImagesChapters_upload/Absolute%20Duo/Vol.%20%2C%20Ch.%203:%20no%20name/p34-0840e4ff298665eddf54479ffe8350e926549bf1a845ceeb624ec3d988c9afa9.jpg"
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
