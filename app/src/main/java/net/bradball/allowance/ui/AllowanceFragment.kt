package net.bradball.allowance.ui

import androidx.appcompat.app.AppCompatActivity

import dagger.android.support.DaggerFragment
import net.bradball.allowance.di.ViewModelFactory
import javax.inject.Inject

open class AllowanceFragment: DaggerFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    protected fun setToolbarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

}