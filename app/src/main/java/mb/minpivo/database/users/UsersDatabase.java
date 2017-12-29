package mb.minpivo.database.users;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mb.minpivo.L;
import mb.minpivo.database.AbstractDatabase;
import mb.minpivo.database.DatabaseItem;
/**
 * Created by mbolg on 22.12.2017.
 */

public class UsersDatabase extends AbstractDatabase {
    private static UsersDatabase instance;

    public static UsersDatabase getInstance() {
        if (instance == null)
            instance = new UsersDatabase();

        return instance;
    }

    @Override
    protected DatabaseReference getDataReference() {
        return FirebaseDatabase.getInstance().getReference("emails");
    }

    @Override
    protected Class<? extends DatabaseItem> getItemClass() {
        return User.class;
    }

    public String getUserNameById(String userId) {
        if(databaseItems.get(userId) == null)
            return null;
        return ((User)databaseItems.get(userId)).getName();
    }
}
