package mb.minpivo.database.beers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mb.minpivo.Config;
import mb.minpivo.L;
import mb.minpivo.database.AbstractDatabase;
import mb.minpivo.database.DatabaseItem;
import mb.minpivo.database.DatasetChangedListener;

/**
 * Created by mbolg on 22.12.2017.
 */

public class BeersDatabase extends AbstractDatabase {
    private static BeersDatabase instance;

    public static BeersDatabase getInstance() {
        if (instance == null)
            instance = new BeersDatabase();

        return instance;
    }

    @Override
    protected DatabaseReference getDataReference() {
        return FirebaseDatabase.getInstance().getReference(Config.BEERS_REF);
    }

    @Override
    protected Class<? extends DatabaseItem> getItemClass() {
        return Beer.class;
    }
}
