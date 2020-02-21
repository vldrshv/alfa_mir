package ru.alfabank.alfamir.post.presentation.post;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostContract;
import ru.alfabank.alfamir.post.presentation.post.view_holder.BlankSpaceVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.CommentVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.FooterVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.HeaderVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.HtmlVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.PictureVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.QuoteVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.SingleHtmlVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.TextVH;
import ru.alfabank.alfamir.post.presentation.post.view_holder.VideoVH;

import static ru.alfabank.alfamir.Constants.Post.POST_COMMENT;
import static ru.alfabank.alfamir.Constants.Post.POST_EXTRA_SPACE;
import static ru.alfabank.alfamir.Constants.Post.POST_FOOTER;
import static ru.alfabank.alfamir.Constants.Post.POST_HEADER;
import static ru.alfabank.alfamir.Constants.Post.POST_HTML;
import static ru.alfabank.alfamir.Constants.Post.POST_PICTURE;
import static ru.alfabank.alfamir.Constants.Post.POST_QUOTE;
import static ru.alfabank.alfamir.Constants.Post.POST_SINGLE_HTML;
import static ru.alfabank.alfamir.Constants.Post.POST_SUB_HEADER;
import static ru.alfabank.alfamir.Constants.Post.POST_TEXT;
import static ru.alfabank.alfamir.Constants.Post.POST_VIDEO;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PostAdapterContract.Adapter {

    private RecyclerView mRecyclerView;
    private PostContract.Presenter mPresenter;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mPresenter.takeListAdapter(this);
    }

    @Inject
    public PostAdapter(PostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        return mPresenter.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case POST_HEADER:
                return new HeaderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_header_viewholder, parent, false), mPresenter);
            case POST_TEXT:
            case POST_SUB_HEADER:
                return new TextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_text_viewholder, parent, false));
            case POST_HTML:
                return new HtmlVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_html_viewholder, parent, false), mPresenter);
            case POST_SINGLE_HTML:
                return new SingleHtmlVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_single_html_viewholder, parent, false), mPresenter);
            case POST_PICTURE:
                return new PictureVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_picture_viewholder, parent, false), mPresenter);
            case POST_FOOTER:
                return new FooterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_footer_viewholder, parent, false), mPresenter);
            case POST_COMMENT:
                return new CommentVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_comment_viewholder, parent, false), mPresenter);
            case POST_EXTRA_SPACE:
                return new BlankSpaceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_blank_basement_viewholder, parent, false));
            case POST_VIDEO:
                return new VideoVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_video_viewholder, parent, false));
            case POST_QUOTE:
                return new QuoteVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_quote_viewholder, parent, false), mPresenter);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderVH) {
            HeaderVH rowView = (HeaderVH) holder;
            mPresenter.bindListRowHeader(position, rowView);
        } else if (holder instanceof TextVH) {
            TextVH rowView = (TextVH) holder;
            mPresenter.bindListRowText(position, rowView);
        } else if (holder instanceof HtmlVH) {
            HtmlVH rowView = (HtmlVH) holder;
            mPresenter.bindListRowHtml(position, rowView);
        } else if (holder instanceof SingleHtmlVH) {
            SingleHtmlVH rowView = (SingleHtmlVH) holder;
            mPresenter.bindListRowSingleHtml(position, rowView);
        } else if (holder instanceof PictureVH) {
            PictureVH rowView = (PictureVH) holder;
            mPresenter.bindListRowPicture(position, rowView);
        } else if (holder instanceof FooterVH) {
            FooterVH rowView = (FooterVH) holder;
            mPresenter.bindListRowFooter(position, rowView);
        } else if (holder instanceof CommentVH) {
            CommentVH rowView = (CommentVH) holder;
            mPresenter.bindListRowComment(position, rowView);
        } else if (holder instanceof QuoteVH) {
            QuoteVH rowView = (QuoteVH) holder;
            mPresenter.bindListRowQuote(position, rowView);
        } else if (holder instanceof VideoVH){
            VideoVH rowView = (VideoVH) holder;
            mPresenter.bindListRowVideo(position, rowView);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof VideoVH)
            ((VideoVH) holder).releaseVideo();
    }

    @Override
    public int getItemCount() {
        return mPresenter.getListSize();
    }

    @Override
    public void onPostImageInject(int position, String javaScript) {
        SingleHtmlVH singleHtmlVH = (SingleHtmlVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (singleHtmlVH == null) {
            notifyItemChanged(position);
            return;
        }
        singleHtmlVH.injectJavaScriptPostImage(javaScript);
    }

    @Override
    public void onPostVideoUrlInject(int position, String javaScript) {
        SingleHtmlVH singleHtmlVH = (SingleHtmlVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (singleHtmlVH == null) {
            notifyItemChanged(position);
            return;
        }
        singleHtmlVH.injectJavaScriptVideoUrl(javaScript);
    }

    @Override
    public void onPostVideoPosterInject(int position, String javaScript) {
        SingleHtmlVH singleHtmlVH = (SingleHtmlVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (singleHtmlVH == null) {
            notifyItemChanged(position);
            return;
        }
        singleHtmlVH.injectJavaScriptVideoPoster(javaScript);
    }

    @Override
    public void openPostOptions(int isDeletable, String favoriteStatus, String subscribeStatus) {
        HeaderVH viewHolder = (HeaderVH) mRecyclerView.findViewHolderForAdapterPosition(0);
        if (viewHolder == null) {
            return;
        }
        viewHolder.showOptions(isDeletable, favoriteStatus, subscribeStatus);
    }

    @Override
    public void setCommentLikeState(int position, String likesCount, int currentUserLike) {
        CommentVH viewHolder = (CommentVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setLikeStatus(likesCount, currentUserLike);
    }

    @Override
    public void setPostLikeState(int position, String likesCount, int currentUserLike) {
        FooterVH viewHolder = (FooterVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setLikeStatus(likesCount, currentUserLike);
    }

    @Override
    public void setWebViewOnPause(int position) {
        SingleHtmlVH viewHolder = (SingleHtmlVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setOnPause();
    }

    @Override
    public void setWebViewOnResume(int position) {
        SingleHtmlVH viewHolder = (SingleHtmlVH) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            notifyItemChanged(position);
            return;
        }
        viewHolder.setOnResume();
    }
}
