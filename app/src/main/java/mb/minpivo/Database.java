package mb.minpivo;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mb.minpivo.database.beers.Beer;


/**
 * Created by mbolg on 18.10.2017.
 */

public class Database {
    public static void tryAddBeer(final Beer beer) {
        getBeersRef().addListenerForSingleValueEvent(new ValueEventListener() {
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
        DatabaseReference beersReference = getBeersRef();
        beersReference.child(beer.getName()).setValue(beer);
    }

    public static void setRatingToBeer(mb.minpivo.database.beers.Beer beer, int rating) {
        if (Authenticator.isSomeoneAuthed())
            setCurrentUserRatingToBeer(beer, rating);
    }

    public static void setCurrentUserRatingToBeer(mb.minpivo.database.beers.Beer beer, int userRating) {
        FirebaseUser currentUser;
        try {
            currentUser = Authenticator.getCurrentUser();
        } catch (Authenticator.UserNotAuthedException e) {
            return;
        }
        DatabaseReference beersReference = getBeersRef();
        beersReference.child(beer.getName()).child("users").child(currentUser.getUid()).setValue(userRating);
    }

    public static DatabaseReference getBeersRef() {
        return FirebaseDatabase.getInstance().getReference().child(Config.BEERS_REF);
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
                        .child("email")
                        .setValue(Authenticator.getCurrentUserEmail());
            } catch (Authenticator.UserNotAuthedException userNotAuthed) {
            }
    }

    public static void addSiryaShitEvent() {
        FirebaseDatabase.getInstance().getReference().child("sirya_shits").push().setValue(true);
    }

    public static void setWorkAvailabilityListener(final EnterCapabilityListener listener) {
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

    public static void setCurrentUserName(String name) throws InvalidUserNameException {
        if (!validateUserName(name))
            throw new InvalidUserNameException();

        setNameIfUnique(name);
    }

    private static boolean validateUserName(String name) {
        if (name.trim().length() < 3)
            return false;

        return true;
    }

    private static void setNameIfUnique(final String name) {
        getEmailsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName;
                try {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        userName = (String) user.child("name").getValue();
                        if (userName != null)
                            if (name.toLowerCase().equals(userName.toLowerCase()))
                                if (user.getKey().equals(Authenticator.getCurrentUserId()))
                                    break;
                                else
                                    return;

                    }
                } catch (Authenticator.UserNotAuthedException userNotAuthed) {
                    userNotAuthed.printStackTrace();
                    return;
                }
                try {
                    DatabaseReference reference = getEmailsRef();
                    reference.child(Authenticator.getCurrentUserId()).child("name").setValue(name);
                } catch (Authenticator.UserNotAuthedException userNotAuthed) {
                    userNotAuthed.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void updateAuthorNameForBeers(final String name) {
        getBeersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot beer : dataSnapshot.getChildren())
                        if ((beer.child("authorId").getValue().equals(Authenticator.getCurrentUserId())))
                            beer.child("authorName").getRef().setValue(name);
                } catch (Authenticator.UserNotAuthedException userNotAuthed) {
                    userNotAuthed.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static DatabaseReference getEmailsRef() {
        return FirebaseDatabase.getInstance().getReference().child("emails");
    }


    private static DatabaseReference getBeerRef(String name) {
        return FirebaseDatabase.getInstance().getReference("beers/" + name);
    }

    public static void setListeners() {

    }
}
