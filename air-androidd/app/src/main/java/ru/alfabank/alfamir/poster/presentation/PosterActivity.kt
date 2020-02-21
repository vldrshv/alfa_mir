package ru.alfabank.alfamir.poster.presentation

import android.os.Bundle
import ru.alfabank.alfamir.Constants.Companion.FRAGMENT_TRANSITION_ADD
import ru.alfabank.alfamir.R
import ru.alfabank.alfamir.base_elements.BaseActivity
import ru.alfabank.alfamir.utility.static_utilities.ActivityUtils

class PosterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.place_holder_activity)

        val fragment = PosterFragment()
        fragment.arguments = intent.extras
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.contentFrame, FRAGMENT_TRANSITION_ADD, false)
    }

    override fun onResume() {
        super.onResume()
        if (!checkIfInitialized()) finish()
    }
}