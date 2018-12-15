package net.bradball.allowance.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import kotlinx.android.synthetic.main.fragment_date_picker.view.*
import net.bradball.allowance.R
import java.util.*

class DatePickerFragment: androidx.fragment.app.DialogFragment() {
    companion object {
        const val EXTRA_DATE = "net.bradball.android.allowance.date"
        private const val DATE_ARG = "date"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(DATE_ARG, date)

            val datePickerFragment = DatePickerFragment()
            datePickerFragment.arguments = args
            return datePickerFragment
        }
    }

    private lateinit var datePicker: DatePicker


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date: Date = arguments?.getSerializable(DATE_ARG) as Date

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_date_picker, null)

        datePicker = view.date_picker

        val calendar = Calendar.getInstance()
        calendar.time = date

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null)

        val alertDialog = AlertDialog.Builder(activity)
                .setView(view)
                //.setTitle(R.string.date_picker_title)

        alertDialog.setPositiveButton(android.R.string.ok) { dialog, which ->
            val pickedDate = GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth).time
            sendResult(Activity.RESULT_OK, pickedDate)
        }

        return alertDialog.create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        targetFragment?.let {
            val intent = Intent()
            intent.putExtra(EXTRA_DATE, date)

            it.onActivityResult(targetRequestCode, resultCode, intent)
        }
    }

}