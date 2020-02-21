package ru.alfabank.alfamir.people.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.people.presentation.post_contract.PeopleListContract;
import ru.alfabank.alfamir.people.presentation.view_holder.ProfileViewHolder;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;

import javax.inject.Inject;

import static ru.alfabank.alfamir.Constants.PROFILE_ID;

/**
 * Created by U_M0WY5 on 12.04.2018.
 */

//@Singleton
public class PeopleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements PeopleListContract.ListAdapter, ProfileViewHolder.ProfileVhClickListener {

    private static PeopleListContract.ListPresenter mPresenter;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Inject
    public PeopleListAdapter(PeopleListContract.ListPresenter presenter) {
        mPresenter = presenter;
        mPresenter.takeListAdapter(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.favourite_person_viewholder, parent, false), this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProfileViewHolder profileHolder = (ProfileViewHolder) holder;
        mPresenter.bindListRowView(position, profileHolder);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void onImageDownloaded(int position, Bitmap binaryImage, boolean isNew) {
        ProfileViewHolder viewHolder = (ProfileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) return;

        if (binaryImage == null) {
            viewHolder.setProfilePlaceholder();
            return;
        }
        viewHolder.setProfileImage(binaryImage, isNew);
    }

    @Override
    public void openActivityProfileUi(String id) {
        Intent intent = new Intent(mRecyclerView.getContext(), ProfileActivity.class);
        intent.putExtra(PROFILE_ID, id);
        mRecyclerView.getContext().startActivity(intent);
    }


    @Override
    public void onItemClicked(int position) {
        mPresenter.onItemClicked(position);
    }
}
