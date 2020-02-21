package ru.alfabank.alfamir.messenger.presentation.dto

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chooser_fragment.*
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatPresenter
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.contract.ChatContract

class ChooserFragment : BottomSheetDialogFragment() {

    lateinit var presenter: ChatPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chooser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gallery.setOnClickListener{
            presenter.choosePhoto()
            dismiss()
        }
        camera.setOnClickListener {
            presenter.takePhoto()
            dismiss()
        }
        file.setOnClickListener {
            presenter.chooseFile()
            dismiss()
        }
    }

    fun setPresenter(presenter: ChatContract.Presenter): ChooserFragment {
        this.presenter = presenter as ChatPresenter
        return this
    }

}