package mb.minpivo.beerinfo;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mb.minpivo.Database;
import mb.minpivo.L;
import mb.minpivo.R;

/**
 * Created by mbolg on 16.12.2017.
 */

public class UsersRatingAdapter extends RecyclerView.Adapter<UsersRatingAdapter.Item> {
    private ArrayList<Pair<String, Long>> rating;

    public UsersRatingAdapter(HashMap<String, Object> data) throws NoNamedUsersException{
        rating = new ArrayList<>();
        for (String id : data.keySet()) {
            if(!Database.isUserNameSetForUser(id))
                continue;
            Pair<String, Long> p = new Pair(id, data.get(id));
            rating.add(p);
        }
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_info_rating_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        holder.setName(Database.getUserNameById(rating.get(position).first));
        holder.setRating(Long.toString(rating.get(position).second));
//        holder.setRating(rating.get(position).second.toString());
    }

    @Override
    public int getItemCount() {
        return rating.size();
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
