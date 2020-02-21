package ru.alfabank.alfamir.settings.presentation.settings;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

public class InfoSubscriptionsDialogFragment extends DialogFragment {

    @BindView(R.id.info_subscriptions_dialog_tv_ok)
    TextView tvOk;

    public static InfoSubscriptionsDialogFragment newInstance() {
        InfoSubscriptionsDialogFragment fragment = new InfoSubscriptionsDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_subscriptions_dialog, container);
        ButterKnife.bind(this, view);

        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) return;

        int width = getResources().getDimensionPixelSize(R.dimen.info_favorites_dialog_width);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOk.setOnClickListener(view1 -> {
            dismiss();
        });

    }

}
