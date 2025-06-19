
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
            System.out.print("Enter username: ");
            String uname = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String pass = scanner.nextLine().trim();

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
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    // ——— Add Anime with full validation ———
                    while (true) {
                        try {
                            System.out.print("Anime Title: ");
                            String title = scanner.nextLine().trim();

                            System.out.print("Genre: ");
                            String genre = scanner.nextLine().trim();

                            System.out.print("Total Episodes: ");
                            int total = Integer.parseInt(scanner.nextLine().trim());

                            System.out.print("Episodes Watched: ");
                            int watched = Integer.parseInt(scanner.nextLine().trim());

                            System.out.print("Status: ");
                            String status = scanner.nextLine().trim();

                            System.out.print("Rating (0.0–10.0): ");
                            double rating = Double.parseDouble(scanner.nextLine().trim());

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
                    String toEdit = scanner.nextLine().trim();
                    Optional<Anime> opt = loggedInUser.getAnimeList().stream()
                            .filter(a -> a.getTitle().equalsIgnoreCase(toEdit))
                            .findFirst();

                    if (opt.isEmpty()) {
                        System.out.println("❌ No anime found with title: " + toEdit);
                    } else {
                        Anime edit = opt.get();
                        try {
                            System.out.print("New Episodes Watched (current " + edit.getEpsWatched() + "): ");
                            int newWatched = Integer.parseInt(scanner.nextLine().trim());
                            edit.setEpsWatched(newWatched);

                            System.out.print("New Status (current “" + edit.getStatus() + "”): ");
                            edit.setStatus(scanner.nextLine().trim());

                            System.out.print("New Rating (current " + edit.getRating() + "): ");
                            edit.setRating(Double.parseDouble(scanner.nextLine().trim()));

                            System.out.println("✔ Updated “" + edit.getTitle() + "” successfully.");
                        } catch (Exception e) {
                            System.out.println("❌ Update failed: " + e.getMessage());
                        }
                    }
                    break;
                case "3":
                    // ——— Delete by title ———
                    System.out.print("Enter exact Anime Title to delete: ");
                    String toDelete = scanner.nextLine().trim();
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
                    String soulLink = scanner.nextLine();
                    watchManager.loadAnimeFile(soulLink);

                    loggedInUser.getAnimeList().clear();
                    loggedInUser.getAnimeList().addAll(watchManager.getAnimeList());
                    System.out.println(watchManager.getAnimeList().size() + " Souls Synced!");
                    break;


                case "6":
                    // ——— Recommend top-3 by rating ———
                    System.out.println("\nTop 3 Recommendations:");
                    loggedInUser.getAnimeList().stream()
                            .sorted(Comparator.comparingDouble(Anime::getRating).reversed())
                            .limit(3)
                            .forEach(a -> System.out.println("  " + a.getTitle() + " (" + a.getRating() + ")"));
                    break;

                case "7":
                    System.out.println("Do you wish to save your anime list? (Y/N)");
                    String save = scanner.nextLine().trim();

                    if (save.contentEquals("N")) {
                        System.out.println("Thank you for using our anime list!");
                        System.out.println("Good bye!");
                        System.exit(0);
                    } else {
                        System.out.print("Enter .txt filename to Sync your current list (e.g. mylist.txt): ");
                        String outPath = scanner.nextLine().trim();

                        if (!outPath.toLowerCase().endsWith(".txt")) {
                            System.out.println("❌ You must specify a .txt filename.");
                        } else {
                            try {
                                watchManager.saveAnimeFile(outPath, loggedInUser.getAnimeList());
                                System.out.println("✅ Saved " + loggedInUser.getAnimeList().size() + " entries to " + outPath);
                            } catch (IOException ioe) {
                                System.out.println("❌ Sync failed: " + ioe.getMessage());
                            }
                        }
                    }
                    break;

            }
        }

        System.out.println("Thanks for using SoulSync!");
        scanner.close();
    }
}