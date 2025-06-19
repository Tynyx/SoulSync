// This class is implemented to verify and protect users who have or
// want to have an account on SoulSync by confirming login
// and password credentials and also storing that information to
// ensure its only that user logging in to his account


package backend;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManager {

    //Attributes we only need one to list all the users
    private List<User> userList;

    //Constructor


    public AuthenticationManager(List<User> userList) {
        this.userList = userList;
    }

    //method registerUser is log the qualified user into the server or storage
    public boolean registerUser(String userName, String password) {

        try {
            if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                return false;
            }
            for (User user : userList) {
                if (user.getUserName().equals(userName)) {
                    return false;
                }
            }
            int newID = userList.size() + 1;
            userList.add(new User(newID, userName, password, new ArrayList<>()));
            return true;
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String userName, String password) {
        try {
            if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                return null;
            }

            for (User user : userList) {
                if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error logging in user: " + e.getMessage());
            return null;
        }
    }


    public List<User> getUserList () {
        return userList;
    }

}
