package serverapi.Tables.UserTables.User;

import serverapi.Query.DTO.ChapterDTO;
import serverapi.Query.DTO.MangaChapterDTO;
import serverapi.Query.Repository.Manga.ChapterRepos;
import serverapi.Query.Repository.Manga.MangaRepos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HelpingUser {
    public HelpingUser(MangaRepos mangaRepository, ChapterRepos chapterRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepos = chapterRepos;
    }

    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepos;

    public List getMangaList(Long transGroupId) {
        List<MangaChapterDTO> mangaList = new ArrayList<>();
        List<MangaChapterDTO> listManga = mangaRepository.getMangaByTransgroup(transGroupId);
        System.err.println("listManga.isEmpty() " + listManga.isEmpty());


        if (!listManga.isEmpty()) {
            List<ChapterDTO> listChapters = chapterRepos.getAllChapter();

            if (!listChapters.isEmpty()) {
                listManga.forEach(manga -> {
                    System.err.println("mangaId " + manga.getManga_id());
                    List<MangaChapterDTO> subMangaList = new ArrayList<>();

                    listChapters.forEach(chapter -> {
                        System.err.println("chapterId " + chapter.getChapter_id());
                        System.err.println("checkkingg " + manga.getManga_id().equals(chapter.getManga_id()));

                        if (manga.getManga_id().equals(chapter.getManga_id())) {
                            MangaChapterDTO mangaChapterDTO = new MangaChapterDTO();
                            mangaChapterDTO.setChapter_id(chapter.getChapter_id());
                            mangaChapterDTO.setChapter_name(chapter.getChapter_name());
                            mangaChapterDTO.setCreatedAt(chapter.getCreatedAt());
                            mangaChapterDTO.setManga_id(manga.getManga_id());
                            mangaChapterDTO.setManga_name(manga.getManga_name());
                            mangaChapterDTO.setThumbnail(manga.getThumbnail());
                            mangaChapterDTO.setStars(manga.getStars());
                            mangaChapterDTO.setStatus(manga.getStatus());

                            subMangaList.add(mangaChapterDTO);
                            System.err.println("mangaChapterDTO " + mangaChapterDTO);
                        }
                    });
                    if (subMangaList.isEmpty()) {
                        MangaChapterDTO mangaChapterDTO = new MangaChapterDTO();
                        mangaChapterDTO.setChapter_id(0L);
                        mangaChapterDTO.setChapter_name(" ");
                        mangaChapterDTO.setCreatedAt(null);
                        mangaChapterDTO.setManga_id(manga.getManga_id());
                        mangaChapterDTO.setManga_name(manga.getManga_name());
                        mangaChapterDTO.setThumbnail(manga.getThumbnail());
                        mangaChapterDTO.setStars(manga.getStars());
                        mangaChapterDTO.setStatus(manga.getStatus());

                        mangaList.add(mangaChapterDTO);
                    } else {
                        MangaChapterDTO maxChapter = subMangaList.stream().max(Comparator.comparing(v -> v.getChapter_id())).get();
                        mangaList.add(maxChapter);
                    }

                });
            }
        }
        return mangaList;
    }
}
