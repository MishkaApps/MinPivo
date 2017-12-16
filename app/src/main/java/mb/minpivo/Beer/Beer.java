package mb.minpivo.Beer;

import com.google.firebase.database.Exclude;

/**
 * Created by mbolg on 02.09.2017.
 */

public class Beer {
    private String authorEmail;
    private String name;
    private int rating;
    private float averUsersRating;
    private int currentUserRating;
    private boolean isRatedByCurrentUser;
    private boolean isRatedByMe, isRatedBySomeoneElse;

    public Beer(String name, String authorEmail) {
        this.name = name.trim();
        averUsersRating = 0;
        this.authorEmail = authorEmail;
        isRatedByMe = false;
        isRatedBySomeoneElse = false;
    }

    public Beer() {
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
}
