package serverapi.Tables.Manga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import serverapi.Tables.Author.Author;
import serverapi.Tables.Author.AuthorRepository;

import java.util.Calendar;
import java.util.Optional;
import java.util.Scanner;
import java.util.TimeZone;

@Configuration
public class MangaConfig {

    @Bean
    Manga commandLineManga(MangaRepository mangaRepository, AuthorRepository authorRepository) {

//        Manga newManga = new Manga();
//        newManga.setManga_name("horimiya");
//        newManga.setStatus("complete");
//        newManga.setDescription("abc");
//        newManga.setStars(4.5f);
//        newManga.setViews(3000);
//        newManga.setThumbnail("abc url");
//        newManga.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//        newManga.setDate_publication(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//        mangaRepository.save(newManga);


//        Manga newmanga1 = new Manga();
//        newmanga1.setManga_name("Black clover");
//        newmanga1.setStatus("complete");
//        newmanga1.setDescription("abc");
//        newmanga1.setStars(4.5f);
//        newmanga1.setViews(3000);
//        newmanga1.setThumbnail("abc url");
//        newmanga1.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//        newmanga1.setDate_publication(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//        mangaRepository.save(newmanga1);

//
//
//        String manganamearr [] ={"Absolute Duo","Akagami No Shirayukihime","Akatsuki No Yona","Attack On Titan","Black Clover","Bokutachi Benkyou","Dr.Stone","Dungeon Ni Deai O Motomeru No Wa Machigatte Iru Darou Ka","Ero manga sensei",
//                                "Fairy tail","Fate/Stay NIght","Gakusen Toshi Asterisk","God Of High School","Go-Toubun No Hanayome",
//                                "Gun x Clover","Haikyuu!!","Hentai Ouji To Warawanai Neko. Nya!","High-School Dxd"," Isekai wa smartphone to tomoni",
//                                "Jaku-Chara Tomozaki-Kun","Hell's Paradise: Jigokuraku",
//                                "Jujutsu Kaisen","Kaguya-Sama Wa Kokurasetai - Tensai-Tachi No Renai Zunousen",
//                                "Kanojo, Okarishimasu","Kimetsu No Yaiba","Kiss X Sis","Koi To Yobu Ni Wa Kimochi Warui",
//                                "Kuzu No Honkai","Monster Musume No Iru Nichijou","Mushoku Tensei - Isekai Ittara Honki Dasu",
//                                "Under Observation: My First Loves And I","Nanatsu No Taizai","Nisekoi","Rakudai Kishi No Eiyuutan",
//                                "I Shaved. Then I Brought A High School Girl Home","Tensei Shitara Slime Datta Ken","Solo Leveling",
//                                "Sora No Otoshimono","Sousei no Onmyouji","The Rising Of The Shield Hero",
//                                "Tonikaku Cawaii","Tower Of God","Trinity Seven: 7-Nin No Mahoutsukai","Uzaki-Chan Wa Asobitai!",
//                                "Yahari Ore No Seishun Rabukome Wa Machigatte Iru. - Mougenroku"};
////
//
//
//
//        String statusarr [] ={
//                            "Ongoing",   "Ongoing",  "Ongoing",  "Ongoing",  "Ongoing",  "Ongoing","Ongoing","Completed","Ongoing",
//                            "Ongoing", "Completed",  "Ongoing",  "Ongoing",  "Completed","Ongoing","Ongoing","Ongoing",  "Completed",
//                            "Ongoing", "Completed",  "Ongoing","Completed",  "Ongoing",  "Ongoing","Ongoing","Completed","Ongoing",
//                            "Ongoing", "Completed",  "Ongoing",  "Ongoing",  "Ongoing",  "Completed","Completed","Ongoing","Ongoing",
//                            "Ongoing", "Completed","Completed",  "Ongoing",  "Ongoing",  "Ongoing",  "Ongoing",  "Ongoing","Completed"};
//
//
//
//
//
//
//        String descriparr [] ={
//                "After losing a loved one, Tooru Konoe enrolls at Kouryou Academy so as to gain a , a weapon that is his soul made manifest, for the sake of revenge. He was expecting a weapon... What he got was a shield... How is he to seek revenge with something that isn't even a weapon?!",
//                "Shirayuki is an ordinary herbalist citizen in the kingdom of Tanburn with one characteristic that is distinctive: her lovely red hair. Her mom died and her dad is a noble forged a way from Tanburn. As a result of her unique hair colour, her grand-parents were constantly watchful over what she elevated her to manage things by herself and did, but it's made her careful about brought focus in new environment. When Prince Raji, orders her to become his concubine due to this, she cuts her hair and getaways to the nearby kingdom of Clarines. On her way there, she meets and befriends his two aides and Prince Zen. Shirayuki determines to follow the trio to Clarines and effectively gets the antidote when Zen is poisoned by an apple designed for for her. Soon afterwards, Shirayuki passes an examination to get a place to to teach in the palace as a pharmacist. Shirayuki and carries out her responsibilities as a courtroom pharmacist and her pals find a spot in Clarines. She presents her abilities treat and to recognize disease outbreaks. In spite of resistance because of the difference in social position, Shirayuki develops a close connection with Zen, on the span of the narrative.",
//                "Akatsuki no Yona Manga: Yona of the Dawn, known as Akatsuki no Yona in Japan. \"Yona of the Dawn\" , also called Akatsuki no Yona -The girl standing in the blush of morning-) is a Japanese manga series by Mizuho Kusanagi, serialized in Hakusensha's sh?jo manga magazine Hana to Yume from August 2009",
//                "Attack on Titan Manga is a series created by Hajime Isayama. The series is based on a fictional story of humanity’s last stand against man-eating giant creatures known as Titans. The series commenced in 2009 and has been going on for 6 years now. Attack on Titans manga is expected to continue with the success, and even get better with time. According to the series’ editor Kuwakubo Shintaro, there are approximately 3 years’ worth of chapters yet to be published for the extensively popular manga.",
//                " Aster and Yuno were abandoned together at the same church, and have been inseparable since. As children, they promised that they would compete against each other to see who would become the next sorcerous emperor. However, as they grew up, some differences between them became plain. Yuno was a genius with magic, with amazing power and control, while Aster could not use magic at all, and tried to make up for his lack by training physically. When they received their grimoires at age 15, Yuno got a spectacular book with a four-leaf-clover (most people receive a three-leaf-clover), while Aster received nothing at all. However, when Yuno was threatened, the truth about Aster's power was revealed-- he received a five-leaf-clover grimoire, a 'black clover' book of anti-magic. Now the two friends are heading out in the world, both seeking the same goal!",
//                "Yuiga Nariyuki works hard every day to obtain a full-college scholarship by his High School to support his poor family. His headmaster promises that he'll get it if he full-fills a small condition. He has to tutor two resident female geniuses Furuhashi Fumino (Literature-genius) and her friend Ogata Rizu (Science-genius), so that they can get into their dream-college. The catch is that Fumino wants to major in Science and Rizu wants to major in Literature despite them failing constantly in those subjects.",
//                "The science-fiction adventure series follows what happens when suddenly the world's biggest-ever 'crisis' arrives.",
//                "Bell Cranel is just trying to find his way in the world. Of course, in his case, the “world” is an enormous dungeon filled with monsters below a city run by gods and goddesses with way too much time on their hands. He’s got big dreams but not much more when a roll on the random encounter die brings him face-to-face with the girl of his dreams—but what’s an amateur adventurer got to offer a brilliant swordswoman? And what if the lonely goddess who sponsors his solo adventuring gets jealous?",
//                "The 'new sibling romantic comedy' revolves around Masamune Izumi, a light novel author in high school. Masamune's little sister is Sagiri, a shut-in girl who hasn't left her room for an entire year. She even forces her brother to make and bring her meals when she stomps the floor. Masamune wants his sister to leave her room, because the two of them are each other's only family.",
//                "Celestial wizard Lucy wants to join the Fairy Tail, a guild for the most powerful wizards. But instead, her ambitions land her in the clutches of a gang of unsavory pirates led by a devious magician. Her only hope is Natsu, a strange boy she happens to meet on her travels. Natsu's not your typical hero - but he just might be Lucy's best hope. Note: Won the 33rd Kodansha Manga Award for Best Shounen Manga. Won the Society for the Promotion of Japanese Animation's Industry Award for Best Comedy Manga in 2009. Vol. 9 was nominated in the Youth Selection category at the 2010 Angoulême International Comics Festival.",
//                "In Fuyuki City, seven sorcerers will each summon and become 'Masters' to powerful familiars known as 'Servants' in order to wage a secret war for the Holy Grail. The seven groups will battle until only one is left - the one who will be granted a chance to wish upon the Holy Grail. While the origins of the war are obscure, it is set to begin again. Shirou Emiya, out of admiration and gratitude towards his his late foster father, strives to become a 'Hero of Justice' in spite of his lack of talent for sorcery. Unexpectedly, he becomes a participant in the Fifth Holy Grail War when he accidentally summons Saber, who is said to be the strongest servant of all... Based on 'Fate' and 'Unlimited Blade Works', the first and second scenarios of the game.",
//                "he Academy City on water, 'Rikka'. This city, otherwise known as 'Asterisk', was famous for being the world's largest stage for the integrated battle entertainment . The young boys and girls of the belonging to the six academies made their wishes with Shining Armaments in their hands, vying for supremacy -- Amagiri Ayato is one of them. Ayato arrived at Rikka at the invitation of the Student Council President of the Seidoukan Academy, Claudia, and right after that he incurred the wrath of the Julis, and ended up having to duel her. 'If you're obedient, I'll pardon you by grilling you to only about well-done?' The greatest academy battle entertainment, begins here",
//                "The God of High School is a Korean manga that is hosted by the Naver.com a Korean search engine. This full colored manga tells the story of a young 17 high school student named Mori Jin. The main character is an orphan taken in care by his 'grandfather' without any real blood relation.",
//                "Go-Toubun no Hanayome summary is updating. Come visit MangaNelo.com sometime to read the latest chapter of Go-Toubun no Hanayome. If you have any question about this manga, Please don't hesitate to contact us or translate team. Hope you enjoy it.",
//                "Mikado High School trains the best students to be the best mercenary bodyguards. But Morito Hayama, a merc escort student with no rank whatsoever, is suddenly assigned to guard a person so highly valued that no escort has ever survived before. Will his subject survive the experience? Will HE survive?!",
//                "Junior high school pupil Shoyo After viewing a nationwide tournament match on Television, Hinata develops a surprising love of volleyball. Although short tall, he becomes motivated to follow in the footsteps of the tournament's star player, nick-named the 'Little Giant', after viewing his plays. He starts practicing by himself and produces a volley-ball club. Eventually 3 members join the group by his past year of middle-school, driving his two buddies that have been in different teams to join solely for the championship to be persuaded by Hinata. Nevertheless, they can be conquered in their very first tournament match after being challenged by the championship favourite team, including the so called 'King of the Court' Tobio Kageyama, in the initial round. Though the group of Hinata endures a miserable defeat, get the better of him and he vows to finally surpass Kageyama. Fastforward to high school, Hinata enters Karasuno Highschool with all the hopes of joining their club that is volley-ball. Sadly for him, the very man he vowed to surpass seems as among his new team-mates before him.",
//                "Alternate Story of Hentai Ouji to Warawanai Neko.",
//                "Hyoudou Issei is an ordinary yet lecherous highschool student who is killed by his girlfriend, Amano Yuuma, during their first date. Yuma is revealed to be a fallen angel named Reinare who was sent on a mission to eliminate divine weapons. Issei is later reincarnated as a devil by his senpai Rias Gremory; who in return will serve under Rias in fighting against the fallen angels.",
//                "After a freak accident involving some lightning winds up zapping him dead, 15-year-old Mochizuki Touya wakes up to find himself face-to-face with God. “I am afraid to say that I have made a bit of a blunder…” laments the old coot. But all is not lost! God says that he can reincarnate Touya into a world of fantasy, and as a bonus, he gets to bring his smartphone along with! So begins Touya’s adventure in a new, anachronistic pseudo-medieval world. Friends! Laughs! Tears! Inexplicable Deus ex Machina! He sets off on a journey full of wonder as he absentmindedly travels from place to place, following whatever goal catches his fancy. The curtains lift on an epic tale of swords, sorcery, and smartphone apps!",
//                "Life is a shitty game. No matter how hard you try, you cannot overcome the characteristics given to you at birth.' To Tomozaki Fumiya, who feels he is trash-tier and can never compete with the god-tier humans in the world, this phrase is the truth. His one pride is in his position as Nanashi, Japan’s number one player in the video game Atafami. However, one day, when he meets the rank two player face to face, his stance is challenged. Is life actually the greatest game of all?",
//                "Gabimaru the Empty, a former ninja assassin known feared as a heartless husk of a man, spends his days on death row wondering when an executioner skilled enough to so much as harm him will arrive, as he thinks nothing of seeing an end to his meaningless existence... Or so he thought. Then the lady executioner, Asaemon the Beheader, rekindles his hope with an astounding proposition. If he ever wishes to see his beloved wife again, he is, under the auspices of the shogunate, to embark on a perilous voyage to the mysterious mystic island said to house the elixir of immortality. Should he be the one among many rival death-row fiends and scoundrels to find the elixir, he’ll earn a full exoneration, and, more importantly, a chance at an ordinary married life with the light of his life--the woman who made the world seem not so ugly. What awaits them is a journey like no other!",
//                "Yuuji is a genius at track and field. But he has zero interest running around in circles, he's happy as a clam in the Occult Research Club. Although he's only in the club for kicks, things get serious when a real spirit shows up at school! Life's about to get really strange in Sugisawa Town #3 High School!",
//                "Kaguya Shinomiya and Miyuki Shirogane are the members of the incredibly prestigious Shuchi'in Academy's student council, asserting their positions as geniuses among geniuses. All the time they spend together has caused the two of them to develop feelings for each other, but their pride will not allow them to be the one to confess and become the submissive one in the relationship! Love is war, and their battle to make the other confess begins now!",
//                "Kinoshita Kazuya dumped for another guy, he hires Mizuhara Chizuru from an app called Diamond to be his girlfriend to make himself feel better.",
//                "Tanjiro is the oldest son in his family who has lost his father. One day, Tanjiro ventures off to another town to sell charcoal. Instead of going home, he ends up staying the night at someone else's house due to rumors of a demon nearby in the mountains. When he gets home the following day, a terrible tragedy awaits him.",
//                "Keita Suminoe has two older (not blood-related) twin step-sisters who love him in a lustful way and have no problems expressing it. They even like to compete about it; much to the laughter of his friends (which he usually endures without problems). One day they come to his school to give him his lunch, resulting in him getting angry due to the atmosphere. To counteract his anger, they confess their tender feelings for him in front of everyone and leave. Feeling sorry he chases after them and says that he'll study hard in order to get in the same high school as them.",
//                "From Decadence: Once you fall for someone, you can't stop the love. A strange encounter spurs the meeting of Amakusa Ryou, a high spec businessman who's loose with women, and his high school sister's best friend, Arima Ichika. From there, he falls madly in love. On the one hand, he approaches her with almost too straight-forward methods, while she responds simply disgusted, insulting him without hesitation...and he takes it as her way of showing love. This is a romantic comedy about a twisted elite employee and a normal otaku high school girl.",
//                "From MangaHelpers: 17-year-old Awaya Mugi and Yasuraoka Hanabi appear to be the ideal couple. They are both pretty popular, and they seem to suit each other well. However, outsiders don't know of the secret they share. Both Mugi and Hanabi have hopeless crushes on someone else, and they are only dating each other to soothe their loneliness. Mugi is in love with Minagawa Akane, a young teacher who used to be his home tutor. Hanabi is also in love with a teacher, a young man who has been a family friend since she was little. In each other, they find a place where they can grieve for the ones they cannot have, and they share a loneliness-driven physical intimacy. Will things stay like this for them forever?",
//                "Monster Musume No Iru Nichijou, understood in Japan as Everyday Life with Monster Girls and subtitled in Englis Tokuma Shoten in their Monthly Comic Ry now publish in Japan monster Musume No Iru Nichijou Manga? By and magazine Seven Seas Entertainment in the United States, together with the chapters reprinted and collected into eight tank?bon volumes to date. The storyline revolves around Kimihito Kurusu, a Japanese student whose life is thrown into chaos after inadvertently becoming involved using the 'Interspecies Cultural Exchange' plan.",
//                "A 34-years-old NEET otaku is chased out from his house by his family. Unattractive and penniless, he finds that he has reached a dead end in life and realizes that his life actually could have been much better if he had made better choices in the past. Just when he was at the point of regret, he saw a truck moving at a fast speed straight towards three high schoolers in its path. Mustering all the strength he had, he tried to save them and ended up getting run over by the truck, quickly ending his life. The next time he opened his eyes, he was already reincarnated into a world of sword and magic as Rudeus Greyrat. Born to a new world, a new life, Rudeus decided that, 'This time, I'll really live my life to the fullest with no regret!' Thus, starts the journey of a man yearning to restart his life.",
//                "unhee's a pushover and has somehow managed to lend out all his money. He finally finds a great part-time gig, but it's only for women! In serious need of the cash, he dresses up as a girl only to discover the job he's just applied for is a clinical trial on a libido pill for women. Not only is he locked in for a week with five other (ahem, horny) women, they're all women from his high school! How long before everyone discovers he's not a she?",
//                "The Seven Deadly Sins Manga (Japanese: Nanatsu no Taizai) is a Japanese manga series written and illustrated by Nakaba Suzuki. It's been serialized in Kodansha's Weekly Shonen Magazine using the chapters gathered into eighteen tankobon volumes by December 17, 2015, since October 2012. The manga comes with a setting much like the European Middle Ages, with its titular number of knights symbolizing the seven deadly sins.",
//                "On May 9th, 1999, Raku Ichijou was born into the Yakuza heir. On June 7th, 1999, Chitoge Kirisaki was born into the Bee Hive Gangsters. Even though Raku may be the next heir to a Yakuza group, he's actually a normal highschool student who wishes for peace and quiet. However, when he meets violent transfer student Chitoge Kirisaki, his life takes a sharp turn for the worse!",
//                "In a time and place where one’s soul can be morphed into a weapon, there are modern-day magicians called Mage-Knights. Although Kurogane Ikki is a student at an institution that trains Mage-Knights, he has no particular talent in magic and is labeled the 'Failure Knight' or 'Worst One.' Getting way less than average marks in the scorings, he was forced to repeat a year. But with the arrival of a new head of the institution, a new rule was created: knights whose abilities are compatible, as decided by the board, must share rooms and attend practice and training together throughout their school years to bring up their abilities to the max. It is a rule to implement the absolute verdict of ability. Ikki’s roommate, Stella Vermillion, turns out to be a princess of another foreign country. Stella is a Rank A knight: the type of genius in magic who only appears once a decade. When Ikki walked in on her while she was changing her clothes, it caused a huge misunderstanding (since neither yet knew of the new rule) which eventually ended up in a duel between the two of them. The punishment (or as some might see it, the prize) of the losing side is Eternal Submission to the winning side. Forced to live the same room and practice magic together throughout all their school years, how will Stella and Ikki’s relationship evolve?",
//                "Yoshida was swiftly rejected by his crush of 5 years. On his way home after drinking his sorrows away, he saw a high school girl sitting on the street.",
//                "A man is stabbed by a robber on the run after pushing his coworker and his coworker's new fiance out of the way. As he lays dying, bleeding on the ground, he hears a voice. This voice is strange and interprets his dying regret of being a virgin by gives him the [Great Sage] unique skill! Is he being made fun of !?!",
//                "10 years ago, after “the Gate” that connected the real world with the monster world opened, some of the ordinary, everyday people received the power to hunt monsters within the Gate. They are known as 'Hunters'. However, not all Hunters are powerful. My name is Sung Jin-Woo, an E-rank Hunter. I'm someone who has to risk his life in the lowliest of dungeons, the \"World's Weakest\". Having no skills whatsoever to display, I barely earned the required money by fighting in low-leveled dungeons… at least until I found a hidden dungeon with the hardest difficulty within the D-rank dungeons! In the end, as I was accepting death, I suddenly received a strange power, a quest log that only I could see, a secret to leveling up that only I know about! If I trained in accordance with my quests and hunted monsters, my level would rise. Changing from the weakest Hunter to the strongest S-rank Hunter!",
//                "Tomoki Sakurai is a perverted teen boy whose slogan is 'Peace and quiet are the best,' and frequently has goals of meeting an angel. He finds it almost impossible to reside in relaxation when he must put up with Sohara Mitsuki, his next door neighbor with a killer karate chop; Eishiro Sugata, a strange pseudo-scientist bent on finding the 'New World'; and Mikako Satsukitane, their college sadistic student council president. One night, while he was watching a peculiar anomaly in the heavens, a UMA (Unidentified Mysterious Animal) crash lands near-by. Tomoki finds that what fell in the skies is a winged feminine humanoid named Ikaros from an unfamiliar world of Synapse, who shortly declares herself to be the servant of Tomoki. From then on, more creatures identified as 'Angeloids' arrive; with this, he loses his solace, but in the exact same time discovers nice matters the Angeloids bring him, and battle the forces that drop up on Earth. ",
//                "This is the story about two kids (Rokuro and Benio) and their lives as onmyouji warriors...",
//                "Naofumi Iwatani, an uncharismatic Otaku who spends his days on games and manga, suddenly finds himself summoned to a parallel universe! He discovers he is one of four heroes equipped with legendary weapons and tasked with saving the world from its prophesied destruction. As the Shield Hero, the weakest of the heroes, all is not as it seems. Naofumi is soon alone, penniless, and betrayed. With no one to turn to, and nowhere to run, he is left with only his shield. Now, Naofumi must rise to become the legendary Shield Hero and save the world!",
//                "Hata's new manga is titled Tonikaku Kawaii (Generally Cute), and it will launch in the magazine's next issue on February 14. The English subtitle is \"Fly Me to The Moon,\" and the visual shows a 'mysterious girl.' The magazine's tagline says the series is \"packed with love and dreams. [ANN]\"",
//                "The storyline centers around a boy called Twenty-Fifth Baam, that has spent his life trapped beneath a tower that was mysterious. Chasing after his only friend, as he attempts to locate his company, he manages to open a door to the Tower, and must face challenges at every floor of the cryptic tower.",
//                "Everyday is a normal day in the small town where Kasuga Arata lives. However, everything changed on the day of the Black Sun, and following it, a magician appears before him. The Black Sun caused the Breakdown Phenomenon which destroyed the town where he lives. Because of this, his normal life was artificially reconstructed by a Grimoire that his childhood friend had left. Just what is the purpose of the magician coming to the town? What will he do with the Grimoire's keepsake?",
//                "Annoying! Cute! But Annoying!",
//                "The daily life of a quiet college student who just wants to left alone, being teased by his cute, stacked underclassman.",
//                "From AnimeNewsNetwork: Watari's (author) romantic comedy revolves around an antisocial high school student named Hachiman Hikigaya with a distorted view on life and no friends or girlfriend. When he see his classmates talking excitedly about living their adolescent lives, he mutters, \"They're a bunch of liars.\" When he is asked about his future dreams, he responds, \"Not working.\" A teacher gets Hachiman to join the volunteer \"service club,\" which happens to have the school's prettiest girl, Yukino Yukinoshita."
//
//};
//
//
//
//
//
//
//
//        String thumbmailarr[]={"https://res.cloudinary.com/mangacrawlers/image/upload/v1619091138/manga_thumbnails/Absolute_duo.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091137/manga_thumbnails/Akagami_Shirayukihime.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091136/manga_thumbnails/Akatsuki_no_yona.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619004012/manga_thumbnails/read_attack_on_tian_manga.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619004068/manga_thumbnails/read_black_clover_manga.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091144/manga_thumbnails/Bokutachi_benkyou.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619090789/manga_thumbnails/Dr.Stone.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1620225450/manga_thumbnails/Dungeon.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091150/manga_thumbnails/Ero_manga_sensei.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619013761/manga_thumbnails/read_Fairy_tail_manga.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091142/manga_thumbnails/Fate_stay_night.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091139/manga_thumbnails/Gakuen_toshi.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091149/manga_thumbnails/god_of_highschool.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091148/manga_thumbnails/Gotbun.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091145/manga_thumbnails/Gun_x_clover.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091145/manga_thumbnails/Haikyuu.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091143/manga_thumbnails/Hentai_ouji.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619091141/manga_thumbnails/high_school_dxd.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097763/manga_thumbnails/Isekai_wa_smartphone.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097760/manga_thumbnails/Jaku_chara_tomozaki-kun.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097757/manga_thumbnails/Jigokuraku.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097749/manga_thumbnails/Jujutsu_kaisen.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097745/manga_thumbnails/Kaguyasama.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097740/manga_thumbnails/Kanojo_Okarimashu.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097769/manga_thumbnails/Kimetsu_no_Yaiba.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097768/manga_thumbnails/Kiss_x_sis.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097767/manga_thumbnails/Koi_to_yobu.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097765/manga_thumbnails/Kuzu_no_honkai.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097762/manga_thumbnails/Monster_musume.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097758/manga_thumbnails/Mushoku_tensei.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097753/manga_thumbnails/My_first_love_and_I.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097748/manga_thumbnails/Natsu_no_taizai.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097744/manga_thumbnails/Nisekoi.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097737/manga_thumbnails/Rakudai_kishi.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097755/manga_thumbnails/Hige_wo_soru.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097768/manga_thumbnails/Slime.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097751/manga_thumbnails/Solo_leveling.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1620228235/manga_thumbnails/Sora.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1620228312/manga_thumbnails/sousei.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097764/manga_thumbnails/Tate_no_yuusha.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097761/manga_thumbnails/Tonikaku_kawaii.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097758/manga_thumbnails/Tower_of_God.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097752/manga_thumbnails/Trinity_seven.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097747/manga_thumbnails/Uzaki-chan.jpg",
//                            "https://res.cloudinary.com/mangacrawlers/image/upload/v1619097743/manga_thumbnails/yahari.jpg"};
//
//
//
//
//        int date_publicationarr[]={2013,2010,2012,2013,2014,2015,
//                2017,2017,2013,2014,
//                2006,2004,2012,2013,2015,
//                2016,2017,2018,2006,2018,
//                2017,2015,2018,2019,
//                2015,2016,2017,2010,
//                2013,2017,2018,
//                2015,2016,2011,2012,
//                2008,2015,
//                2018,2017,2019,
//                2016,2017,2020,2015,
//                2014};
//
//
//
//        for (int i = 0; i < manganamearr.length; i++) {
//
//
//
//
//            Manga newmanga = new Manga();
//
//            newmanga.setManga_name(manganamearr[i]);
//
//
//            newmanga.setStatus(statusarr[i]);
//
//
//            newmanga.setDescription(descriparr[i]);
//
//
//            newmanga.setStars(4);
//
//
//            newmanga.setViews(100);
//
//
//
//            newmanga.setThumbnail(thumbmailarr[i]);
//
//
//
//            newmanga.setDate_publications(date_publicationarr[i]);
//
//            newmanga.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//
//
//
//            mangaRepository.save(newmanga);
//
//
//        }
//        System.out.println("----------------------------");
//
//
//
//

            return null;
        }


}
