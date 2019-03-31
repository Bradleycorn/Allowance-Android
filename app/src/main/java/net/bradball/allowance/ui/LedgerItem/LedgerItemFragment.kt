package net.bradball.allowance.ui.LedgerItem


import android.content.Intent
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_ledger_item.view.*
import net.bradball.allowance.R
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.ui.DatePickerFragment
import net.bradball.allowance.util.empty
import java.util.*


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

    private lateinit var dateEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kidId = it.getString(ARG_KID_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ledger_item, container, false)
        dateEditText = view.ledger_item_date


        dateEditText.setOnClickListener {


            val dialog = DatePickerFragment.newInstance(Date())
            dialog.setTargetFragment(this, DATE_REQUEST)
            //dialog.show(fragmentManager, DATE_DIALOG)
        }

        return view
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        when (requestCode) {
//            DATE_REQUEST -> {
//                val selectedDate: Date = data.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
//                dateEditText.setText(selectedDate.toString())
//            }
//        }
//    }

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

        private const val DATE_DIALOG = "DateDialog"
        private const val DATE_REQUEST = 0

    }
}
