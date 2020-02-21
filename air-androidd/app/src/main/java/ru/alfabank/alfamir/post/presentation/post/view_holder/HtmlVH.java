package ru.alfabank.alfamir.post.presentation.post.view_holder;

//rc_news_item_post_web_view

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;

public class HtmlVH extends RecyclerView.ViewHolder implements PostAdapterContract.HtmlRowView {

    @BindView(R.id.rc_news_item_post_web_view)
    WebView webView;
    private ViewHolderClickListener mListener;

    public HtmlVH(View itemView, ViewHolderClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        mListener = listener;

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(webView.getContext().getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDatabasePath(webView.getContext().getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mListener.onContentLoaded(getAdapterPosition());
            }
        });

        webView.setWebViewClient(new WebViewClient() {
        });
    }

    @Override
    public void setHtml(String html) {
        webView.loadData(html, "text/html", null);
    }

    public interface ViewHolderClickListener {
        void onContentLoaded(int position);
    }

}
