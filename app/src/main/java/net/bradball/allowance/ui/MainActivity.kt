package net.bradball.allowance.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHost
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.Ledger.LedgerFragment
import net.bradball.allowance.util.fabMenu.FabMenuActivity
import net.bradball.allowance.util.fabMenu.IHasFabMenu


class MainActivity : FabMenuActivity(), KidListFragment.OnFragmentInteractionListener, LedgerFragment.onLedgerInteraction {

    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var toolbar: Toolbar

    private val fragmentCallbacks = object: FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is IHasFabMenu) {
                showFabMenu()
            } else {
                hideFabMenu()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = test_toolbar
                toolbar.title = "Title"

        bottomAppBar = bottom_app_bar

        setupNavigation()

        setFabMenuAnchor(fab)
        setFabMenuOverlay(fab_menu_overlay)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)

    }

    fun showFabMenu() {
        fab.show()
    }

    fun hideFabMenu() {
        fab.hide()
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.fragment_nav_host).navigateUp()

    override fun onFragmentInteraction() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupNavigation() {
        val navController = findNavController(this, R.id.fragment_nav_host)
        //setupActionBarWithNavController(this as AppCompatActivity, navController)

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController)


        //setSupportActionBar(bottomAppBar)
        //bottomAppBar.setupWithNavController(navController)
    }
}
