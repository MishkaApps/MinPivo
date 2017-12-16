package mb.minpivo;

import android.support.v4.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

import mb.minpivo.Beer.Beer;

/**
 * Created by mbolg on 16.12.2017.
 */

public class BeerInfoBundle implements Serializable {
    private Beer beer;
    private ArrayList<Pair<String, Double>> usersRating;

    public BeerInfoBundle(Beer beer) {
        this.beer = beer;

    }
}
