package mb.minpivo;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import mb.minpivo.beerlist.BeerListActivity;

public class CodeActivity extends AppCompatActivity implements View.OnFocusChangeListener, EnterCapabilityListener {
    private EditText etDummy;
    private ArrayList<DigitInput> etDigits;
    private final String CODE = "1488";
    private ViewGroup lytCode, lytWaitingIcon;
    private ImageView imgvWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        if (Authenticator.isSomeoneAuthed()) {
            goToBeerListActivity();
            return;
        }

        lytCode = (ViewGroup)findViewById(R.id.lyt_code);
        lytWaitingIcon = (ViewGroup)findViewById(R.id.lyt_waiting_icon);
        imgvWaiting = (ImageView) findViewById(R.id.imgv_waiting_icon);

        showWaitingAndHideCode();

        Database.setEnterAvailableValue(this);

        etDummy = (EditText) findViewById(R.id.et_dummy);

        etDigits = new ArrayList<>(4);

        etDigits.add((DigitInput) findViewById(R.id.et_digit_1));
        etDigits.add((DigitInput) findViewById(R.id.et_digit_2));
        etDigits.add((DigitInput) findViewById(R.id.et_digit_3));
        etDigits.add((DigitInput) findViewById(R.id.et_digit_4));

        for (EditText et : etDigits) {
            final int currentIndex = getIndexOfDigitEt(et);

            et.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int keyCode = keyEvent.getKeyCode();
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (currentIndex != 0)
                            if (isFocusedEtEmpty())
                                requestFocusForEtByIndex(currentIndex - 1);
                    } else if (keyCode == KeyEvent.KEYCODE_0
                            || keyCode == KeyEvent.KEYCODE_1
                            || keyCode == KeyEvent.KEYCODE_2
                            || keyCode == KeyEvent.KEYCODE_3
                            || keyCode == KeyEvent.KEYCODE_4
                            || keyCode == KeyEvent.KEYCODE_5
                            || keyCode == KeyEvent.KEYCODE_6
                            || keyCode == KeyEvent.KEYCODE_7
                            || keyCode == KeyEvent.KEYCODE_8) {
                        if (!isFocusedEtEmpty())
                            getCurrentFocusedEt().setText("");
                    }
                    return false;
                }
            });

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() == 1) {
                        if (currentIndex == etDigits.size() - 1) {
                            checkCodeAndGoToBeerListIfCorrect();
                            hideFocusToDummy();
                        } else {
                            requestFocusForView(etDigits.get(currentIndex + 1));
                        }
                    }

                }
            });

            et.setOnFocusChangeListener(this);

            et.setLongClickable(false);

            et.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }
    }

    private void showWaitingAndHideCode() {
        lytWaitingIcon.setVisibility(View.VISIBLE);
        lytCode.setVisibility(View.INVISIBLE);
        animateWaitingIcon();
    }

    private void animateWaitingIcon() {
        imgvWaiting.animate().rotation(360).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                imgvWaiting.setRotation(0);
                animateWaitingIcon();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    private void hideWaitingAndShowCode() {
        lytWaitingIcon.setVisibility(View.INVISIBLE);
        lytCode.setVisibility(View.VISIBLE);
        imgvWaiting.animate().cancel();
    }

    private boolean isFocusedEtEmpty() {
        return getCurrentFocusedEt().getText().length() == 0;
    }

    private EditText getCurrentFocusedEt() {
        for (EditText et :
                etDigits) {
            if (et.isFocused())
                return et;
        }
        return null;
    }

    private void goToBeerListActivity() {
        Intent intent = new Intent(this, BeerListActivity.class);
        startActivity(intent);
    }

    private void checkCodeAndGoToBeerListIfCorrect() {
        StringBuilder code = new StringBuilder(4);
        for (EditText et : etDigits)
            code.append(et.getText());

        if (code.toString().equals(CODE)) {
            goToBeerListActivity();
        } else {
            Toast.makeText(this, "Код неверен. Идите нахуй.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private int getLastEnteredDigitIndex() {
        int curIndex;
        EditText prevEt;
        EditText curEt = etDigits.get(0);

        for (EditText etDigit : etDigits) {
            prevEt = curEt;
            curEt = etDigit;
            curIndex = getIndexOfDigitEt(curEt);
            if (curEt.getText().toString().equals("")
                    && !prevEt.getText().toString().equals(""))
                return curIndex - 1;
        }
        return -1;
    }

    private int getIndexOfDigitEt(View et) {
        return etDigits.indexOf(et);
    }

    @Override
    public void onFocusChange(View view, boolean isViewReceiveFocus) {

        if (isViewReceiveFocus) {
            if (view == etDummy) {
                KeyboardHider.hideKeyboard(this);
                return;
            }


            int lastEnteredDigitIndex = getLastEnteredDigitIndex();
            int indexOfNewFocusedView = getIndexOfDigitEt(view);

            if (lastEnteredDigitIndex == indexOfNewFocusedView)
                return;

            if (isAllEtEmpty() && getIndexOfDigitEt(view) != 0) {
                hideFocusToDummy();
                return;
            }

            if (indexOfNewFocusedView != lastEnteredDigitIndex + 1)
                requestFocusForEtByIndex(lastEnteredDigitIndex + 1);

        }
    }

    private boolean isAllEtEmpty() {
        return (etDigits.get(0).length() == 0)
                && (etDigits.get(1).length() == 0)
                && (etDigits.get(2).length() == 0)
                && (etDigits.get(3).length() == 0);
    }

    private void requestFocusForEtByIndex(int index) {
        requestFocusForView(etDigits.get(index));
    }

    private void requestFocusForView(View view) {
        view.requestFocus();
    }

    private void hideFocusToDummy() {
        etDummy.requestFocus();
        KeyboardHider.hideKeyboard(this);
    }

    @Override
    public void setCapability(boolean allowed) {
        hideWaitingAndShowCode();
    }
}
