package mb.minpivo.Beer;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import mb.minpivo.Authenticator;
import mb.minpivo.Config;
import mb.minpivo.Database;
import mb.minpivo.L;
import mb.minpivo.R;
import mb.minpivo.WorkAvailabilityProvider;
import mb.minpivo.beerinfo.BeerInfoFragment;
import mb.minpivo.database.DatasetChangedListener;
import mb.minpivo.database.users.UsersDatabase;

/**
 * Created by mbolg on 02.09.2017.
 */

public class BeerView extends CardView implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, DatasetChangedListener {
    private mb.minpivo.database.beers.Beer beer;
    private TextView name;
    private TextView rating, usersRating;
    private ImageView ivRatedByCurrentUser;
    private TextView tvAuthorName;


    public BeerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(mb.minpivo.database.beers.Beer beer) {
        this.beer = beer;
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        usersRating = findViewById(R.id.users_rating);
        ivRatedByCurrentUser = findViewById(R.id.iv_rated_by_current_user);
        tvAuthorName = findViewById(R.id.tv_author_name);

        name.setText(beer.getName());
        ivRatedByCurrentUser.setVisibility(beer.isRatedByCurrentUser() ? VISIBLE : INVISIBLE);
        tvAuthorName.setText(beer.getAuthorName());

        if (beer.isRatedByMe())
            rating.setText(Integer.toString(beer.getMyRating()));
        else rating.setText(" ");

        if (beer.isRatedBySomeoneElse())
            usersRating.setText(Integer.toString(beer.getAverUsersRating()));
        else usersRating.setText(" ");

        paintMyRatingView();
        paintUsersRatingView();

        if (Authenticator.isIAuthed())
            pickerRating = beer.getMyRating();
        else if (Authenticator.isSomeoneExceptMeAuthed())
            pickerRating = beer.getCurrentUserRating();

        rating.setOnClickListener(this);
        usersRating.setOnClickListener(this);
        UsersDatabase.getInstance().addDatasetChangedListener(this);
    }

    private boolean isNecessaryToShowBeerInfo() {
        for (String id : beer.getUsers().keySet())
            if (UsersDatabase.getInstance().getUserNameById(id) != null)
                return true;

        return false;
    }

    private void showBeerInfoFragment() {
        isNecessaryToShowBeerInfo();
        FragmentManager manager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        BeerInfoFragment beerInfoFragment = new BeerInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("beer", beer);
        beerInfoFragment.setArguments(bundle);
        beerInfoFragment.show(manager, "beer_info");
    }


    private void paintMyRatingView() {
        rating.setBackground(getBackgroundAccordinglyRating(beer.getMyRating()));
    }

    private void paintUsersRatingView() {
        usersRating.setBackground(getBackgroundAccordinglyRating((int) beer.getAverUsersRating()));
    }

    private Drawable getBackgroundAccordinglyRating(int rating) {
        ColorDrawable ratingBackground;
        if (rating < 0) {
            ratingBackground = new ColorDrawable(getResources().getColor(R.color.negative_rating));
            ratingBackground.setAlpha(55 + -rating * 200 / 10);
        } else if (rating > 0) {
            ratingBackground = new ColorDrawable(getResources().getColor(R.color.positive_rating));
            ratingBackground.setAlpha(55 + rating * 200 / 10);
        } else {
            ratingBackground = new ColorDrawable(getResources().getColor(R.color.neutral_rating));
        }
        return ratingBackground;
    }


    private int pickerRating;
    private TextView tvBeerRatingInDialog;

    private void createRatingPickerDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.beer_rating_picker, null);
        SeekBar ratingPicker = dialogView.findViewById(R.id.rating_picker);
        ratingPicker.setProgress(pickerRating + 10);
        ratingPicker.setOnSeekBarChangeListener(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Ок", (dialogInterface, i) -> {
            try {
                if (Authenticator.getCurrentUserEmail().equals(Config.SONYA_EMAIL))
                    if (pickerRating == 10 || pickerRating == -10) {
                        Toast.makeText(getContext(), "Cоня, иди нахуй (от Папы)", Toast.LENGTH_SHORT).show();
                        return;
                    }
            } catch (Authenticator.UserNotAuthedException userNotAuthed) {
                userNotAuthed.printStackTrace();
            }
            Database.setRatingToBeer(beer, pickerRating);
        });
        ((TextView) dialogView.findViewById(R.id.message)).setText("Ваша оценка пива " + beer.getName());
        tvBeerRatingInDialog = dialogView.findViewById(R.id.rating);
        tvBeerRatingInDialog.setText(Integer.toString(pickerRating));
        dialogBuilder.create().show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        pickerRating = i - 10;
        tvBeerRatingInDialog.setText(Integer.toString(pickerRating));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View view) {
        if (!((WorkAvailabilityProvider) getContext()).isWorkAvailable()) {
            Toast.makeText(getContext(), "Оценка пиваса недоступна", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.rating:
                if (Authenticator.isIAuthed())
                    createRatingPickerDialog();
                break;
            case R.id.users_rating:
                if (Authenticator.isSomeoneExceptMeAuthed())
                    createRatingPickerDialog();
                break;
        }
    }

    @Override
    public void notifyDataChanged() {
        if (isNecessaryToShowBeerInfo())
            name.setOnClickListener(view -> showBeerInfoFragment());
        else name.setOnClickListener(null);
    }
}
