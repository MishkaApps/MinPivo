package mb.minpivo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mb.minpivo.Beer.Beer;
import mb.minpivo.Beer.BeerView;

/**
 * Created by mbolg on 02.09.2017.
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.ViewHolder> {
    private ArrayList<Beer> beers;

    public BeerListAdapter() {
        beers = new ArrayList<>();
        DatabaseReference reference = Database.getBeersReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                beers = new ArrayList<>();
                Beer tempBeer;
                int usersRating;
                int usersCount;
                String currentUserId = null;
                try {
                    currentUserId = Authenticator.getCurrentUserId();
                } catch (Authenticator.UserNotAuthed userNotAuthed) {
                    userNotAuthed.printStackTrace();
                }
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    tempBeer = item.getValue(Beer.class);
                    usersRating = 0;
                    usersCount = 0;
                    for (DataSnapshot user : item.child("users").getChildren()) {

                        if (user.getKey().equals(currentUserId))
                            tempBeer.setRatedByCurrentUser(true);

                        if (user.getKey().equals(Config.MY_ID)) {
                            tempBeer.setRatedByMe(true);
                            continue;
                        } else {
                            tempBeer.setRatedBySomeoneElse(true);
                        }
                        ++usersCount;
                        usersRating += (long) user.getValue();
                        if (currentUserId != null)
                            if (currentUserId.equals(user.getKey()))
                                tempBeer.setCurrentUserRating(Math.round((long) user.getValue()));
                    }

                    if (usersCount > 0)
                        tempBeer.setAverUsersRating(usersRating / usersCount);

                    beers.add(tempBeer);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BeerView beerView = (BeerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_list_item, parent, false);
        return new ViewHolder(beerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setBeer(beers.get(position));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private BeerView beerView;

        public ViewHolder(BeerView beerView) {
            super(beerView);
            this.beerView = beerView;
        }

        public void setBeer(Beer beer) {
            beerView.initialize(beer);
        }
    }
}
