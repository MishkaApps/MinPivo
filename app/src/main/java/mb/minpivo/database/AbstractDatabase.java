package mb.minpivo.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class AbstractDatabase {
    protected HashMap<String, DatabaseItem> databaseItems;
    private List<DatasetChangedListener> datasetChangedListeners;

    protected AbstractDatabase() {
        databaseItems = new HashMap<>();
        datasetChangedListeners = new ArrayList<>();
        listenFirebase();
    }


    public void listenFirebase() {
        getDataReference().addChildEventListener(new ChildEventListener() {
            private void addItem(DataSnapshot dataSnapshot) {
                databaseItems.put(dataSnapshot.getKey(), dataSnapshot.getValue(getItemClass()));
                notifyDatasetChanged();
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    addItem(dataSnapshot);
                } catch (DatabaseException ex) {
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    addItem(dataSnapshot);
                } catch (DatabaseException ex) {
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                databaseItems.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void notifyDatasetChanged() {
        for(DatasetChangedListener datasetChangedListener: datasetChangedListeners)
            datasetChangedListener.notifyDataChanged();
    }

    protected abstract DatabaseReference getDataReference();

    protected abstract Class<? extends DatabaseItem> getItemClass();

    public void addDatasetChangedListener(DatasetChangedListener datasetChangedListener) {
        datasetChangedListeners.add(datasetChangedListener);
        notifyDatasetChanged();
    }


    public ArrayList<? extends DatabaseItem> getItemsAsList() {
        return new ArrayList(databaseItems.values());
    }
}
