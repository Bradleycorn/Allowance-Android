package net.bradball.allowance.ui.KidList

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import net.bradball.allowance.R
import net.bradball.allowance.di.ViewModelFactory
import net.bradball.allowance.models.Kid
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.util.empty
import net.bradball.allowance.util.fabMenu.IHasFabMenu
import net.bradball.allowance.util.lazyNavController
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 * Use the [KidListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class KidListFragment : AllowanceFragment(), IHasFabMenu {

    companion object {
        @JvmStatic
        fun newInstance() = KidListFragment()
    }

    private lateinit var kidListView: RecyclerView
    private val listAdapter = KidListAdapter()

    private val viewModel by viewModels<KidListViewModel> { viewModelFactory }
    private val navController by lazyNavController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_kid_list, container, false)

        kidListView = view.findViewById(R.id.kid_list)

        kidListView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        kidListView.adapter = listAdapter

        viewModel.getKidList().observe(this, Observer<List<Kid>> { list ->
            listAdapter.submitList(list)
            kid = list[0].storeId ?: String.empty
        })

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateFabMenu(menu: Menu, menuInflater: MenuInflater): Boolean {
        menuInflater.inflate(R.menu.kid_list_fab_menu, menu)
        return true
    }


    private var kid = String.empty
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fab_add_kid -> {
                val action = KidListFragmentDirections.actionKidListFragmentToEditKidFragment(kid, "Edit Kid")
                navController.navigate(action)
                true
            }
            R.id.fab_add_money -> {
                Toast.makeText(requireContext(), "Add Money Clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




}
