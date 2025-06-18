// LaTroy Richardson CEN - 3024c-31774 June 16th 2025
// Software Development 1
// Anime class is the logic behind holding all the code for the
// anime that will be added, deleted, edited and displayed


package backend;

import java.util.ArrayList;
import java.util.List;

public class Anime {
    // Class attribute
    //

    // id will be the primary key and identifier
    private int id;

    // title is as listed the title of the anime
    private String title;

    // eps refers to the episodes watched to track the progress
    private int epsWatched;

    //help notate if one have finished the anime
    private int totalEps;

    private String status;

    private double rating;
    // rate the anime 0.0 to 10.0


    // Constructors

    public Anime(int id, String title, int epsWatched, int totalEps, String status, double rating) {
        this.id = id;
        this.title = title;
        this.epsWatched = epsWatched;
        this.totalEps = totalEps;
        this.status = status;
        this.rating = rating;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpsWatched() {
        return epsWatched;
    }

    public void setEpsWatched(int epsWatched) {
        this.epsWatched = epsWatched;
    }

    public int getTotalEps() {
        return totalEps;
    }

    public void setTotalEps(int totalEps) {
        this.totalEps = totalEps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }



    public static Anime fromCSV(String line){
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String title = parts[1];
        int epsWatched = Integer.parseInt(parts[2]);
        int totalEps = Integer.parseInt(parts[3]);
        String status = parts[4];
        double rating = Double.parseDouble(parts[5]);

        return new Anime(id, title, epsWatched, totalEps, status, rating);
    }

    List<Anime> animeList = new ArrayList<>();
    String lineFromFile = "1,Naruto,100,200,Watching,8.5";

    Anime anime = Anime.fromCSV(lineFromFile);

    // to string

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", epsWatched=" + epsWatched +
                ", totalEps=" + totalEps +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }


}
