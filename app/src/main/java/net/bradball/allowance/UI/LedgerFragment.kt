package net.bradball.allowance.UI

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_ledger.view.*
import kotlinx.android.synthetic.main.fragment_ledger_list_item.view.*

import net.bradball.allowance.R
import net.bradball.allowance.models.LedgerEntry
import java.text.NumberFormat
import java.util.ArrayList

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_KID_ID = "kidId"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LedgerFragment.onLedgerInteraction] interface
 * to handle interaction events.
 * Use the [LedgerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LedgerFragment : Fragment() {
    private var kidId: String = ""
    private var listener: onLedgerInteraction? = null

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
        fun getArgsBundle(kidId: String) = Bundle().apply {
            putString(ARG_KID_ID, kidId)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            kidId = it.getString(ARG_KID_ID, "")
        }

        viewModel = ViewModelProviders.of(this).get(LedgerViewModel::class.java)
        viewModel.getLedger(kidId).observe(this, Observer {
            ledger.clear()
            if (it != null) {
                ledger.addAll(it)
            }
            listAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ledger, container, false)

        view.ledger_list.layoutManager = LinearLayoutManager(activity)
        view.ledger_list.adapter = listAdapter

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onLedgerInteraction) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement onLedgerInteraction")
        }

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

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
    interface onLedgerInteraction {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    private inner class LedgerAdapter internal constructor(private val ledger: ArrayList<LedgerEntry>): RecyclerView.Adapter<LedgerAdapter.ViewHolder>() {

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
