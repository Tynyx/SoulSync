// LaTroy Richardson CEN - 3024c-31774 June 16th 2025
// Software Development 1
// WatchListManager handles logic for managing the list of Anime objects
// such as adding, updating, removing, and viewing the anime list.




package backend;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WatchListManager {

    //Attribute would only be animeList to be able to manipulate the data with the CRUD features
    private List<Anime> animeList;
    private int nextId = 1;

    // Constructor

    // Determine the next available ID by filtering the highest existing ID in the list and adding 1.
    public WatchListManager(List<Anime> animeList) {
        this.animeList =  animeList;

        animeList.stream()
                .mapToInt(Anime::getId)
                .max()
                .ifPresent(max -> nextId = max + 1);
    }

    // method AddAnime is a method to add anime From CLI command add Anime
    public Anime addAnime(Anime toAdd) {
        toAdd.setId(nextId++);
        animeList.add(toAdd);
        return toAdd;
    }

    // Create a map to look up anime by name for quick access and not have to input ID
    public List<Anime> searchAnimeByName(String keyword) {
        return animeList.stream()
                .filter(anime -> anime.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    //method updateAnime another boolean to make sure the anime was updated or not
    public boolean updateAnime(String title, Anime patch) {
        for (Anime a : animeList) {
            if (a.getTitle().toLowerCase().contains(title.toLowerCase())) {
                a.setTitle(patch.getTitle());
                a.setEpsWatched(patch.getEpsWatched());
                a.setTotalEps(patch.getTotalEps());
                a.setStatus(patch.getStatus());
                a.setRating(patch.getRating());
                a.setGenre(patch.getGenre());
                return true;
            }
        }
        return false;
    }
    // now this method is removeAnime if you want to actually remove an anime from your list
    public boolean deleteAnime(String title) {
        return animeList.removeIf(a -> a.getTitle().equalsIgnoreCase(title));
    }



    //And GetAllAnime is the one that gather all your anime and display it for you
    public List<Anime> getAnimeList() {
        return animeList;
    }




    /**
     * Load anime entries from a .txt file (no ID column).
     * Each line must be: title,genre,epsWatched,totalEps,status,rating
     */
    public List<Anime> loadAnimeFile(String soulLink) {
        List<Anime> result = new ArrayList<>();
        File file = new File(soulLink);
        if (!file.exists()) {
            System.out.println("❌ Soul file not found: " + soulLink);
            return Collections.emptyList();
        }

        try (Scanner filescanner = new Scanner(file)) {
            System.out.println(".....Soul Syncing with " + file.getName());
            System.out.println("-------Start of Soul--------");

            while (filescanner.hasNextLine()) {
                String line = filescanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 6) {
                    System.out.println("❌ Skipping malformed line: " + line);
                    continue;
                }

                try {
                    // parse each field
                    String title      = parts[0].trim();
                    String genre      = parts[1].trim();
                    int    epsWatched = Integer.parseInt(parts[2].trim());
                    int    totalEps   = Integer.parseInt(parts[3].trim());
                    String status     = parts[4].trim();
                    double rating     = Double.parseDouble(parts[5].trim());

                    // validate
                    if (title.length() < 3)   throw new IllegalArgumentException("Title too short");
                    if (genre.length() < 3)   throw new IllegalArgumentException("Genre too short");
                    if (epsWatched < 0 || epsWatched > totalEps)
                        throw new IllegalArgumentException("EpsWatched out of bounds");
                    if (status.length() < 3)  throw new IllegalArgumentException("Status too short");
                    if (rating < 0.0 || rating > 10.0)
                        throw new IllegalArgumentException("Rating out of bounds");

                    // create with auto-ID
                    Anime anime = new Anime(nextId++, title, genre, epsWatched, totalEps, status, rating);
                    result.add(anime);
                    animeList.add(anime);
                    System.out.println("✔ Synced: " + title);

                } catch (NumberFormatException nfe) {
                    System.out.println("❌ Number format error on line: " + line);
                } catch (IllegalArgumentException iae) {
                    System.out.println("❌ Validation failed: " + iae.getMessage() + " on line: " + line);
                } catch (Exception e) {
                    System.out.println("❌ Unexpected error parsing line: " + line);
                }
            }

            System.out.println("--------End of Soul---------");
        } catch (FileNotFoundException e) {
            // already checked .exists(), but just in case
            System.out.println("❌ Soul file not found at: " + soulLink);
        }
        this.animeList.addAll(result);
        return result;
    }

    /**
     * Persist the current watch-list back to disk as a .txt (CSV-style).
     * @param filePath  the path (and .txt name) to write to
     * @param animeList the list of Anime to serialize
     */
    public void saveAnimeFile(String filePath, List<Anime> animeList) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Anime a : animeList) {
                // build a CSV line: id,title,genre,epsWatched,totalEps,status,rating
                String line = String.join(",",
                        String.valueOf(a.getId()),
                        a.getTitle(),
                        a.getGenre(),
                        String.valueOf(a.getEpsWatched()),
                        String.valueOf(a.getTotalEps()),
                        a.getStatus(),
                        String.valueOf(a.getRating())
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }







    // Recommendation method is the feature used to tally up your most watched genre
    // and highest rating to generate from the full database and recommendation
    // with those criteria
    public List<Anime> recommendAnime(
            List<Anime> allAnime,
            List<Anime> userWatchList,
            int topN) {
        // Step one Determine if the user has data to generate a recommendation
        if (userWatchList.isEmpty()) {
            return allAnime.stream()
                    .sorted(Comparator.comparing(Anime::getRating).reversed())
                    .limit(topN)
                    .collect(Collectors.toList());
        }

        // Determine the users favorite genre
        Map<String, Long> counts = userWatchList.stream()
                .collect(Collectors.groupingBy(Anime::getGenre, Collectors.counting()));
        String favoriteGenre = Collections
                .max(counts.entrySet(), Map.Entry.comparingByValue())
                .getKey();

        // From the entire catalog, pick those in the same genre, excluding what they already watched
        List<Anime> candidates = allAnime.stream()
                .filter(a -> a.getGenre().equalsIgnoreCase(favoriteGenre))
                .filter(a -> userWatchList.stream()
                        .noneMatch(w -> w.getTitle().equalsIgnoreCase(a.getTitle())))
                .collect(Collectors.toList());

        // Sort those candidates by rating descending
        candidates.sort(Comparator.comparingDouble(Anime::getRating).reversed());

        //Return the top N ( or fewer, if there aren't enough)
        return candidates.stream().limit(topN).collect(Collectors.toList());

    }
}


