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

    private lateinit var calendarView: DatePicker
    private lateinit var button: MaterialButton
    private var dismissHandler: ((Date) -> Unit)? = null
    private lateinit var selectedDate: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_bottom_date_picker, container, false)

        button = view.bottom_date_picker_toolbar.bottom_date_picker_close
        button.setOnClickListener { dismiss() }

        val calendar = getBirthDay()
        selectedDate = calendar.time
        calendarView = view.bottom_date_picker
        calendarView.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        calendarView.setOnDateChangedListener { _, year, month, day ->
            selectedDate = GregorianCalendar(year, month, day).time
        }

        return view
    }

    private fun getBirthDay(): GregorianCalendar {
        val birthDate = Date(arguments?.getLong(KEY_DATE) ?: DEFAULT_TIMESTAMP)
        val calendar = GregorianCalendar()
        calendar.time = birthDate
        return calendar
    }

    fun setOnCloseListener(onClose: ((Date) -> Unit)?) {
        dismissHandler = onClose
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        dismissHandler?.invoke(selectedDate)
    }
}