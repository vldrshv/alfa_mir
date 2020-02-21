package ru.alfabank.alfamir.poster.data

import ru.alfabank.alfamir.base_elements.BasePresenter
import ru.alfabank.alfamir.poster.data.dto.Poster
import ru.alfabank.alfamir.poster.presentation.PosterFragment

interface PosterFragmentContract {

    interface Presenter: BasePresenter<PosterFragment>

    interface Adapter{
        fun setData(poster: Poster)
    }
}