package mb.minpivo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import mb.minpivo.beerlist.BeerListActivity;

public class SignInActivity extends AppCompatActivity implements TextWatcher {
    private EditText etCode;
    private Button btnSignIn;
    private Authenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Authenticator.isSomeoneAuthed()) {
            goToBeerListActivity();
        } else {

        }
        setContentView(R.layout.activity_sign_in);
        etCode = (EditText) findViewById(R.id.et_invite_code);
        etCode.addTextChangedListener(this);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySignIn();
                etCode.setText("");
            }
        });

        authenticator = new Authenticator(this);
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Database.addCurrentUserEmailIfAuthed();
                subscribeToTopicsIfAuthed();
            }
        });
    }

    private void goToBeerListActivity() {
        Intent intent = new Intent(this, BeerListActivity.class);
        startActivity(intent);
    }


    private void trySignIn() {
        authenticator.tryAuthenticate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (authenticator.checkIfAuthSuccessOnAuthActivityResult(requestCode, data)) {
            goToBeerListActivity();
        }
    }

    private void subscribeToTopicsIfAuthed() {
//        FirebaseMessaging.getInstance().subscribeToTopic(Configurations.BEER_NEWS);
//        FirebaseMessaging.getInstance().subscribeToTopic(Configurations.SIRYA_SHIT_NEWS);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(etCode.getText().toString().equals(""))
            return;
        if(etCode.getText().toString().length() < 1)
            return;

        Database.enableSignInButtonIfInviteCodeMatch(btnSignIn, Integer.parseInt(etCode.getText().toString()));
    }
}
