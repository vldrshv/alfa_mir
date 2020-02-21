package ru.alfabank.alfamir.poster.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.playback_control.view.*
import kotlinx.android.synthetic.main.poster_header_vh.view.*
import kotlinx.android.synthetic.main.poster_variable_vh.view.*
import kotlinx.android.synthetic.main.poster_video_vh.view.*
import kotlinx.android.synthetic.main.question_vh.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.alfabank.alfamir.Constants.Initialization.SCREEN_WIDTH_PHYSICAL
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.poster.data.dto.Poster
import ru.alfabank.alfamir.poster.data.dto.Question
import ru.alfabank.alfamir.utility.toBoolean
import java.util.*
import javax.inject.Inject

class PosterAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), Player.EventListener {

    private val headersCount = 3
    private var poster: Poster? = null
    private var questions = listOf<Question>()
    private val timeZone = TimeZone.getDefault().rawOffset
    private lateinit var presenter: PosterPresenter
    private lateinit var context: Context
    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private var player: SimpleExoPlayer? = null
    private var playerView: PlayerView? = null


    override fun onCreateViewHolder(parent: ViewGroup, index: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        this.context = parent.context
        return when (index) {
            0 -> VideoVH(LayoutInflater.from(parent.context).inflate(R.layout.poster_video_vh, parent, false))
            1 -> HeaderVH(LayoutInflater.from(parent.context).inflate(R.layout.poster_header_vh, parent, false))
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

    fun setData(poster: Poster) {
        this.poster = poster
        poster.questions?.also { questions = it }
        notifyDataSetChanged()
    }

    fun setItem(position: Int, poster: Poster) {
        this.poster = poster
        notifyItemChanged(position + headersCount, position)
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
            is VideoVH -> bindVideoVH(holder)
            is HeaderVH -> bindHeaderVH(holder)
            is VariableVH -> bindVariableVH(holder)
            is QuestionVH -> bindQuestionVH(holder, index)
        }
    }

    private fun bindVideoVH(holder: VideoVH) {

        poster?.also {
            if (it.videoUrl.isNullOrBlank()) {
                holder.player.visibility = View.GONE
                holder.phContainer.visibility = View.VISIBLE
                presenter.getImage(it.backgroundImgUrl!!, holder.placeholder, false)

                val date = DateTime.parse(it.eventStartDate).plusMillis(timeZone)
                var formatter = DateTimeFormat.forPattern("dd MMMM").withLocale(Locale("ru", "RU"))
                holder.date.text = formatter.print(date)

                formatter = DateTimeFormat.forPattern("hh:mm")

                val sBuilder = StringBuilder()
                        .append(formatter.print(date))
                        .append(" - ")

                val time = DateTime.parse(it.eventEndTime).plusMillis(timeZone)
                sBuilder.append(formatter.print(time))
                holder.time.text = sBuilder.toString()

                holder.description.text = it.description
            } else {
                holder.player.visibility = View.VISIBLE
                holder.phContainer.visibility = View.GONE

                playerView = holder.player.apply {
                    layoutParams.height = ((SCREEN_WIDTH_PHYSICAL / 16) * 9)
                }


                holder.llMute.setOnClickListener {
                    val currentVolume = player?.volume
                    if (currentVolume?.toDouble() != 0.0) {
                        player?.volume = 0f
                        holder.imageMute.setImageDrawable(context.getDrawable(R.drawable.ic_playback_mute_off))
                    } else {
                        player?.volume = 1f
                        holder.imageMute.setImageDrawable(context.getDrawable(R.drawable.ic_playback_mute))
                    }
                }
                initPlayer()
            }
        }
    }

    private fun bindHeaderVH(holder: HeaderVH) {
        holder.writeQuestion.setOnClickListener {
            presenter.writeQuestion()
        }
    }

    private fun bindVariableVH(holder: VariableVH) {
        if (questions.isNullOrEmpty()) {
            holder.emptyContainer.visibility = View.VISIBLE
            holder.likesContainer.visibility = View.GONE
        } else {
            holder.emptyContainer.visibility = View.GONE
            holder.likesContainer.visibility = View.VISIBLE
            holder.questionsCount.text = String.format("Вопросов: %d", questions.count())
            holder.sortLabel.text = context.getString(poster!!.sort)
            holder.sortButton.setOnClickListener {
                presenter.onSortClicked()
            }
        }
    }

    private fun bindQuestionVH(holder: QuestionVH, index: Int) {
        val question = questions[index - headersCount]
        val isLiked = question.isSetLike.toBoolean()

        holder.name.text = question.user.fullName
        holder.body.text = question.body
        holder.setLike(isLiked)
        val date = DateTime.parse(question.date).plusMillis(timeZone)
        val formatter = DateTimeFormat.forPattern("dd MMM в HH:mm").withLocale(Locale("ru", "RU"))
        holder.date.text = formatter.print(date)
        holder.likes.text = question.likeCount.toString()
        holder.container.setOnClickListener {
            presenter.toggleLike(question.id, !isLiked)
        }
        presenter.getImage(question.user.profilePicUrl!!, holder.avatar, true)
    }

    private fun initPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(context).also { player ->
            playerView?.player = player
            player.playWhenReady = true

            val uri = Uri.parse(poster!!.videoUrl)
            dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "ExoPlayerDemo"))
            val videoSource = buildMediaSource(uri)
            player.prepare(videoSource)
        }
    }

    @SuppressLint("SwitchIntDef")
    private fun buildMediaSource(uri: Uri): MediaSource {
        val type = Util.inferContentType(uri)
        return when (type) {
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            else -> {
                throw IllegalStateException("Unsupported type: $type")
            }
        }
    }

    fun onResume() {
        if (playerView != null) {
            initPlayer()
        }
    }

    fun onPause() {
        player?.release()
    }
}

class VideoVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val player: PlayerView = view.player
    val placeholder: ImageView = view.video_placeholder
    val date: TextView = view.video_date
    val time: TextView = view.video_time
    val description: TextView = view.video_description
    val phContainer: FrameLayout = view.placeholder_container
    val llMute: FrameLayout = view.exo_mute
    val imageMute: ImageView = view.exo_mute_image

}

class HeaderVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val writeQuestion: FrameLayout = view.write_question_container
}

class VariableVH(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val emptyContainer: ConstraintLayout = view.empty_container
    val likesContainer: LinearLayout = view.likes_counter_container
    val questionsCount: TextView = view.questions_count
    val sortButton: FrameLayout = view.sort_button
    val sortLabel: TextView = view.sort_label
}

class QuestionVH(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val name: TextView = view.name
    val body: TextView = view.question_text
    val date: TextView = view.date
    val likes: TextView = view.likes_count
    val avatar: ImageView = view.avatar
    val like: ImageView = view.like
    val container: FrameLayout = view.likes_container

    fun setLike(isSetLike: Boolean) {
        val id = if (isSetLike) ContextCompat.getColor(view.context, R.color.like_color_enabled) else ContextCompat.getColor(view.context, R.color.like_color_disabled)
        like.setColorFilter(id, PorterDuff.Mode.MULTIPLY)
    }
}
