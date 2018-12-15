package net.bradball.allowance.ui.Ledger

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_ledger.view.*
import kotlinx.android.synthetic.main.fragment_ledger_list_item.view.*

import net.bradball.allowance.R
import net.bradball.allowance.di.ViewModelFactory
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.models.LedgerEntry
import net.bradball.allowance.util.empty
import java.text.NumberFormat
import java.util.ArrayList
import javax.inject.Inject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_KID_ID = "kidId"
private const val ARG_KID_NAME = "kidName"

/**
 * A simple [Fragment] subclass.
 *
 * Use the [LedgerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LedgerFragment : AllowanceFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    private var kidId: String = ""

    private lateinit var viewModel: LedgerViewModel
    private val ledger: ArrayList<LedgerEntry> = ArrayList()
    private val listAdapter  = LedgerAdapter(ledger)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param kidId Parameter 1.
         * @return A new instance of fragment LedgerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun getArgsBundle(kidId: String, kidName: String) = Bundle().apply {
            putString(ARG_KID_ID, kidId)
            putString(ARG_KID_NAME, kidName)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            kidId = it.getString(ARG_KID_ID, String.empty)
            setToolbarTitle(it.getString(ARG_KID_NAME))
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LedgerViewModel::class.java)
        viewModel.getLedger(kidId).observe(this, Observer {
            ledger.clear()
            if (it != null) {
                ledger.addAll(it)
            }
            listAdapter.notifyDataSetChanged()
        })

        viewModel.getKid(kidId).observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it?.firstname
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ledger, container, false)

        view.ledger_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        view.ledger_list.adapter = listAdapter

        return view
    }

    private inner class LedgerAdapter internal constructor(private val ledger: ArrayList<LedgerEntry>): androidx.recyclerview.widget.RecyclerView.Adapter<LedgerAdapter.ViewHolder>() {

        internal inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            var descriptionLabel: TextView = itemView.ledger_item_description
            var creditLabel: TextView = itemView.ledger_item_credit
            var debitLabel: TextView = itemView.ledger_item_debit

            init {
                itemView.setOnClickListener { /* showAddContactDialog(adapterPosition) */ }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_ledger_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currencyFormatter: NumberFormat = NumberFormat.getNumberInstance()
            currencyFormatter.minimumFractionDigits = 2
            currencyFormatter.maximumFractionDigits = 2
            currencyFormatter.minimumIntegerDigits = 1
            val (id, kid, date, description, amount) = ledger[position]
            holder.descriptionLabel.text = description
            if (amount >= 0) {
                holder.creditLabel.text = currencyFormatter.format(amount)
            } else {
                holder.debitLabel.text = currencyFormatter.format(amount)
            }
        }

        override fun getItemCount(): Int {
            return ledger.size
        }
    }

}
