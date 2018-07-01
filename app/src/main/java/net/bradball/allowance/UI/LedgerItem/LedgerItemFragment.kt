package net.bradball.allowance.UI.LedgerItem


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.bradball.allowance.R
import net.bradball.allowance.UI.AllowanceFragment
import net.bradball.allowance.util.empty


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_KID_ID = "kidId"

/**
 * A simple [Fragment] subclass.
 * Use the [LedgerItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LedgerItemFragment : AllowanceFragment() {
    private var kidId: String = String.empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kidId = it.getString(ARG_KID_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ledger_item, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param kidId Parameter 1.
         * @return A new instance of fragment LedgerFragment.
         */
        @JvmStatic
        fun getArgsBundle(kidId: String) = Bundle().apply {
            putString(ARG_KID_ID, kidId)
        }

    }
}
