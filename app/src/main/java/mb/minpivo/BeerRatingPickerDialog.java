package mb.minpivo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by mbolg on 12.10.2017.
 */

public class BeerRatingPickerDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        dialogBuilder.setTitle("")
        return super.onCreateDialog(savedInstanceState);
    }
}
