package mb.minpivo.beerlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import mb.minpivo.AddBeerPanelFragment;
import mb.minpivo.Authenticator;
import mb.minpivo.BeerListAdapter;
import mb.minpivo.Config;
import mb.minpivo.Database;
import mb.minpivo.EnterCapabilityListener;
import mb.minpivo.InvalidUserNameException;
import mb.minpivo.R;
import mb.minpivo.WorkAvailabilityProvider;

public class BeerListActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, EnterCapabilityListener, WorkAvailabilityProvider {
    private Authenticator authenticator;
    private AddBeerPanelFragment addBeerPanel;
    private boolean isWorkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        initUI();
        authenticator = new Authenticator(this);
        isWorkAvailable = false;
        Database.setEnterAvailableValue(this);
//        Database.setUserDataChangeListener();

        addBeerPanel = (AddBeerPanelFragment) getSupportFragmentManager().findFragmentById(R.id.add_beer_panel_fragment);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                invalidateOptionsMenu();
            }
        });

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Database.addCurrentUserEmailIfAuthed();
                subscribeToTopicsIfAuthed();
            }
        });
    }

    private void subscribeToTopicsIfAuthed() {
        FirebaseMessaging.getInstance().subscribeToTopic(Config.BEERS_NEWS_TOPIC_NAME);
        FirebaseMessaging.getInstance().subscribeToTopic(Config.SIRYA_SHIT_TOPIC_NAME);
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fillBeerList();
    }

    private void fillBeerList() {
        RecyclerView beerList = findViewById(R.id.beer_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        beerList.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new BeerListAdapter();
        beerList.setAdapter(adapter);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(BeerListActivity.this, "Some troubles with connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.findItem(R.id.item_sirya_shit).setVisible(false);
        menu.findItem(R.id.add_beer).setVisible(false);

        if (Authenticator.isSiryaAuthed())
            menu.findItem(R.id.item_sirya_shit).setVisible(true);

        if (Authenticator.isSomeoneAuthed()) {
            if (!addBeerPanel.isVisible())
                menu.findItem(R.id.add_beer).setVisible(true);
        }

        if (Authenticator.isSomeoneAuthed()) {
            menu.findItem(R.id.sign_in).setVisible(false);
            menu.findItem(R.id.sign_out).setVisible(true);
            menu.findItem(R.id.set_name).setVisible(true);
        } else {
            menu.findItem(R.id.sign_in).setVisible(true);
            menu.findItem(R.id.sign_out).setVisible(false);
            menu.findItem(R.id.set_name).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sirya_shit:
                showSuryaNewShitDialogue();
                break;
            case R.id.sign_out:
                Authenticator.signOut();
                break;
            case R.id.add_beer:
                addBeerPanel.show();
                invalidateOptionsMenu();
                break;
            case R.id.sign_in:
                authenticator.tryAuthenticate();
                item.setVisible(false);
                break;
            case R.id.set_name:
                showSetNameDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.set_name_dialog, null);
        final EditText etName = dialogView.findViewById(R.id.et_set_name);
        builder.setView(dialogView);
        builder.setPositiveButton("Рванули", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Database.setCurrentUserName(etName.getText().toString());
                } catch (InvalidUserNameException e) {
                    Toast.makeText(BeerListActivity.this, "Хуевое имя. Давай другое.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (authenticator.checkIfAuthSuccessOnAuthActivityResult(requestCode, data)) {
            invalidateOptionsMenu();
        }
    }

    private void showSuryaNewShitDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Желаете рассказать друзьям, о том что вы посрали?").setPositiveButton("Рванули", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database.addSiryaShitEvent();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public void setCapability(boolean b) {
        isWorkAvailable = b;
    }

    @Override
    public boolean isWorkAvailable() {
        return isWorkAvailable;

    }
}

















