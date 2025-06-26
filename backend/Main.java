
// LaTroy Richardson
// CEN -3024c-31774
//Software Development 1
// Main class is the class that initialize all the code in SoulSync
// This is where the user or coder should go to run the whole applications and complete
// the complete or test the features the creator implemented



package backend;

import java.io.IOException;
import java.util.*;

public class Main {

    private static String prompt(String message, Scanner sc) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private static int promptInt(String message, Scanner sc) {
       while (true) {
           System.out.print(message);
           try {
               return Integer.parseInt(sc.nextLine().trim());
           } catch (NumberFormatException e) {
               System.out.println("Please enter a whole number");
           }
       }

    }

    private static double promptDouble(String message, Scanner sc) {
        while (true) {
            System.out.print(message);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number (e.g. 7.5).");
            }
        }
    }

    // attributes calling on some of the list and methods we created in the classes
    // we have an hardcoded login becuase we cant store users at the moment so we using this as the
    // test case and when we initialize the db it will be with an login and register

    public static void main(String[] args) {
        // 1) Setup users & managers
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "admin", "1234", new ArrayList<>()));

        AuthenticationManager authManager = new AuthenticationManager(userList);
        // We'll re-use the same manager instance per user later:
        WatchListManager watchManager = new WatchListManager(new ArrayList<>());

        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        // 2) Login loop
        while (loggedInUser == null) {
            System.out.println("\n==== SoulSync ====");
            String uname = prompt("Enter Username", scanner);
            String pass = prompt("Enter Password", scanner);

            loggedInUser = authManager.loginUser(uname, pass);
            if (loggedInUser == null) {
                System.out.println("❌ Login failed. Try again.");
            }
        }

        // 3) Main menu
        boolean exit = false;
        while (!exit) {
            System.out.println("\nWelcome, "  + loggedInUser.getUserName() + " SoulSync is at your Service !");
            System.out.println("1. Add Anime");
            System.out.println("2. Update Anime");
            System.out.println("3. Delete Anime");
            System.out.println("4. List Anime");
            System.out.println("5. Upload Anime TXT");
            System.out.println("6. Get Recommended Anime");
            System.out.println("7. Exit");


            String choice = prompt("Choose an option", scanner);
            switch (choice) {
                case "1":
                    // ——— Add Anime with full validation ———
                    while (true) {
                        try {
                            String title = prompt("Anime Title: ", scanner);

                            String genre = prompt("Genre: ", scanner);

                            int total = promptInt("Total Episodes: ", scanner);

                            int watched = promptInt("Watched Episodes: ", scanner);

                            String status = prompt("Status: ", scanner);

                            double rating = promptDouble("Rating: ", scanner);

                            // Create & add
                            Anime prototype = new Anime(0, title, genre, watched, total, status, rating);
                            Anime added = watchManager.addAnime(prototype);
                            // also track per-user
                            loggedInUser.getAnimeList().add(added);

                            System.out.println("✔ Anime added: " + added.getTitle());
                            break;

                        } catch (NumberFormatException nfe) {
                            System.out.println("❌ Please use valid numbers for episodes & rating.");
                        } catch (IllegalArgumentException iae) {
                            System.out.println("❌ " + iae.getMessage());
                        }
                    }
                    break;


                case "2":
                    // —— UPDATE by Title ——
                    System.out.print("Enter exact Anime Title to update: ");



                    String toEdit = prompt("Soul to Sync: ", scanner);
                    String newGenre = prompt("New Genre:", scanner);
                    int newWatchedEpisodes = promptInt("New Episodes Watched: ", scanner);
                    int newTotalEpisodes =  promptInt("New Total Episodes: ", scanner);
                    String newStatus = prompt("New Status: ", scanner);
                    double newRating = promptDouble("New Rating: ", scanner);



                    Anime patch = new Anime(
                            0,
                            toEdit,
                            newGenre,
                            newWatchedEpisodes,
                            newTotalEpisodes,
                            newStatus,
                            newRating
                    );

                    boolean ok = watchManager.updateAnime(toEdit, patch );
                    System.out.println(ok
                    ? ":checkmark:"+ toEdit + " was Sync Successfully"
                            : " :error: No anime found titled : " + toEdit + "'");
                    break;
                case "3":
                    // ——— Delete by title ———
                    System.out.print("Enter exact Anime Title to delete: ");
                    String toDelete = prompt("Soul to Erase: ", scanner);
                    boolean removed = loggedInUser.getAnimeList()
                            .removeIf(a -> a.getTitle().equalsIgnoreCase(toDelete));
                    System.out.println(removed
                            ? "✔ Un-Synced “" + toDelete + "”"
                            : "❌ No Souls found with title: " + toDelete);
                    break;

                case "4":
                    // ——— List all anime ———
                    System.out.println("\nYour Watchlist:");
                    if (loggedInUser.getAnimeList().isEmpty()) {
                        System.out.println("  (no anime yet)");
                    } else {
                        for (Anime a : loggedInUser.getAnimeList()) {
                            // Print full info (toString() or custom format)
                            System.out.println("  " + a);
                        }
                    }
                    break;

                case "5":
                    System.out.print("Enter Soul link to your anime list (.txt), (be sure to remove quotes): ");
                    String soulLink = prompt("Soul Link: ", scanner);
                    watchManager.loadAnimeFile(soulLink);

                    loggedInUser.getAnimeList().clear();
                    loggedInUser.getAnimeList().addAll(watchManager.getAnimeList());
                    System.out.println(watchManager.getAnimeList().size() + " Souls Synced!");
                    break;


                case "6":
                    // ——— Recommend top-5  by Score and rating ———
                    List<Anime> recs = watchManager.recommendAnime(
                            watchManager.getAnimeList(),
                            loggedInUser.getAnimeList(),
                            5
                    );
                    System.out.println("\nYour SoulSync Recommendations:");
                    if (recs.isEmpty()) {
                        System.out.println("  (no recommendations available)");
                    } else {
                        for (int i = 0; i < recs.size(); i++) {
                            Anime a = recs.get(i);
                            System.out.printf("  %d) %s (%.1f) — %s%n",
                                    i+1, a.getTitle(), a.getRating(), a.getGenre());
                        }
                    }
                 break;

                case "7":

                        System.out.println("Transcend you Soul!");
                        System.out.println("Good bye!");
                        System.exit(0);

                    break;

            }
        }

        System.out.println("Thanks for using SoulSync!");
        scanner.close();
    }
}