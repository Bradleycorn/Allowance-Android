package net.bradball.allowance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.Ledger.LedgerFragment

class MainActivity : DaggerAppCompatActivity(), KidListFragment.OnFragmentInteractionListener, LedgerFragment.onLedgerInteraction {

    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomAppBar = bottom_app_bar

        setupNavigation()
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