package ru.alfabank.alfamir.search.presentation;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.search.presentation.contract.SearchAdapterContract;
import ru.alfabank.alfamir.search.presentation.contract.SearchFragmentContract;
import ru.alfabank.alfamir.search.presentation.view_holder.PageHeaderVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PageMoreVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PageVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PersonHeaderVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PersonMoreVH;
import ru.alfabank.alfamir.search.presentation.view_holder.PersonVH;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropper;
import ru.alfabank.alfamir.utility.logging.local.LogWrapper;

import static ru.alfabank.alfamir.Constants.Post.FEED_ID;
import static ru.alfabank.alfamir.Constants.Post.FEED_TYPE;
import static ru.alfabank.alfamir.Constants.Post.FEED_URL;
import static ru.alfabank.alfamir.Constants.Post.POST_ID;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE_HEADER;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PAGE_MORE;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PERSON;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PERSON_HEADER;
import static ru.alfabank.alfamir.Constants.Search.VIEW_TYPE_PERSON_MORE;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SearchAdapterContract.Adapter {
    private RecyclerView mRecyclerView;
    private SearchFragmentContract.Presenter mPresenter;
    private ImageCropper mImageCropper;
    private LogWrapper mLog;

    @Inject
    SearchAdapter(SearchFragmentContract.Presenter presenter, LogWrapper log, ImageCropper imageCropper) {
        mPresenter = presenter;
        mLog = log;
        mImageCropper = imageCropper;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
    }

    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public long getItemId(int position) {
        return mPresenter.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_PERSON: {
                return new PersonVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_person_viewholder, viewGroup, false), mPresenter, mImageCropper);
            }
            case VIEW_TYPE_PAGE: {
                return new PageVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_page_viewholder, viewGroup, false), mPresenter, mImageCropper);
            }
            case VIEW_TYPE_PERSON_HEADER: {
                return new PersonHeaderVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_person_header_viewholder, viewGroup, false));
            }
            case VIEW_TYPE_PAGE_HEADER: {
                return new PageHeaderVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_page_header_viewholder, viewGroup, false));
            }
            case VIEW_TYPE_PAGE_MORE: {
                return new PageMoreVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_page_more_viewholder, viewGroup, false), mPresenter);
            }
            case VIEW_TYPE_PERSON_MORE: {
                return new PersonMoreVH(LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.search_person_more_viewholder, viewGroup, false), mPresenter);
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PersonVH) {
            PersonVH personVH = (PersonVH) viewHolder;
            mPresenter.bindListRowPerson(position, personVH);
        } else if (viewHolder instanceof PageVH) {
            PageVH pageVH = (PageVH) viewHolder;
            mPresenter.bindListRowPage(position, pageVH);
        }
    }

    @Override
    public void onDataSetChanged() {
        notifyDataSetChanged();
    }

    @Override
    public void onProfileImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated) {
    }

    @Override
    public void onPostImageLoaded(int position, long viewId, String encodedImage, boolean isAnimated) {
    }

    @Override
    public void openPostUi(String feedId, String postId, String feedUrl, String feedType) {
        Intent intent = new Intent(mRecyclerView.getContext(), PostActivity.class);
        intent.putExtra(FEED_TYPE, feedType);
        intent.putExtra(POST_ID, postId);
        intent.putExtra(FEED_URL, feedUrl);
        intent.putExtra(FEED_ID, feedId);
        mRecyclerView.getContext().startActivity(intent);
    }
}
