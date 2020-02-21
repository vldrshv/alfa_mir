package ru.alfabank.alfamir.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class InvisibleToBackPressedEditText extends androidx.appcompat.widget.AppCompatEditText {

    private KeyboardCatcher keyboardCatcher;

    public InvisibleToBackPressedEditText(Context context) {
        super(context);
    }

    public InvisibleToBackPressedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InvisibleToBackPressedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setListener(KeyboardCatcher keyboardCatcher){
        this.keyboardCatcher = keyboardCatcher;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            keyboardCatcher.catchKeyboard();
            return true;  // So it is not propagated.
        }
        return super.dispatchKeyEvent(event);
    }

    public interface KeyboardCatcher {
        void catchKeyboard();
    }

}
