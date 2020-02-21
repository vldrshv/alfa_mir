package ru.alfabank.alfamir.profile.presentation.profile;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

/**
 * Created by U_M0WY5 on 15.03.2018.
 */

public class  WarningOnExtraChargeDialogFragment extends DialogFragment {

    @BindView(R.id.warning_on_extra_charge_dialog_fl_ok) FrameLayout flOk;
    @BindView(R.id.warning_on_extra_charge_dialog_fl_cancel) FrameLayout flCancel;
    @BindView(R.id.warning_on_extra_charge_dialog_ll_checkbox) LinearLayout llCheckBox;
    @BindView(R.id.warning_on_extra_charge_dialog_cb_checkbox) CheckBox cbCheckBox;

    private WarningDialogListener listener;

    public static WarningOnExtraChargeDialogFragment newInstance() {
        return new WarningOnExtraChargeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_warning_on_extra_charge_dialog, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       listener = (WarningDialogListener) getActivity();

        flOk.setOnClickListener(view1 -> {
            listener.onWarningAccepted(cbCheckBox.isChecked());
            dismiss();
        });

        flCancel.setOnClickListener(view1 -> dismiss());

        llCheckBox.setOnClickListener(view1 -> {
            if(cbCheckBox.isChecked()){
                cbCheckBox.setChecked(false);
            } else {
                cbCheckBox.setChecked(true);
            }
        });
    }

    public interface WarningDialogListener {
        void onWarningAccepted(boolean isChecked);
    }
}
