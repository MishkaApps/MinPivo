package mb.minpivo;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mb.minpivo.Beer.Beer;

/**
 * Created by mbolg on 18.10.2017.
 */

public class Database {
    public static void tryAddBeer(final Beer beer) {
        getBeersReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Beer tempBeer;
                String newBeerNameFormated = beer.getName().toLowerCase();
                String tempBeerNameFormated;
                for (DataSnapshot beerInDB : dataSnapshot.getChildren()) {
                    tempBeer = beerInDB.getValue(Beer.class);
                    tempBeerNameFormated = tempBeer.getName().toLowerCase();
                    if (tempBeerNameFormated.equals(newBeerNameFormated))
                        return;
                }
                addBeer(beer);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void addBeer(Beer beer) {
        DatabaseReference beersReference = getBeersReference();
        beersReference.child(beer.getName()).setValue(beer);
    }

    public static void setRatingToBeer(Beer beer, int rating) {
        if (Authenticator.isIAuthed()) {
            getBeersReference().child(beer.getName()).child("rating").setValue(rating);
            setCurrentUserRatingToBeer(beer, rating);
        } else if (Authenticator.isSomeoneExceptMeAuthed()) {
            setCurrentUserRatingToBeer(beer, rating);
        }
    }

    public static void setCurrentUserRatingToBeer(Beer beer, int userRating) {
        FirebaseUser currentUser;
        try {
            currentUser = Authenticator.getCurrentUser();
        } catch (Authenticator.UserNotAuthed e) {
            return;
        }
        DatabaseReference beersReference = getBeersReference();
        beersReference.child(beer.getName()).child("users").child(currentUser.getUid()).setValue(userRating);
    }

    public static DatabaseReference getBeersReference() {
        return FirebaseDatabase.getInstance().getReference().child(Config.BEERS_REF);
    }

    public static void addInviteNewUser(String email) {
        FirebaseDatabase.getInstance().getReference().child("users_emails").child(email).setValue(true);
    }

    public static DatabaseReference getUsersEmailsRef() {
        return FirebaseDatabase.getInstance().getReference().child("users_emails");
    }

    public static void setInviteCodeToTextView(final TextView textView) {
        getInviteCodeRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void enableSignInButtonIfInviteCodeMatch(final Button btnSignIn, final int userCode) {
        getInviteCodeRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int code = Integer.parseInt(dataSnapshot.getValue().toString());
                if (userCode == code)
                    btnSignIn.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static DatabaseReference getInviteCodeRef() {
        return FirebaseDatabase.getInstance().getReference().child("invite_code");
    }

    public static void addCurrentUserEmailIfAuthed() {
        if (Authenticator.isSomeoneAuthed())
            try {
                FirebaseDatabase.getInstance().getReference().child("emails")
                        .child(Authenticator.getCurrentUserId())
                        .setValue(Authenticator.getCurrentUserEmail());
            } catch (Authenticator.UserNotAuthed userNotAuthed) {
            }
    }

    public static void addSiryaShitEvent() {
        FirebaseDatabase.getInstance().getReference().child("sirya_shits").push().setValue(true);
    }

    public static void setEnterAwailableValue(final EnterCapabilityListener listener) {
        FirebaseDatabase.getInstance().getReference().child("is_enter_available").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.setCapability(dataSnapshot.getValue(Boolean.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
