package net.bradball.allowance.UI

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

open class AllowanceFragment: Fragment() {

    protected fun setToolbarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}