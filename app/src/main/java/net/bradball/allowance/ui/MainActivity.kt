package net.bradball.allowance.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.Ledger.LedgerFragment
import net.bradball.allowance.util.fabMenu.FabMenuActivity
import net.bradball.allowance.util.fabMenu.IHasFabMenu
import net.bradball.allowance.util.lazyView


class MainActivity : FabMenuActivity() {

    private val bottomAppBar by lazyView<BottomAppBar>(R.id.bottom_app_bar)
    private val toolbar by lazyView<Toolbar>(R.id.app_toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        setFabMenuAnchor(fab)
        setFabMenuOverlay(fab_menu_overlay)
    }

    private fun setupNavigation() {
        val navController = findNavController(this, R.id.fragment_nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
