package ru.alfabank.alfamir.profile.presentation.address;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;

/**
 * Created by U_M0WY5 on 21.02.2018.
 */

public class AddressActivity extends BaseActivity implements AddressContract.View {
    @BindView(R.id.activity_address_tv_physical_address) TextView tvPhysicalAddress;
    @BindView(R.id.activity_address_tv_work_space_full) TextView tvWorkspaceFull;
    @BindView(R.id.activity_address_tv_work_space_short) TextView tvWorkspaceShort;
    @BindView(R.id.activity_address_ll_container_physical_address) LinearLayout llPhysicalAddress;
    @BindView(R.id.activity_address_ll_container_work_space_full) LinearLayout llWorkspaceFull;
    @BindView(R.id.activity_address_ll_container_work_space_short) LinearLayout llWorkspaceShort;
    @BindView(R.id.activity_address_ll_back) LinearLayout llBack;

    @Inject
    AddressContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        ButterKnife.bind(this);

        llBack.setOnClickListener(view -> onBackPressed());
    }


    @Override
    public void showPhysicalAddress(String physicalAddress) {
        tvPhysicalAddress.setText(physicalAddress);
    }

    @Override
    public void showWorkSpaceFull(String workSpaceFull) {
        tvWorkspaceFull.setText(workSpaceFull);
    }

    @Override
    public void showWorkSpaceShort(String workSpaceShort) {
        tvWorkspaceShort.setText(workSpaceShort);
    }

    @Override
    public void hidePhysicalAddress() {
        llPhysicalAddress.setVisibility(View.GONE);
    }

    @Override
    public void hideWorkSpaceFull() {
        llWorkspaceFull.setVisibility(View.GONE);
    }

    @Override
    public void hideWorkSpaceShort() {
        llWorkspaceShort.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkIfInitialized()) return;
        presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }


}
