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
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907938/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p1-601994fb152e3870d13b616cbc651854920b2e5fcac18984de880aac9d140dc3.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907939/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p2-813835c2021eadac4ad74ae2431fd370d365de9abf06e2173aaafcbeed9c1711.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907941/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p3-905bdd75890dcbe8bf5488fd512a29da73658808799265b534253f6dbd86ab89.jpg",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907943/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p4-c5a44fa700548c7d4e066bfab1512b9eeb81d242d35cd8fdac764bf8e1f5267d.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907944/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p5-4bef74da40fa0caf421160b83cfc28095db9e24048b991a4bc90a37dc239e450.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907946/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p6-42d9bdf0906a8575b077966ab527f6b437ecf137c9df5777926f34fe8cde49b3.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907947/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p7-9c802fa424e1274e794907f910c5f032a1f58ac60b7c84fe616cfe4e4afa9501.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907948/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p8-720e0822ef12c0b354dc796405731013ebda4f9d828a04005350c4a96a9c8e73.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907950/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p9-e62211edef429cc6fbffbd50130b8e8c3ba7553fc3bf4ef85face3db4a8a2893.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907951/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p10-90806417273cc37856c6ab302efd24ddf884131812dffef08496a65726568512.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907952/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p11-7a0d68ba0acc99bb7c8a6bb0ee9f02ddabde3d123e7608ba466b5f22561706f9.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907954/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p12-d7e6a0deadec9bb1eab09aaf499fa60c775088accf4d49574c532d254bf59514.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907955/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p13-81d07af0dea67a496568ecc9402dc7f2fcc3bb23323f8cbffb4e85528be1b08d.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907956/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p14-43f5e3246ce26260e16539b9a12a69858904f492057eee0c1592712cfbd04706.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907958/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p15-0d8c459a14e544c540ad8936307019d24f4c9c9d790efeb07155e9b21bfbca40.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907959/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p16-8645a651c98260ac0ebf52a267de99cde89ce342b71ac9d61784ef65feaef373.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907960/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p17-cfeba1fae2dacd9cb727c7578000381ca02ce0c51067fb5c45e25a30f897db6e.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907962/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p18-d17e7cf8421573585533117f0adca92d5dfe2fa23828a9756b69d894bac25280.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907963/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p19-0368dfd2b38da291dffc58ba410bd66fd1974c59f7f69059f37d1d4beee3a34c.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907964/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p20-91e29565b5e18b755fcc5f91f28b60023760fd3c99af25e9d9c63fe67408ff65.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907966/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p21-d4f37e2438aa5b8e844b72917142f23bcf6e29a8ff29386a5bf3a25d7278a545.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907967/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p22-2ba1229e8d68b58d8b0ed8a565939fa41c3adf6b1fcf17c5194392c116d3323d.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907969/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p23-b6890b566a1ab5a98dd1ae2bc7ceff5646d6839904f2332d008b071523c13555.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907971/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p24-0d044ac6a9bea1f2f5d5705716bc3c08873ad2a330d45da20681474fa80dbc55.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907973/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p25-7ca75cbcd1fd0ba2e36f7215827bcd31e322603af230344ab9853c5b36c317c0.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907974/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p26-b4a0130b5b0dbe63d6072107610a886bc8c1125af9f3ea82086125f37df4cb53.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907975/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p27-7e204f7112bad0eb6cd10c740454a950f53321b5dc512b2f3b2ebcd02d0bf5a9.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907976/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p28-5b095bb152b68868ca511fdc8d5efd99ff9f9c50c4f30860b0dbce3d43129ea5.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907978/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p29-46ecc0b19f2779902ad0b9ad4889d75561c01b27cdb3297476e59e0e816e4752.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907979/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p30-4586cdf24f866522a4ccbd434bc34810d8bd8b6b3b9b35d3be0a4ad89db230ae.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907980/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p31-b50b63b14924a7e07e3d6fc757ebd56a1f21b29b35e6c2446916e3e7f8ba80c1.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907982/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p32-dd4b1e2750238f7ff83840386905a459851eee180b80a6a81a83ac1ac7b36462.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907983/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p33-70e9946d40036d18bf325bd4993b126a5cdfe32bdc4dd73b6d6dba8a7ce2c5e7.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907985/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p34-3eb3dc11bbb43fc6546ead68da010dd20510c49fea12cf78d021a207e85a1cd2.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907986/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p35-892bd44931e64915932dac8a3b31dfdd282a1290ad0353e5f01407a9fe9bb550.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907987/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p36-07026783a0ea60a6e92b3ac794fbcd3e32b5f008082e324e64b016457da258e3.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907989/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p37-eaa185cf27c8b03e0d850f2f54ead26119ef232ff4144b11eb0dbb3995dabccf.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907990/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p38-bf35ad10e69108342005c2c7df6f5887f718ac334b060000d4ff2794c12a5f73.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907992/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p39-c27019378e0cfceff3ee42ff33b323b4c397e859e1d6b4a29682d598133ce344.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907993/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p40-946a80dcba248cdd4a936d4d1216aac65ad71cf1f10f6d73e4e545e23d5dd222.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907994/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p41-d1f98c517c152b45ab20d7e3e29a7d12aa0a63b782e08306ced72aac71a367e1.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907996/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p42-284f37a994477981bc782303cc1f73a1c41eb027bea98f26537c50579b3b2320.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907997/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p43-1521697cdcaebde0f5524e03aebed1d90563f1e6e174e596045a64fed795ea3f.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627907999/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p44-bfd1d08a78d1f6e40b8c5bfff130ce572c11122fcc1556d1da68ffc23728f09c.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627908000/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p45-f4eb9fb6efbf4e8e109053a5e8775131cde02c1ccc96ca204886431589fa5b20.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627908001/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p46-64a6d61a85b7d8c9ee578a264d430dcbf158e156c5c051ee6334ddeadd6565c0.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627908003/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p47-f8f2f039a5828e3f775008e1716404218257743e12f9ef9c0e89f5028321d3e6.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627908005/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p48-d9c5de9cae8d231fd07de0d3b22aed373663af5870962039c147c98c7d9f9bc8.png",
                "https://res.cloudinary.com/mangacrawlers/image/upload/v1627908006/ImagesChapters_upload/Absolute%20Duo/Vol.%201%2C%20Ch.%201:%20no%20name/p49-bb4c0d70a7c8facc6a1d098bd95cb399829b8db0f68d9751cd715e916271b1d9.png"
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
