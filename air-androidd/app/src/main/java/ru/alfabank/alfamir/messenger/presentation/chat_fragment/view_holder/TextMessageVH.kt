package ru.alfabank.alfamir.messenger.presentation.chat_fragment.view_holder

import android.content.res.Resources
import android.text.Html
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.message_text_viewholder.view.*
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatAdapterContract

class TextMessageVH(view: View) : MessageVH(view), ChatAdapterContract.TextRow {

    private var tvText: TextView = view.message_text
    private val resources: Resources = view.context.resources
    private val hOffset = 72f + 12f + 12f + 12f //  message l+r margins + message padding
    private var maxWidth = 0
    private val constraintSet = ConstraintSet()

    init {
        maxWidth = (resources.configuration.screenWidthDp - hOffset).toDp()
        constraintSet.clone(layout)
    }

    override fun setText(text: String) {
        tvText.text = Html.fromHtml(text).trim()
        setDateStatusColor(false)
    }

    private fun Float.toDp(): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics).toInt()
    }
}
