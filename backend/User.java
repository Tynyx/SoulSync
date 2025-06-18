// This class is the user class which is part of giving the user an acutual account
// to store all their data and their credentials to the app


package backend;

import java.util.List;

public class User  {

    // Attributes will be userID which is the primary key,
    //UserName which will be string element and a password as a string element

    private int userID;

    private String userName;

    private String password;

    private List<Anime> animeList;

    //Constructor


    public User(int userID, String userName, String password, List<Anime> animeList) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.animeList = animeList;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setAnimeList(List<Anime> animeList) {
        this.animeList = animeList;
    }

    //method authenticate is used to make sure the password is correct on the account when logging in
    public boolean authenticateUser(String userName, String password) {
        return this.userName.equals(userName) && this.password.equals(password);
    }

    //method getAnimelist to generate the user display and logs and ratings of shows the user want to watch
    public List<Anime> getAnimeList() {
        return animeList;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", animeList=" + animeList + '\''+
                '}';
    }
}
