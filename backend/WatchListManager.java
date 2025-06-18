// LaTroy Richardson CEN - 3024c-31774 June 16th 2025
// Software Development 1
// WatchListManager handles logic for managing the list of Anime objects
// such as adding, updating, removing, and viewing the anime list.




package backend;

import java.util.List;

public class WatchListManager {

    //Attribute would only be animeList to be able to manipulate the data with the CRUD features
    private List<Anime> animeList;

    // Constructor
    public WatchListManager(List<Anime> animeList) {
        this.animeList = animeList;
    }

    //method addAnime is used to state the anime was added or not that is why it is a boolean
    public boolean addAnime(Anime anime) {
        return animeList.add(anime);
    }

    //method updateAnime another boolean to make sure the anime was updated or not
    public boolean updateAnimeById(int id, Anime newAnime) {
        for (int i = 0; i < animeList.size(); i++) {
            if (animeList.get(i).getId() == id) {
                animeList.set(i, newAnime);
                return true;
            }
        }
        return false;
    }


    // now this method is removeAnime if you want to actually remove an anime from your list
    public boolean deleteAnimeById(int id) {
        return animeList.removeIf(anime -> anime.getId() == id);
    }



    //And GetAllAnime is the one that gather all your anime and display it for you
    public List<Anime> getAnimeList() {
        return animeList;
    }
}
