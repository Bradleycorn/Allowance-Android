package net.bradball.allowance.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.Ledger.LedgerFragment
import net.bradball.allowance.util.fabMenu.FabMenuActivity


class MainActivity : FabMenuActivity(), KidListFragment.OnFragmentInteractionListener, LedgerFragment.onLedgerInteraction {

    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBar = bottom_app_bar

        setupNavigation()

        setFabMenuAnchor(fab)
        setFabMenuOverlay(fab_menu_overlay)
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

        setSupportActionBar(bottomAppBar)
        bottomAppBar.setupWithNavController(navController)
    }
}
