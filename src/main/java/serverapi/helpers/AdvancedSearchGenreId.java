package serverapi.helpers;

import serverapi.query.dtos.tables.MangaChapterDTO;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.query.repository.manga.MangaRepos;

import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchGenreId {


    public final MangaRepos mangaRepository;

    public AdvancedSearchGenreId(MangaRepos mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public List searchGen(Long finalSecondGenreId, List<MangaGenreDTO> firstList, List<MangaGenreDTO> listGenresMangas){


        List<MangaGenreDTO> secondList = new ArrayList<> ();
        MangaGenreDTO mangaGenreDTO = new MangaGenreDTO ();
        firstList.forEach (items->{
            listGenresMangas.forEach (listGenManga ->{
                if(listGenManga.getGenre_id () == finalSecondGenreId){

                    System.err.println ("check listGenManga hàm"+listGenManga.getManga_id ());
                    mangaGenreDTO.setManga_id (listGenManga.getManga_id ());


                    if(items.getManga_id () == mangaGenreDTO.getManga_id ()){

                        System.err.println ("check trong hàm"+items.getManga_id ());

                        secondList.add (items);

                    }
                }

            });
        });
        return secondList;
    }

    public List showMangaList(List<MangaGenreDTO> firstList, List<MangaChapterDTO> Mangas){

            List<MangaChapterDTO> mangaChapterDTOList = new ArrayList<> ();

            firstList.forEach (items->{

                Mangas.forEach (manga->{

                    if(items.getManga_id ().equals (manga.getManga_id ())){

                        System.err.println ("MangaADD"+manga.getManga_id ());
                        mangaChapterDTOList.add (manga);

                    }
                });
            });
        return mangaChapterDTOList;
    }



}
