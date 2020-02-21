package ru.alfabank.alfamir.poster.presentation

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.poster_fragment_toolbar.*
import kotlinx.android.synthetic.main.write_question_fragment.*
import ru.alfabank.alfamir.Constants.Poster.TEMP_STRING_KEY
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseFragment
import ru.alfabank.alfamir.utility.toBoolean
import javax.inject.Inject

class WriteQuestionFragment : BaseFragment() {

    @Inject
    lateinit var presenter: PosterPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.write_question_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            getString(TEMP_STRING_KEY)?.apply {
                edit_text.text = SpannableStringBuilder(this)
            }
        }
        toolbar.setOnClickListener { activity?.onBackPressed() }
        showKeyboard()
        setActualUserData()

        edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                text?.apply {
                    send_button.isActivated = trim().count() > 5
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                char_counter.text = edit_text.text.count().toString()
            }
        })

        anonymous_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                hideUserData()
            } else {
                setActualUserData()
            }
        }

        send_button.isActivated = !edit_text.text.isNullOrBlank()
        send_button.setOnClickListener {
            val mText = edit_text.text
            if (!mText.isNullOrBlank() && mText.count() > 5 && send_button.isActivated) {
                presenter.sendQuestion(edit_text.text.toString(), if (anonymous_checkbox.isChecked) 1 else 0)
                val message = if (PosterPresenter.poster.moderation!!.toBoolean()) R.string.question_will_appear else R.string.question_sent
                Snackbar.make(send_button, message, Snackbar.LENGTH_LONG).show()
                activity?.onBackPressed()
            } else {
                Snackbar.make(send_button, R.string.input_text_length_error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showKeyboard() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        edit_text.requestFocus()
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edit_text, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setActualUserData() {
        presenter.setUserPhoto(user_avatar)
        user_name.text = presenter.getUserName()
    }

    private fun hideUserData() {
        user_name.text = getString(R.string.anonymous_name)
        user_avatar.setImageResource(R.drawable.ic_avatar_default)
    }
}