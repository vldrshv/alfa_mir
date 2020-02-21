package ru.alfabank.alfamir.initialization.presentation.initialization;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import javax.inject.Inject;

import ru.alfabank.alfamir.Constants;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.base_elements.BaseActivity;
import ru.alfabank.alfamir.initialization.presentation.initialization.contract.InitializationContract;

public class InitializationActivity extends BaseActivity implements InitializationContract.View {

    private int mHeight = 0;
    @Inject
    InitializationContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialization_activity);

        int screenHeight = Constants.FRAGMENT_TYPE_QUIZ_CHECKBOX;
        ImageView imageView = findViewById(R.id.activity_initialization_image_view);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = imageView.getHeight();
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int statusBar = getStatusBarHeight();
                int screenSizeWithoutView = screenHeight - mHeight;
                int realMarginTop = screenSizeWithoutView / 2;
                int marginTopWithStatusBar = realMarginTop - statusBar;
                if (imageView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                    p.setMargins(0, marginTopWithStatusBar, 0, 0);
                    imageView.requestLayout();
                }
            }
        });

        mPresenter.takeView(this);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) result = getResources().getDimensionPixelSize(resourceId);
        return result;
    }

    @Override
    public void close() {
//        Handler handler = new Handler(getMainLooper());
//        handler.post(this::onBackPressed);
        finish();
    }
}
