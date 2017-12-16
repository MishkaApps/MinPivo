package mb.minpivo;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mbolg on 11.11.2017.
 */

public class KeyboardHider {
    public static void hideKeyboard(Activity activity) {
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }
}
