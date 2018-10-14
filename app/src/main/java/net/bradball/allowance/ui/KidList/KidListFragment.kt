package net.bradball.allowance.ui.KidList

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_kid_list.view.*
import kotlinx.android.synthetic.main.fragment_kid_list_card.view.*

import net.bradball.allowance.R
import net.bradball.allowance.di.ViewModelFactory
import net.bradball.allowance.ui.Ledger.LedgerFragment
import net.bradball.allowance.models.Kid
import net.bradball.allowance.ui.AllowanceFragment
import java.text.NumberFormat
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [KidListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [KidListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class KidListFragment : AllowanceFragment() {

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

    private var listener: OnFragmentInteractionListener? = null
    private val listAdapter = KidListAdapter()

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: KidListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kid_list, container, false)

        val fab = view.kid_list_fab_add_kid as FloatingActionButton

        view.kid_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        view.kid_list.adapter = listAdapter

        view.

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KidListViewModel::class.java)
        viewModel.getKidList().observe(this, Observer<List<Kid>> { list -> listAdapter.submitList(list) })

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction()
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
