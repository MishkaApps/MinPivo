package mb.minpivo;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;

/**
 * Created by mbolg on 15.11.2017.
 */


public class DigitInput extends AppCompatEditText {

    public DigitInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if(length() == 1 && getSelectionStart() == 0)
            setSelection(1);
    }
}
