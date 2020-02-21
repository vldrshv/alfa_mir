package ru.alfabank.alfamir.post.presentation.post.view_holder;

import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract;

public class TextVH extends RecyclerView.ViewHolder implements PostAdapterContract.TextRowView{

    @BindView(R.id.rc_news_item_post_text_tv_text)
    TextView text;

    public TextVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setPostBody(String body) {
        String textBody = body.replace("<p>", "").replace("</br>", "\n\n").replace("</p>", "\n\n");
        while (textBody.endsWith("\n"))
            textBody = textBody.substring(0, textBody.length() - 2);
//        text.setText(body);
        text.setText(Html.fromHtml(body));
    }

    @Override
    public void isSubHeader(Boolean isSubHeader) {
        if (isSubHeader)
            makeSubHeaderFromText();
    }

    private void makeSubHeaderFromText() {
        text.setTextSize(26);
        text.setLineSpacing(5,1);
        text.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,10,20,40);
        text.setLayoutParams(params);
    }
}