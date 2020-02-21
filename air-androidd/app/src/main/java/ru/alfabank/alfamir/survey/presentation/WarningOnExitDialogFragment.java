package ru.alfabank.alfamir.survey.presentation;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

public class WarningOnExitDialogFragment extends DialogFragment {

    @BindView(R.id.warning_on_exit_dialog_fl_ok)
    FrameLayout flOk;
    @BindView(R.id.warning_on_exit_dialog_fl_cancel)
    FrameLayout flCancel;

    private WarningDialogListener listener;

    public static WarningOnExitDialogFragment newInstance() {
        WarningOnExitDialogFragment fragment = new WarningOnExitDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_warning_on_exit_dialog, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (WarningDialogListener) getActivity();

        flOk.setOnClickListener(view1 -> {
            listener.onWarningAccepted();
            dismiss();
        });

        flCancel.setOnClickListener(view1 -> dismiss());
    }

    public interface WarningDialogListener {
        void onWarningAccepted();
    }
}
