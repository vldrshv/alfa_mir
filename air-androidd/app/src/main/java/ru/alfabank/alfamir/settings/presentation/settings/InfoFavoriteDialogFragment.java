package ru.alfabank.alfamir.settings.presentation.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.*;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;

public class InfoFavoriteDialogFragment extends DialogFragment {

    @BindView(R.id.info_favorite_dialog_tv_ok)
    TextView tvOk;

    public static InfoFavoriteDialogFragment newInstance() {
        InfoFavoriteDialogFragment fragment = new InfoFavoriteDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_favorite_dialog, container);
        ButterKnife.bind(this, view);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOk.setOnClickListener(view1 -> {
            dismiss();
        });
    }
}