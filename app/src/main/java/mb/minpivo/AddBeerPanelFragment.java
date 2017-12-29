package mb.minpivo;


import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mb.minpivo.database.beers.Beer;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBeerPanelFragment extends Fragment {
    private EditText etNewBeerName;
    private Button btnAddBeer, btnHideAddBeerPanel;
    private final long PLUS_ROTATION_ANIMATION_DURATION = 300, APPEAR_DISAPPEAR_ANIMATION_DURATION = 300;
    private int previousBeerNameLength;

    public AddBeerPanelFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_add_beer_panel, container, false);

        etNewBeerName = fragment.findViewById(R.id.et_beer_name);
        btnAddBeer = fragment.findViewById(R.id.btn_add_beer);
        btnHideAddBeerPanel = fragment.findViewById(R.id.btn_hide_add_beer_panel);

        btnHideAddBeerPanel.animate().rotation(45).setDuration(0).start();

        etNewBeerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etNewBeerName.getText().length() == 0) {
                    if (previousBeerNameLength == 1)
                        hideBtnAddBeerAndShowBtnHideAddBeerPanel();
                } else if (etNewBeerName.getText().length() == 1 && previousBeerNameLength == 0) {
                    showBtnAddBeerAndHideBtnHideAddBeerPanel();
                }
                previousBeerNameLength = etNewBeerName.getText().length();
            }
        });

        btnAddBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewBeer();
            }
        });

        btnHideAddBeerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });

        etNewBeerName.setText("");
        previousBeerNameLength = 0;

        return fragment;
    }


    private void showBtnAddBeerAndHideBtnHideAddBeerPanel() {
        btnHideAddBeerPanel.setVisibility(View.INVISIBLE);
        btnAddBeer.setVisibility(View.VISIBLE);
        btnAddBeer.animate().rotation(45).setDuration(0).start();
        btnAddBeer.animate().rotation(0).setDuration(PLUS_ROTATION_ANIMATION_DURATION).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void hideBtnAddBeerAndShowBtnHideAddBeerPanel() {
        btnAddBeer.setVisibility(View.INVISIBLE);
        btnHideAddBeerPanel.setVisibility(View.VISIBLE);
        btnHideAddBeerPanel.animate().rotation(0).setDuration(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                btnHideAddBeerPanel.animate().rotation(45).setDuration(PLUS_ROTATION_ANIMATION_DURATION).setInterpolator(new AccelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        KeyboardHider.hideKeyboard(getActivity());
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public void show() {
        getView().setVisibility(View.VISIBLE);

        getView().animate().yBy(getView().getHeight()).setDuration(0).start();
        getView().animate().yBy(-getView().getHeight()).setDuration(APPEAR_DISAPPEAR_ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());


        etNewBeerName.setText("");
        previousBeerNameLength = 0;
        hideBtnAddBeerAndShowBtnHideAddBeerPanel();
    }

    public void hide() {
        getView().animate().yBy(getView().getHeight()).setDuration(APPEAR_DISAPPEAR_ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                getView().animate().yBy(-getView().getHeight()).setDuration(0);
                getView().animate().setListener(null);
                getView().setVisibility(View.INVISIBLE);
                getActivity().invalidateOptionsMenu();
                KeyboardHider.hideKeyboard(getActivity());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void addNewBeer() {

        String name = etNewBeerName.getText().toString();

        boolean isAppWorkAvailable = ((WorkAvailabilityProvider) getActivity()).isWorkAvailable();

        if (isAppWorkAvailable) {
            if (BeerNameValidator.check(name)) {
                Beer newBeer = null;
                try {
                    newBeer = new Beer(name);
                } catch (Authenticator.UserNotAuthedException e) {
                    return;
                }
                Database.tryAddBeer(newBeer);
            } else {
                Toast.makeText(getActivity(), "Необходимо менее ущербное имя", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Добавление пиваса недоступно", Toast.LENGTH_SHORT).show();
        }


        hide();
        etNewBeerName.setText("");
        etNewBeerName.clearFocus();
    }

}
