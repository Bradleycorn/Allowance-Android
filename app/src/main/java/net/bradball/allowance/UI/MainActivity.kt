package net.bradball.allowance.UI

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import net.bradball.allowance.R

class MainActivity : AppCompatActivity(), KidListFragment.OnFragmentInteractionListener, LedgerFragment.onLedgerInteraction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_nav_host).navigateUp()

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment_nav_host)
        setupActionBarWithNavController(navController)
    }

}
