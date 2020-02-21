package ru.alfabank.alfamir.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.format.Formatter;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.alfamir.R;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepository;
import ru.alfabank.alfamir.image.data.source.repository.ImageRepository;
import ru.alfabank.alfamir.base_elements.BaseActivity;

public class TestActivity2 extends BaseActivity {

    @Inject
    ShowRepository mAlfaTvRepository;
    @Inject
    ImageRepository mImageRepository;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_web_view);

        Intent test = getIntent();
        Set<String> keySet = test.getExtras().keySet();

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");
        WebChromeClient webChromeClient = new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                final EditText taskEditText = new EditText(view.getContext());
                AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
                        setTitle("YourAlertTitle").
                        setMessage(message).
                        setView(taskEditText).
                        setPositiveButton("OK", (dialog1, which) -> {
                            result.confirm(taskEditText.getText().toString());
                        }).create();
                dialog.show();
                result.confirm();
                return true;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
            }
        };


//        webChromeClient.set

        webView.setWebChromeClient(webChromeClient);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                String imageUrl = "http://it/tv/AlfaMir/Альфа-Мир/Alfa Future People, второй день/Preview Images/alfatv_logo.jpg";
//                mImageRepository.fillImage(imageUrl, 1080, 250, new ImageDataSource.LoadImageCallback() {
//                    @Override
//                    public void onImageLoaded(Image img, boolean isCached) {
//                        String encodedImage = img.getBinaryImage();
//                        String videoId = "alfatv_iframe1";
//                        webView.evaluateJavascript("setPoster(\"" + videoId + "\",\"getData:image/jpeg;charset=utf-8;base64," + encodedImage + "\")", null);
//                    }
//
//                    @Override
//                    public void onDataNotAvailable() {
//
//                    }
//                });
//            }
//
//
//           // http://it/tv/AlfaMir/Альфа-Мир/Alfa Future People, второй день/Preview Images/alfatv_logo.jpg
//        });


        webView.loadUrl("file:///android_asset/news_item_with_video.html");

    }

    public class WebViewJavaScriptInterface {

        private Context context;

        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        @SuppressLint("CheckResult")
        @JavascriptInterface
        public void postMessage(String message, int password){
//            String videoId = message;
            String html_id = message;
            String videoId = "1019";
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            String phoneIp = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            mAlfaTvRepository.getVideoUrl(videoId, "172.28.67.209", "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(json -> {
                        Gson gson = new Gson();
                        ServerResponse response = gson.fromJson(json, ServerResponse.class);
                        String urlstr = response.url + "?token=" + response.token;
//                        String urlstr = "https://youtu.be/TxSSF-SnuEQ";
                        webView.evaluateJavascript("changeVideoSource(\"" + html_id + "\",\"" + urlstr + "\")", null);
                    });
        }

    }

    class ServerResponse {
        String url;
        String token;
    }

}
