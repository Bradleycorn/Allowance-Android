package net.bradball.allowance.util

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.dialog_bottom_date_picker.view.*
import net.bradball.allowance.R
import java.time.LocalDate
import java.util.*

class BottomSheetDatePicker: BottomSheetDialogFragment() {

    companion object {
        private const val KEY_DATE = "date"
        private val DEFAULT_TIMESTAMP: Long by lazy {
            Date().time
        }

        fun newInstance(initialDate: Date? = null, onClose: ((selectedDate: Date) -> Unit)? = null): BottomSheetDatePicker {
            val picker = BottomSheetDatePicker()
            val args = Bundle()
            args.putLong(KEY_DATE, initialDate?.time ?: DEFAULT_TIMESTAMP)

            picker.arguments = args
            picker.setOnCloseListener(onClose)

            return picker
        }
    }

    private lateinit var calendarView: CalendarView
    private lateinit var button: MaterialButton
    private var dismissHandler: ((Date) -> Unit)? = null
    private lateinit var selectedDate: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_bottom_date_picker, container, false)

        calendarView = view.bottom_date_picker
        calendarView.setDate(arguments?.getLong(KEY_DATE) ?: DEFAULT_TIMESTAMP, true, true)
        selectedDate = Date(calendarView.date)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedDate = GregorianCalendar(year, month, dayOfMonth).time
        }

        button = view.bottom_date_picker_toolbar.bottom_date_picker_close
        button.setOnClickListener { dismiss() }

        return view
    }

    fun setOnCloseListener(onClose: ((Date) -> Unit)?) {
        dismissHandler = onClose
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        dismissHandler?.invoke(selectedDate)
    }
}