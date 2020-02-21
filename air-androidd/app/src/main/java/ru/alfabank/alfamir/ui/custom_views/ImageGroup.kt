package ru.alfabank.alfamir.ui.custom_views

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.message_image_viewholder.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alfabank.alfamir.R


class ImageGroup(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {

    private lateinit var images: List<Bitmap>
    private var configuration: Configuration
    private val hOffset = 72f
    private val vOffset = 56f + 56f + 24f
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var listener: OnClickListener
    private var position: Int = -1
    private var maxWidth = 0f
    private var maxHeight = 0f

    init {
        inflate(context, R.layout.image_group, this)
        image_group.orientation = VERTICAL
        configuration = context.resources.configuration
        maxWidth = (configuration.screenWidthDp - hOffset)
        maxHeight = (configuration.screenHeightDp - vOffset) / 2
    }

    fun setOnClickListener(listener: OnClickListener, position: Int) {
        this.listener = listener
        this.position = position
    }

    fun removeOnClickListener() {
        listener = OnClickListener { }
    }

    fun setImages(images: List<Bitmap>) {
        this.images = images
        image_group.removeAllViews()

        setupViews()
    }

    @Suppress("DeferredResultUnused")
    private fun setupViews() {

        uiScope.launch {

            when (images.size) {
                0 -> {
                }

                1 -> {
                    val dimen = getDimensions(images[0])
                    image_group.addView(createContainer(dimen.first, dimen.second, images[0], 0))
                }

                2 -> {
                    val lLayout = LinearLayout(context)
                    val llParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    lLayout.orientation = HORIZONTAL
                    lLayout.layoutParams = llParams
                    for (image in images) {
                        lLayout.addView(createContainer(150f, 300f, image, images.indexOf(image)))
                    }
                    image_group.addView(lLayout)
                }

                3 -> {
                    val vLayout = LinearLayout(context)
                    val hLayout = LinearLayout(context)
                    val llParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    vLayout.orientation = VERTICAL
                    vLayout.layoutParams = llParams
                    hLayout.orientation = HORIZONTAL
                    hLayout.layoutParams = llParams

                    for (i in 0 until 3) {
                        val image = images[i]
                        if (i == 0) {
                            hLayout.addView(createContainer(150f, 300f, image, i))
                        } else {
                            vLayout.addView(createContainer(150f, 148f, image, i))
                        }
                    }
                    hLayout.addView(vLayout)
                    image_group.addView(hLayout)
                }

                else -> {
                    val vLayout = LinearLayout(context)
                    val llParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    vLayout.orientation = VERTICAL
                    vLayout.layoutParams = llParams
                    var newbieCounter = 0
                    for (i in 0 until 2) {
                        val hLayout = LinearLayout(context)
                        hLayout.orientation = HORIZONTAL
                        hLayout.layoutParams = llParams

                        for (j in 0 until 2) {
                            val image = images[j + newbieCounter]
                            hLayout.addView(createContainer(150f, 150f, image, j + newbieCounter))
                        }

                        newbieCounter += 2
                        vLayout.addView(hLayout)
                    }
                    image_group.addView(vLayout)
                }
            }
        }
    }

    private fun createContainer(width: Float, height: Float, image: Bitmap, position: Int): androidx.cardview.widget.CardView {
        return createContainer(width.toDp(), height.toDp(), image, position)
    }

    private fun createContainer(width: Int, height: Int, image: Bitmap, pos: Int): androidx.cardview.widget.CardView {
        val container = androidx.cardview.widget.CardView(context)
        val cLP = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = 4f.toDp()

        when (pos) {
            0 -> cLP.setMargins(margin, margin, margin, margin)
            1 -> {
                cLP.marginStart = 0
                cLP.marginEnd = margin
                cLP.topMargin = margin
                cLP.bottomMargin = margin
            }
            2 -> {
                cLP.marginEnd = margin
                cLP.topMargin = 0
                cLP.bottomMargin = margin
                if (images.size == 3) {
                    cLP.marginStart = 0
                } else {
                    cLP.marginStart = margin
                }
            }
            else -> {
                cLP.marginStart = 0
                cLP.marginEnd = margin
                cLP.topMargin = 0
                cLP.bottomMargin = margin
            }
        }
        container.layoutParams = cLP
        container.radius = 4f
        container.cardElevation = 0f

        val img = ImageView(context)
        img.adjustViewBounds = true
        img.layoutParams = LayoutParams(width, height)

        Glide.with(this).load(image).centerCrop().into(img)
        container.addView(img)
        container.setTag(R.id.TAG_POSITION, position)
        container.setTag(R.id.TAG_ITEM, images.indexOf(image))
        container.setOnClickListener(listener)
        return container
    }

    private fun getDimensions(bitmap: Bitmap?): Pair<Int, Int> {
        bitmap?.also {
            val imgHeight = bitmap.height
            val imgWidth = bitmap.width

            return if (imgHeight < imgWidth) {
                val height = maxWidth * imgHeight / imgWidth
                maxWidth.toDp() to height.toDp()
            } else {
                val width = maxHeight / configuration.screenHeightDp * configuration.screenWidthDp
                width.toDp() to maxHeight.toDp()
            }
        }
        return 150 to 300f.toDp()
    }

    private fun Float.toDp(): Int {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, r.displayMetrics).toInt()
    }
}
