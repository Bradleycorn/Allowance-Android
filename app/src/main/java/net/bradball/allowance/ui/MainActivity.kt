package net.bradball.allowance.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
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
import java.util.regex.Pattern


class MainActivity : FabMenuActivity(), ToolbarContainer {

    private val bottomAppBar by lazyView<BottomAppBar>(R.id.bottom_app_bar)
    private val toolbar by lazyView<Toolbar>(R.id.app_toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        setFabMenuAnchor(fab)
        setFabMenuOverlay(fab_menu_overlay)
    }

    // Implement ToolbarContainer
    override var toolbarTitle: String?
        get() = toolbar.title.toString()
        set(value) { toolbar.title = value }

    private fun setupNavigation() {
        val navController = findNavController(this, R.id.fragment_nav_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(bottomAppBar)
//        NavigationUI.setupWithNavController(toolbar, navController)
        bottomAppBar.setupWithNavController(navController, appBarConfiguration)

        // This is pretty much stolen from AbstractAppBarOnDestinationChangedListener.java
        // which is what NavigationUI.setupWithNavController(toolbar, navController) uses.
        // However, that class also sets up the up arrow handling, which we don't want.
        // So we've extracted out only the bits that matter to us, setting the title on our
        // top toolbar.
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val label = destination.label
            if (!label.isNullOrBlank()) {
                // Fill in the data pattern with the args to build a valid URI
                val title = StringBuffer()
                val fillInPattern = Pattern.compile("\\{(.+?)\\}")
                val matcher = fillInPattern.matcher(label)
                while (matcher.find()) {
                    val argName = matcher.group(1)
                    if (arguments?.containsKey(argName) == true) {
                        matcher.appendReplacement(title, "")
                        title.append(arguments.get(argName)!!.toString())
                    } else {
                        Log.w(TAG, "Could not find argument named $argName to set the title")
                    }
                }
                matcher.appendTail(title)
                toolbarTitle = title.toString()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
