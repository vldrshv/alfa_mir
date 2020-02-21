package ru.alfabank.alfamir.ui.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class PasswordEditText extends androidx.appcompat.widget.AppCompatEditText{
    private EditTextListener mListener;

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHideKeyboardListener(EditTextListener listener){
        mListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(mListener!=null) {
                mListener.hideKeyboard();
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public interface EditTextListener {
        void hideKeyboard();
    }
}
