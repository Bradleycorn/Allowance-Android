package net.bradball.allowance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.internal.NavigationMenu
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.Ledger.LedgerFragment
import com.google.android.material.internal.NavigationSubMenu
import androidx.appcompat.view.menu.SubMenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import android.view.SubMenu
import androidx.appcompat.view.menu.MenuBuilder
import android.R
import android.content.Context
import android.widget.PopupMenu
import androidx.core.view.MenuCompat
import androidx.fragment.app.FragmentController


class MainActivity : DaggerAppCompatActivity(), KidListFragment.OnFragmentInteractionListener, LedgerFragment.onLedgerInteraction {

    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBar = bottom_app_bar

        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.fragment_nav_host).navigateUp()

    override fun onFragmentInteraction() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupNavigation() {
        val navController = findNavController(this, R.id.fragment_nav_host)
        //setupActionBarWithNavController(this as AppCompatActivity, navController)

        setSupportActionBar(bottomAppBar)
        bottomAppBar.setupWithNavController(navController)
    }

    private fun createFabMenu {
        val menu = PopupMenu(this, null).menu
        val fabMenu = NavigationMenu(fab?.context)

        supportFragmentManager.

        activity.menuInflater.inflate(menuId, fabMenu)
        val menuSize = fabMenu.size()
    }
}

interface FabMenu {
    fun onCreateFabMenu(menu: Menu): Unit {

    }
}

Fab

class NavigationMenu(context: Context) : MenuBuilder(context) {

    override fun addSubMenu(group: Int, id: Int, categoryOrder: Int, title: CharSequence): SubMenu {
        val item = addInternal(group, id, categoryOrder, title) as MenuItemImpl
        val subMenu = NavigationSubMenu(context, this, item)
        item.setSubMenu(subMenu)
        return subMenu
    }
}

class FabMenuActivity: DaggerAppCompatActivity() {

}