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
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_kid_list.view.*
import kotlinx.android.synthetic.main.fragment_kid_list_card.view.*

import net.bradball.allowance.R
import net.bradball.allowance.models.Kid
import java.text.NumberFormat
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [KidListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [KidListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class KidListFragment : Fragment() {

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
    }

    private var listener: OnFragmentInteractionListener? = null
    private val kidList: ArrayList<Kid> = ArrayList()
    private val listAdapter  = KidListAdapter(kidList)

    private lateinit var viewModel: KidListViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(KidListViewModel::class.java)
        viewModel.getKidList().observe(this, Observer {
            kidList.clear()
            if (it != null) {
                kidList.addAll(it)
            }
            listAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kid_list, container, false)

        view.kid_list.layoutManager = LinearLayoutManager(activity)
        view.kid_list.adapter = listAdapter

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
        fun onFragmentInteraction(uri: Uri)
    }


    private inner class KidListAdapter internal constructor(private val kidList: ArrayList<Kid>): RecyclerView.Adapter<KidListAdapter.ViewHolder>() {

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameLabel: TextView = itemView.kid_name
            var balanceLabel: TextView = itemView.kid_balance

            init {
                itemView.setOnClickListener { /* showAddContactDialog(adapterPosition) */ }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_kid_list_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance();
            val (id, firstName, lastName) = kidList[position]
            holder.nameLabel.text = firstName
            holder.balanceLabel.text = currencyFormatter.format(kidList[position].currentBalance())
        }

        override fun getItemCount(): Int {
            return kidList.size
        }
    }

}
