package Junit.Testing;

import backend.Anime;
import backend.WatchListManager;
import org.junit.jupiter.api.DisplayName;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class CRUDTest {

    // Create an anime object to be tested
    Anime anime;
    WatchListManager manager;
    List<Anime> store;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // Supply the test object with data to be tested i.e. the anime object will be filled in with the data
        store = new ArrayList<>();
        manager = new WatchListManager(store);
     }

    @org.junit.jupiter.api.Test
    @DisplayName(" Add a Anime Test")
    void addAnimeTest() {
        // use the data we just created, to add a anime to the CLI
        Anime bleach = new Anime(0,"Bleach", "Adventure", 320, 320, "Completed", 9.6);


        //Test
        Anime added = manager.addAnime(bleach);


        // Eval
        // the manager should have assigned it and auto-ID (starting at 1)
        assertEquals(1,added.getId());

        // and it should now live in the store
        assertTrue(store.contains(added));
        assertEquals("Bleach", store.get(0).getTitle());

    }

    @org.junit.jupiter.api.Test
    @DisplayName("Adding an Anime with invalid rating throws")
    void addWithBadRating() {

        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Anime bad = new Anime(0, "Test", "X", 1, 10, "Ongoing", 12.0);
                    manager.addAnime(bad);
                },
                "Rating must be between 0.0 and 10.0"
        );
    }

    @org.junit.jupiter.api.Test
    @DisplayName(" Update an Anime Test")
    void updateAnime() {
        // Add test data
        Anime bleach = new Anime(0,"Bleach", "Adventure", 320, 320, "Completed", 9.6);

        manager.addAnime(bleach);
        // try to change or update the test data
        Anime patch = new Anime(
                0,
                "Bleach",
                "Adventure",
                321,
                321,
                "Completed",
                10.0
        );

        //
        boolean ok = manager.updateAnime("Bleach", patch);

        Anime current = store.stream()
                .filter(a -> a.getTitle().equalsIgnoreCase("Bleach"))
                .findFirst().get();
        assertEquals(10.0,current.getRating());
        assertEquals(321, current.getEpsWatched());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Updating nonexistent title returns false")
    void updateNonexistent() {
        // list is empty, so any update should return false
        Anime patch = new Anime(  0, "Ghost", "Horror", 1, 10, "Watching", 5.0 );
        assertFalse(manager.updateAnime("Ghost", patch));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Delete and Anime test")
    void deleteAnime() {

        // Create
        Anime bleach = new Anime(0,"Bleach", "Adventure", 320, 320, "Completed", 9.6);
        manager.addAnime(bleach);

        // Test should delete the anime
        boolean removed = manager.deleteAnime("Bleach");

        // making sure that the data was acutally removed
        assertTrue(removed, "Should return True when 'Bleach' is deleted.");
        assertTrue(store.isEmpty(), "Should return empty list.");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Deleting nonexistent title returns false")
    void deleteNonexistent() {
        assertFalse(manager.deleteAnime("NoSuchShow"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Load and .txt File")
    void loadAnimeFile_Correctly() {

        assertTrue(store.isEmpty());
        String SoulLink = "Junit/Testing/Anime_Starter_List.txt";

        List<Anime> loaded = manager.loadAnimeFile(SoulLink);
        assertEquals(23, loaded.size());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Loading a missing file should not crash")
    void loadMissingFile() {
        // should return empty list (or at least not throw)
        List<Anime> loaded = manager.loadAnimeFile("no-such-file.txt");
        assertTrue(loaded.isEmpty());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Loading malformed lines are skipped")
    void loadMalformedLines() throws IOException {
        // prepare a temp .txt with one good line and one bad line:
        Path tmp = Files.createTempFile("test", ".txt");
        Files.write(tmp, List.of(
                "Valid,Genre,1,1,Done,5.0",
                "broken,line,too,few,cols"
        ));
        List<Anime> list = manager.loadAnimeFile(tmp.toString());
        assertEquals(1, list.size(), "Only the well-formed record should be loaded");
    }


    @org.junit.jupiter.api.Test
    @DisplayName("Get Recommendations For An Anime Test")
    void getRecommendationsForAnimeTest() {
        //  build full catalog
        List<Anime> allAnime = List.of(
                new Anime(0,"Naruto","Action",10,220,"Watching",8.5),
                new Anime(0,"Bleach","Action",50,366,"Completed",9.0),
                new Anime(0,"Your Lie in April","Drama",8,22,"Completed",9.4),
                new Anime(0,"Clannad","Drama",20,23,"Completed",9.2),
                new Anime(0,"One Punch Man","Action",12,12,"Watching",9.1),
                new Anime(0,"Kimi no Na wa","Drama",1,1,"Completed",9.3),
                new Anime(0," Samurai Champloo", "Adventure", 26, 26, "Completed", 9.3),
                new Anime(0, "Astra Lost in Space", "Sci-Fi", 12, 12, "Completed", 6.7),
                new Anime(0, "Steins;Gate", "Sci-Fi", 24, 24, "Completed", 7.6),
                new Anime (0, "Made in Abyss", "Dark Fantasy", 13, 13, "Completed", 6.5),
                new Anime(0, "Erased", "Mystery", 12, 12, "Completed", 7.7),
                new Anime (0, "Pyscho-Pass", "Psychological Thriller", 22, 22, "Completed", 8.4),
                new Anime(0,"Mushoku Tensei: Jobless Reincarnation", "Fantasy", 23, 23, "On-Going", 6.7)
        );

        // give them IDs via your manager
        allAnime.forEach(a -> manager.addAnime(a));

        // --- 2) simulate their watchlist ---
        // e.g. user has watched Naruto and One Punch Man, both Action
        List<Anime> userWatchList = List.of(
                allAnime.get(0),
                allAnime.get(4)
        );

        // --- 3) call recommend ---
        // top 3 recommendations
        List<Anime> recs = manager.recommendAnime(
                manager.getAnimeList(),
                userWatchList,
                3
        );

        // --- 4) verify ---
        // since userWatched is non-empty, we pick favorite genre = "Action"
        // then from allAnime we exclude Naruto & OnePunch, leaving Bleach(9.0)
        // but if fewer than 3 in that genre, we might fall back to top-rated overall
        assertFalse(recs.isEmpty(), "Should get at least one recommendation");
        // first recommendation should be the highest-rated unseen Action:
        assertEquals("Bleach", recs.get(0).getTitle());
        assertEquals(9.0, recs.get(0).getRating());

        // if you requested 3 but only 1 left in Action, recs.size() == 1
        assertEquals(1, recs.size());
        System.out.println(recs.get(0));
    }





}

