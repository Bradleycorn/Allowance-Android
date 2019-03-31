package net.bradball.allowance.util

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.dialog_bottom_date_picker.view.*
import net.bradball.allowance.R
import java.time.LocalDate
import java.util.*

class BottomSheetDatePicker: BottomSheetDialogFragment() {

    companion object {
        private const val KEY_DATE = "date"

        fun newInstance(initialDate: LocalDate = LocalDate.now(), onClose: ((selectedDate: LocalDate) -> Unit)? = null): BottomSheetDatePicker {
            val picker = BottomSheetDatePicker()
            val args = Bundle()
            args.putString(KEY_DATE, initialDate.format(DATE_FORMAT.SHORT))

            picker.arguments = args
            picker.setOnCloseListener(onClose)

            return picker
        }
    }

    private lateinit var calendarView: DatePicker
    private lateinit var button: MaterialButton
    private var dismissHandler: ((LocalDate) -> Unit)? = null
    private lateinit var selectedDate: LocalDate

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_bottom_date_picker, container, false)

        button = view.bottom_date_picker_toolbar.bottom_date_picker_close
        button.setOnClickListener { dismiss() }

        selectedDate = getBirthDay()
        calendarView = view.bottom_date_picker
        calendarView.maxDate = Date().time
        calendarView.updateDate(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
        calendarView.setOnDateChangedListener { _, year, month, day ->
            selectedDate = LocalDate.of(year, month + 1, day)
        }

        return view
    }

    private fun getBirthDay(): LocalDate {
        return arguments?.getString(KEY_DATE)?.parseDate(DATE_FORMAT.SHORT) ?: LocalDate.now()
    }

    fun setOnCloseListener(onClose: ((LocalDate) -> Unit)?) {
        dismissHandler = onClose
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissHandler?.invoke(selectedDate)
    }
}