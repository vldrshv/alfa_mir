package ru.alfabank.alfamir.profile.presentation.profile;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.common.base.Strings;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

/**
 * Created by U_M0WY5 on 13.03.2018.
 */

public class EditMobilePhoneDialogFragment extends DialogFragment {

    @BindView(R.id.edit_mobile_dialog_fl_save) FrameLayout flSave;
    @BindView(R.id.edit_mobile_dialog_fl_cancel) FrameLayout flCancel;
    @BindView(R.id.edit_mobile_dialog_et_blank_space) EditText editText;
    @BindView(R.id.edit_mobile_dialog_image_delete) ImageView imageDelete;

    private boolean mIsPhoneEntered;
    private EditPhoneDialogListener listener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static EditMobilePhoneDialogFragment newInstance(String phone) {
        EditMobilePhoneDialogFragment fragment = new EditMobilePhoneDialogFragment();
        if(Strings.isNullOrEmpty(phone)){

        } else {
            Bundle args = new Bundle();
            args.putString("phone", phone);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_mobile_dialog, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (EditPhoneDialogListener) getActivity();


        MaskImpl mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(editText);
        setInputValidation();

        // We must set edittext after FormatWatcher is set. It will work wrong otherwise :(

        Bundle bundle = getArguments();
        if(bundle!=null){
            String phone = getArguments().getString("phone","");
            if(phone.length()==18){
                editText.setText(phone);
                editText.setSelection(phone.length());
                imageDelete.setVisibility(View.VISIBLE);
                setButtonState(phone.length());
            }
        }

        flSave.setOnClickListener(view1 -> {
            if(mIsPhoneEntered){
                String input = editText.getText().toString();
                listener.onNumberEntered(input);
                dismiss();
            }
        });

        flCancel.setOnClickListener(view1 -> dismiss());
        imageDelete.setOnClickListener(view1 -> {
            listener.onNumberDeleted();
            dismiss();
        });

    }

    private void setInputValidation(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                setButtonState(input.length());
            }
        });
    }

    private void setButtonState(int length){
        int validLength = 18;

        if(length == validLength){
            mIsPhoneEntered = true;
            flSave.setBackgroundColor(Color.parseColor("#1875f0"));
        } else {
            mIsPhoneEntered = false;
            flSave.setBackgroundColor(Color.parseColor("#cccccc"));
        }
    }


    public interface EditPhoneDialogListener {
        void onNumberEntered(String number);
        void onNumberDeleted();
    }
}
