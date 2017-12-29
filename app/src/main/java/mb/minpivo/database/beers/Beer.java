package mb.minpivo.database.beers;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

import mb.minpivo.Authenticator;
import mb.minpivo.Config;
import mb.minpivo.L;
import mb.minpivo.database.DatabaseItem;

/**
 * Created by mbolg on 22.12.2017.
 */

public class Beer implements DatabaseItem, Serializable {
    String authorEmail;
    String authorId;
    String authorName;
    String name;
    int rating;
    HashMap<String, Integer> users;

    public Beer() {
        users = new HashMap<>();
    }

    public Beer(String name) throws Authenticator.UserNotAuthedException {
        this.name = name.trim();
        this.authorEmail = Authenticator.getCurrentUserEmail();
        this.authorId = Authenticator.getCurrentUserId();
        users = new HashMap<>();
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(HashMap<String, Integer> users) {
        if (users != null)
            this.users = users;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getName() {
        return name;
    }

    @Exclude
    public int getMyRating() {
        if (users.containsKey(Config.MY_ID))
            return users.get(Config.MY_ID);
        else return 0;
    }

    public HashMap<String, Integer> getUsers() {
        return users;
    }

    @Exclude
    public boolean isRatedByCurrentUser() {
        String currentUserId = null;
        try {
            currentUserId = Authenticator.getCurrentUserId();
        } catch (Authenticator.UserNotAuthedException userNotAuthed) {
            return false;
        }
        for (String userId : users.keySet())
            if (userId.equals(currentUserId))
                return true;

        return false;
    }

    @Exclude
    public boolean isRatedByMe() {
        for (String userId : users.keySet())
            if (userId.equals(Config.MY_ID))
                return true;

        return false;
    }

    @Exclude
    public boolean isRatedBySomeoneElse() {
        boolean isRatedOnlyByMe = users.size() == 1 && users.containsKey(Config.MY_ID);
        if (isRatedOnlyByMe)
            return false;

        boolean isRatedBySomeone = !users.isEmpty();
        if (isRatedBySomeone)
            return true;
        else return false;
    }

    @Exclude
    public int getAverUsersRating() {
        int summaryRating = 0;
        int usersCount = 0;
        for (String userId : users.keySet()) {
            if (userId.equals(Config.MY_ID))
                continue;
            ++usersCount;
            summaryRating += users.get(userId);
        }

        if (usersCount != 0)
            return summaryRating / usersCount;
        else return 0;
    }

    @Exclude
    public int getCurrentUserRating() {
        try {
            if (users.containsKey(Authenticator.getCurrentUserId()))
                return users.get(Authenticator.getCurrentUserId());
            else return 0;
        } catch (Authenticator.UserNotAuthedException e) {
            return 0;
        }
    }
}
