package ru.alfabank.alfamir.profile.presentation.absence;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import ru.alfabank.alfamir.R;

/**
 * Created by U_M0WY5 on 15.03.2018.
 */

public class DelegatePromptDialogFragment extends DialogFragment {

    public static DelegatePromptDialogFragment newInstance() {
        DelegatePromptDialogFragment fragment = new DelegatePromptDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM);

//        WindowManager.LayoutParams wmlp = getDialog().getWindow().getAttributes();
//        wmlp.gravity = Gravity.FILL_HORIZONTAL|Gravity.BOTTOM;

        return inflater.inflate(R.layout.info_delegate_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvOk = view.findViewById(R.id.delegate_prompt_dialog_tv_ok);
        tvOk.setOnClickListener(view1 -> dismiss());
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, );
//    }
}
