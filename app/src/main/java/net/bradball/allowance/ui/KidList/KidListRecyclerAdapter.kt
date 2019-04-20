package net.bradball.allowance.ui.KidList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_kid_list_card.view.*
import net.bradball.allowance.R
import net.bradball.allowance.models.Kid
import net.bradball.allowance.ui.Ledger.LedgerFragment
import java.text.NumberFormat

class KidListAdapter: ListAdapter<Kid, KidListViewHolder>(KidListDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KidListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_kid_list_card, parent, false)
        return KidListViewHolder(view)
    }

    override fun onBindViewHolder(holder: KidListViewHolder, position: Int) {
        holder.bindKid(getItem(position))
    }

}


class KidListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameView: TextView = itemView.kid_card_name
    private val spendMoneyView: TextView = itemView.kid_card_spend_money
    private val totalView: TextView = itemView.kid_card_total

    fun bindKid(kid: Kid) {

        val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance();

        nameView.text = kid.firstname
        spendMoneyView.text = currencyFormatter.format(kid.spendingBalance)
        totalView.text = currencyFormatter.format(kid.totalBalance)

        itemView.setOnClickListener {
            Navigation.findNavController(itemView).navigate(R.id.action_showLedger, LedgerFragment.getArgsBundle(kid.storeId!!, kid.firstname))
            // Navigation.createNavigateOnClickListener(R.id.ledgerFragment, LedgerFragment.getArgsBundle(kid.id))
        }

    }
}

private val KidListDiffUtil = object: DiffUtil.ItemCallback<Kid>() {
    override fun areItemsTheSame(oldItem: Kid, newItem: Kid): Boolean {
        return oldItem.storeId == newItem.storeId
    }

    override fun areContentsTheSame(oldItem: Kid, newItem: Kid): Boolean {
        return oldItem == newItem
    }
}
