// LaTroy Richardson CEN - 3024c-31774 June 16th 2025
// Software Development 1
// Anime class is the logic behind holding all the code for the
// anime that will be added, deleted, edited and displayed


package backend;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Anime {
    // Class attribute
    //

    // id will be the primary key and identifier
    private int id;

    // title is as listed the title of the anime
    private String title;

    //genre is the broad description of the anime
    private String genre;

    // eps refers to the episodes watched to track the progress
    private int epsWatched;

    //help notate if one have finished the anime
    private int totalEps;

    private String status;

    private double rating;
    // rate the anime 0.0 to 10.0


    // Constructors

    public Anime() {}

    public Anime(int id, String title, String genre, int epsWatched, int totalEps, String status, double rating) {
        setId(id);
        setTitle(title);
        setGenre(genre);
        setEpsWatched(epsWatched);
        setTotalEps(totalEps);
        setStatus(status);
        setRating(rating);
    }

    // Getters and Setters

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        try {
            if (genre != null && genre.equals("")) {
                throw new IllegalArgumentException(" Genre cannot be empty");

            }
            this.genre = genre;
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" Genre cannot be numbers");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        try{
            if (id < 0 || id > 100) {
                throw new IllegalArgumentException(" Id must be between 0 and 100");
            }
            this.id = id;
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" Id must be a integer between 0 and 100");
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException(" Title cannot be empty");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" Title must only contain alphabetical characters");
        }

        this.title = title;
    }

    public int getEpsWatched() {
        return epsWatched;
    }

    public void setEpsWatched(int epsWatched) {

        try {
            if (epsWatched < 0) {
                throw new IllegalArgumentException(" EpsWatched cannot be negative");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" EpsWatched must only contain numeric characters");
        }
        this.epsWatched = epsWatched;
    }

    public int getTotalEps() {
        return totalEps;
    }

    public void setTotalEps(int totalEps) {

        try {
            if (totalEps < 0) {
                throw new IllegalArgumentException(" TotalEps cannot be negative");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" TotalEps must only contain numeric characters");
        }
        this.totalEps = totalEps;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        try {
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException(" Status cannot be empty");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" Status must only contain alphabetical characters");
        }
        this.status = status;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {

        try {
            if (rating < 0.0 || rating > 10.0) {
                throw new IllegalArgumentException(" Rating must be between 0 and 10");
            }
        } catch (InputMismatchException e) {
            throw new IllegalArgumentException(" Rating must only contain numeric characters");
        }
        this.rating = rating;
    }




    public static Anime fromTxt(String line){
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String title = parts[1];
        String genre = parts[2];
        int epsWatched = Integer.parseInt(parts[3]);
        int totalEps = Integer.parseInt(parts[4]);
        String status = parts[5];
        double rating = Double.parseDouble(parts[6]);

        return new Anime(id, title, genre, epsWatched, totalEps, status, rating);
    }



    // to string

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                "| ~~\ntitle -'" + title + '\'' +
                "| ~~\ngenre -'" + genre + '\'' +
                "| ~~\nepsWatched -" + epsWatched +
                "| ~~\ntotalEps -" + totalEps +
                "| ~~\nstatus -'" + status + '\'' +
                "| ~~\nrating -" + rating +
                '|';
    }


}
