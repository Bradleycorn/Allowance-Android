package net.bradball.allowance.util.fabMenu

import net.bradball.allowance.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import net.bradball.allowance.util.fabMenu.patterns.VerticalCoordinatorPattern
import net.bradball.allowance.util.dpToPx

const val DEFAULT_ANIMATION_DURATION = 200L

interface IHasFabMenu {
    fun onCreateFabMenu(menu: Menu, menuInflator: MenuInflater): Boolean = false
}



