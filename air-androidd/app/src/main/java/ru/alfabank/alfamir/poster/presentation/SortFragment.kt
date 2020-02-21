package ru.alfabank.alfamir.poster.presentation

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sort_fragment.*
import ru.alfabank.alfamir.Constants.QuestionSortType.POPULAR
import ru.alfabank.alfamir.Constants.QuestionSortType.RECENT
import ru.alfabank.alfamir.R

class SortFragment : BottomSheetDialogFragment() {

    lateinit var presenter: PosterPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sort_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_button.setOnClickListener {
            this.dismiss()
        }
        sort_popular.setOnClickListener {
            presenter.applySort(POPULAR)
            this.dismiss()
        }
        sort_new.setOnClickListener {
            presenter.applySort(RECENT)
            this.dismiss()
        }
    }

    fun setPresenter(presenter: PosterPresenter): SortFragment {
        this.presenter = presenter
        return this
    }
}