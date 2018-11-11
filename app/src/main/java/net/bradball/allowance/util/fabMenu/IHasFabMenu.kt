package net.bradball.allowance.util.fabMenu


import android.view.Menu
import android.view.MenuInflater

const val DEFAULT_ANIMATION_DURATION = 200L

interface IHasFabMenu {
    fun onCreateFabMenu(menu: Menu, menuInflator: MenuInflater): Boolean = false
}