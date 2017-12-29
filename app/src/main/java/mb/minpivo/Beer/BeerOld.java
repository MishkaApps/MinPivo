package mb.minpivo.Beer;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

import mb.minpivo.Authenticator;

/**
 * Created by mbolg on 02.09.2017.
 */

public class BeerOld implements Serializable{
    private String authorEmail;
    private String authorId;
    private String authorName;
    private String name;
    private HashMap<String, Object> users;
    private int rating;
    private float averUsersRating;
    private int currentUserRating;
    private boolean isRatedByCurrentUser;
    private boolean isRatedByMe, isRatedBySomeoneElse;


    public BeerOld(String name) throws Authenticator.UserNotAuthedException {
        this.name = name.trim();
        averUsersRating = 0;
        this.authorEmail = Authenticator.getCurrentUserEmail();
        this.authorId = Authenticator.getCurrentUserId();
        isRatedByMe = false;
        isRatedBySomeoneElse = false;
    }

    public BeerOld() {
    }

    public HashMap<String, Object> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, Object> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Exclude
    public int getAverUsersRating() {
        return Math.round(averUsersRating);
    }

    public void setAverUsersRating(float averUsersRating) {
        this.averUsersRating = averUsersRating;
    }

    public String toString() {
        return name + ": " + rating;
    }

    @Exclude
    public int getCurrentUserRating() {
        return currentUserRating;
    }

    public void setCurrentUserRating(int currentUserRating) {
        this.currentUserRating = currentUserRating;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    @Exclude
    public boolean isRatedByCurrentUser() {
        return isRatedByCurrentUser;
    }

    @Exclude
    public void setRatedByCurrentUser(boolean ratedByCurrentUser) {
        isRatedByCurrentUser = ratedByCurrentUser;
    }

    public boolean isRatedByMe() {
        return isRatedByMe;
    }

    public void setRatedByMe(boolean ratedByMe) {
        isRatedByMe = ratedByMe;
    }

    @Exclude
    public boolean isRatedBySomeoneElse() {
        return isRatedBySomeoneElse;
    }

    @Exclude
    public void setRatedBySomeoneElse(boolean ratedBySomeoneElse) {
        isRatedBySomeoneElse = ratedBySomeoneElse;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
