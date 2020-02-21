package ru.alfabank.alfamir.post.presentation.post.view_holder

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.post.presentation.post.post_contract.PostAdapterContract

class SingleHtmlVH @SuppressLint("SetJavaScriptEnabled")
constructor(itemView: View, private val mListener: ViewHolderClickListener) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), PostAdapterContract.SingleHtmlRowView {

    internal var webView: WebView = itemView.findViewById(R.id.post_single_html_view_holder)

    init {
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JavaScriptInterface(), "app")
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                mListener.onContentLoaded(adapterPosition)
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val uri = request.url
                mListener.onUriClicked(uri)
                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }

        val webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                val alertBuilder = AlertDialog.Builder(webView.context)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                val alert = alertBuilder.create()
                alert.show()
                result.confirm()
                return true
            }

            override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                val input = EditText(webView.context)
                input.inputType = InputType.TYPE_CLASS_NUMBER
                input.setText(defaultValue)
                val alertBuilder = AlertDialog.Builder(webView.context)
                        .setView(input)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { _, _ -> result.confirm(input.text.toString()) }
                        .setNegativeButton(android.R.string.cancel) { _, _ -> result.cancel() }

                val alert = alertBuilder.create()
                alert.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                alert.show()
                alert.setCanceledOnTouchOutside(false)
                return true
            }

        }
        webView.webChromeClient = webChromeClient
    }

    override fun setHtml(html: String) {
        val htmlS = Constants.HTML_STYLE_PREFIX + html
        webView.loadDataWithBaseURL(null, htmlS, "text/html", "utf-8", null)
    }

    override fun injectJavaScriptPostImage(javaScript: String) {
        val handler = Handler()
        handler.postDelayed({ webView.evaluateJavascript(javaScript, null) }, 500)
    }

    override fun injectJavaScriptVideoUrl(javaScript: String) {
        webView.evaluateJavascript(javaScript, null)
    }

    override fun injectJavaScriptVideoPoster(javaScript: String) {
        val handler = Handler()
        handler.post { webView.evaluateJavascript(javaScript, null) }
    }

    override fun setOnPause() {
        webView.onPause()
    }

    override fun setOnResume() {
        webView.onResume()
    }

    inner class JavaScriptInterface {
        @JavascriptInterface
        fun onVideoClicked(id: String, password: String) {
            mListener.onVideoClicked(id, password, adapterPosition)
        }
    }

    interface ViewHolderClickListener {
        fun onContentLoaded(position: Int)

        fun onJsPrompt(message: String, position: Int)

        fun onVideoClicked(id: String, password: String, position: Int)

        fun onUriClicked(uri: Uri)
    }
}
