package mb.minpivo.beerinfo;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mb.minpivo.R;
import mb.minpivo.database.beers.Beer;
import mb.minpivo.database.users.UsersDatabase;

/**
 * Created by mbolg on 16.12.2017.
 */

public class UsersRatingAdapter extends RecyclerView.Adapter<UsersRatingAdapter.Item> {
    private ArrayList<Pair<String, Integer>> usersRating;

    public UsersRatingAdapter(Beer beer) {
        usersRating = new ArrayList<>();
        for (String id : beer.getUsers().keySet()) {
            if (UsersDatabase.getInstance().getUserNameById(id) == null)
                continue;
            usersRating.add(new Pair<>(id, beer.getUsers().get(id)));
        }
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_info_rating_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        String userId = usersRating.get(position).first;
        holder.setName(UsersDatabase.getInstance().getUserNameById(userId));
        holder.setRating(Integer.toString(usersRating.get(position).second));
    }

    @Override
    public int getItemCount() {
        return usersRating.size();
    }

    class Item extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRating;

        public Item(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_user_name);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }

        public void setName(String name) {
            tvName.setText(name);
        }

        public void setRating(String rating) {
            tvRating.setText(rating);
        }
    }
}
