package mb.minpivo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by mbolg on 12.10.2017.
 */

public class Authenticator implements GoogleApiClient.OnConnectionFailedListener {
    private AppCompatActivity context;
    private GoogleApiClient googleApiClient;

    public Authenticator(AppCompatActivity context) {
        this.context = context;

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(context, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public void tryAuthenticate() {
        openAuthenticationForm();
    }

    private final int SIGN_IN_REQUEST_CODE = 0;

    private void openAuthenticationForm() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        context.startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);

    }

    public boolean checkIfAuthSuccessOnAuthActivityResult(int requestCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (signInResult.isSuccess()) {
                GoogleSignInAccount signInAccount = signInResult.getSignInAccount();
                authWithGoogleAccount(signInAccount);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }


    public static boolean isSomeoneAuthed() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static boolean isIAuthed() {
        try {
            return getCurrentUser().getEmail().equals(Config.MY_EMAIL);
        } catch (UserNotAuthed userNotAuthed) {
            return false;
        }
    }

    public static FirebaseUser getCurrentUser() throws UserNotAuthed {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            throw new UserNotAuthed();
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getCurrentUserEmail() throws UserNotAuthed {
        return getCurrentUser().getEmail();
    }

    public static String getCurrentUserId() throws UserNotAuthed {
        return getCurrentUser().getUid();
    }

    private void authWithGoogleAccount(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential);
    }

    public static boolean isSiryaAuthed() {
        try {
            return getCurrentUserEmail().equals(Config.SIRYA_EMAIL);
        } catch (UserNotAuthed userNotAuthed) {
            return false;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static boolean isSomeoneExceptMeAuthed() {
        return isSomeoneAuthed() && !isIAuthed();
    }

    public static class UserNotAuthed extends Exception {
    }
}
