package net.bradball.allowance.util.fabMenu

import android.view.Menu
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

interface FabMenuContainer {
    fun onCreateFabMenu(fabMenu: Menu)
}

class BaseFragment: Fragment() {


    @CallSuper
    fun onCreateFabMenu(fabMenu: Menu) {
        this.fragmentManager?.
    }
}