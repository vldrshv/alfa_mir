package ru.alfabank.alfamir.poster.presentation

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.http.SslError
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.html_poster_header.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.alfabank.alfamir.Constants
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.poster.data.dto.Poster
import ru.alfabank.alfamir.poster.data.dto.Question
import ru.alfabank.alfamir.utility.toBoolean
import java.util.*
import javax.inject.Inject

class HtmlAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), Player.EventListener {

    private val headersCount = 1
    private var poster: Poster? = null
    private var questions = listOf<Question>()
    private val timeZone = TimeZone.getDefault().rawOffset
    private lateinit var presenter: PosterPresenter
    private lateinit var context: Context
    private lateinit var drawables: HashMap<String, Drawable>

    override fun onCreateViewHolder(parent: ViewGroup, index: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        this.context = parent.context
        return when (index) {
            0 -> HeaderVH(LayoutInflater.from(parent.context).inflate(R.layout.html_poster_header, parent, false))
            2 -> VariableVH(LayoutInflater.from(parent.context).inflate(R.layout.poster_variable_vh, parent, false))
            else -> QuestionVH(LayoutInflater.from(parent.context).inflate(R.layout.question_vh, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> 3
        }
    }

    override fun getItemCount(): Int {
        return when (poster) {
            null -> 0
            else -> questions.count() + headersCount
        }
    }

    fun setData(poster: Poster, drawables: HashMap<String, Drawable>) {
        this.poster = poster
        this.drawables = drawables
        poster.questions?.also { questions = it }
        notifyDataSetChanged()
    }

    fun setPresenter(presenter: PosterPresenter) {
        this.presenter = presenter
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] is Int) {
            if (holder is QuestionVH) {
                val question = questions[payloads[0] as Int]
                holder.setLike(question.isSetLike.toBoolean())
                holder.likes.text = question.likeCount.toString()
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, index: Int) {
        when (holder) {
            is HeaderVH -> bindHeaderVH(holder)
            is VariableVH -> bindVariableVH(holder)
            is QuestionVH -> bindQuestionVH(holder, index)
        }
    }

    // "EventStartDate":"2020-02-17T06:00:00.000Z",
    // "EventEndTime":"2020-02-17T07:00:00.000Z"
    private fun bindHeaderVH(holder: HeaderVH) {
        poster?.also {
            presenter.getImage(it.backgroundImgUrl!!, holder.background, false)

            val date = DateTime.parse(it.eventStartDate).plusMillis(timeZone)
            var formatter = DateTimeFormat.forPattern("dd MMMM").withLocale(Locale("ru", "RU"))
            val dateText = formatter.print(date)
            println("DATE = $dateText")

            formatter = DateTimeFormat.forPattern("hh:mm")

            val sBuilder = StringBuilder()
                    .append(dateText)
                    .append(" ")
                    .append(formatter.print(date))
                    .append(" - ")

            val time = DateTime.parse(it.eventEndTime).plusMillis(timeZone)
            val timeText = formatter.print(time)
            sBuilder.append(timeText)
            println("ALL = $sBuilder")


            holder.title.text = it.title
            holder.dateTime.text = sBuilder.toString()

            if (it.location != null && it.location != "")
                holder.address.text = it.location
            else
                holder.address.visibility = View.GONE

            it.description = Constants.HTML_STYLE_PREFIX + it.description

            holder.content.loadDataWithBaseURL(null, it.description, "text/html", "utf-8", null)
            holder.content.webViewClient = object : WebViewClient() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    super.shouldOverrideUrlLoading(view, request)

                    println(request.url.toString())
                    if (!request.url.toString().contains("alfa")) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, request.url)
                        holder.content.context.startActivity(browserIntent)
                        return true
                    }
                    return false

                }

                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    super.onReceivedSslError(view, handler, error)
                    handler?.proceed()
                }
            }
//            holder.content.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Html.fromHtml(it.description, Html.FROM_HTML_MODE_COMPACT, ImageGetter(), null)
//            } else {
//                Html.fromHtml(it.description, ImageGetter(), null)
//            }
        }
    }

    private fun bindVariableVH(holder: VariableVH) {
    }

    private fun bindQuestionVH(holder: QuestionVH, index: Int) {

    }

    class HeaderVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val background: ImageView = view.background_img
        val dateTime: TextView = view.date_time
        val title: TextView = view.title
        val address: TextView = view.address
        val content: WebView = view.content
    }

    inner class ImageGetter : Html.ImageGetter {
        override fun getDrawable(source: String?): Drawable {

            for ((key, value) in drawables) {
                if (key.contains(source!!)) {
                    value.setBounds(0, 0, value.intrinsicWidth, value.intrinsicHeight)
                    return value
                }
            }
            val value = context.resources.getDrawable(R.drawable.ic_file_v)
            value.setBounds(0, 0, value.intrinsicWidth, value.intrinsicHeight)
            return value
        }
    }
}
