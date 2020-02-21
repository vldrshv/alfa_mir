package ru.alfabank.alfamir.profile.presentation.skills;

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
 * Created by U_M0WY5 on 19.03.2018.
 */

public class SkillsPromptDialogFragment extends DialogFragment {

    public static SkillsPromptDialogFragment newInstance() {
        SkillsPromptDialogFragment fragment = new SkillsPromptDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM);
        return inflater.inflate(R.layout.info_skills_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvOk = view.findViewById(R.id.skills_prompt_dialog_tv_ok);
        tvOk.setOnClickListener(view1 -> dismiss());
    }
}
