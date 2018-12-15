package net.bradball.allowance.ui.KidList

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_kid_list.view.*
import kotlinx.android.synthetic.main.fragment_kid_list_card.view.*

import net.bradball.allowance.R
import net.bradball.allowance.di.ViewModelFactory
import net.bradball.allowance.ui.Ledger.LedgerFragment
import net.bradball.allowance.models.Kid
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.util.fabMenu.IHasFabMenu
import java.text.NumberFormat
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
\         * @return A new instance of fragment KidListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = KidListFragment()

        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Kid>() {
            override fun areItemsTheSame(oldItem: Kid, newItem: Kid): Boolean {
                return oldItem.recordId == newItem.recordId
            }

            override fun areContentsTheSame(oldItem: Kid, newItem: Kid): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }

    private val listAdapter = KidListAdapter()

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: KidListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kid_list, container, false)

        view.kid_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        view.kid_list.adapter = listAdapter

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KidListViewModel::class.java)
        viewModel.getKidList().observe(this, Observer<List<Kid>> { list -> listAdapter.submitList(list) })

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateFabMenu(menu: Menu, menuInflater: MenuInflater): Boolean {
        menuInflater.inflate(R.menu.kid_list_fab_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.fab_add_kid -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_kidListFragment_to_editKidFragment)
                true
            }
            R.id.fab_add_money -> {
                Toast.makeText(requireContext(), "Add Money Clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private inner class KidListAdapter internal constructor(): ListAdapter<Kid, KidListAdapter.ViewHolder>(DIFF_CALLBACK) {
        internal inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            private val nameView: TextView = itemView.kid_card_name
            private val spendMoneyView: TextView = itemView.kid_card_spend_money
            private val totalView: TextView = itemView.kid_card_total

            fun bindKid(kid: Kid) {

                val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance();

                nameView.text = kid.firstname
                spendMoneyView.text = currencyFormatter.format(kid.balance)
                totalView.text = currencyFormatter.format(kid.balance)

                itemView.setOnClickListener {
                    Navigation.findNavController(itemView).navigate(R.id.action_showLedger, LedgerFragment.getArgsBundle(kid.recordId, kid.firstname))
                    // Navigation.createNavigateOnClickListener(R.id.ledgerFragment, LedgerFragment.getArgsBundle(kid.id))
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_kid_list_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindKid(getItem(position))
        }

    }

}
