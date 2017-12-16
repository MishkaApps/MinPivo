package mb.minpivo.beerinfo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mb.minpivo.Beer.Beer;
import mb.minpivo.BeerRatingPickerDialog;
import mb.minpivo.R;

/**
 * Created by mbolg on 16.12.2017.
 */

public class BeerInfoFragment extends DialogFragment {
    private TextView tvBeerName;
    private RecyclerView rvUsersRating;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.beer_info, null);
        builder.setView(layout);
        tvBeerName = layout.findViewById(R.id.tv_beer_name);
        rvUsersRating = layout.findViewById(R.id.rv_rating_list);
        Beer beer = (Beer) getArguments().getSerializable("beer");


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvUsersRating.setLayoutManager(layoutManager);
        try {
            rvUsersRating.setAdapter(new UsersRatingAdapter(beer.getUsers()));
        } catch (NoNamedUsersException e) {
            e.printStackTrace();
        }


        return builder.create();
    }

}
