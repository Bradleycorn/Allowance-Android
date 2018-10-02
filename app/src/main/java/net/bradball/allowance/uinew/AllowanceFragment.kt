package net.bradball.allowance.ui

import androidx.appcompat.app.AppCompatActivity

import dagger.android.support.DaggerFragment

open class AllowanceFragment: DaggerFragment() {

    protected fun setToolbarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}