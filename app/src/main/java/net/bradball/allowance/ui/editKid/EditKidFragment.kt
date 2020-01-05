package net.bradball.allowance.ui.editKid

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText

import net.bradball.allowance.R
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.util.BottomSheetDatePicker
import net.bradball.allowance.util.getValue
import net.bradball.allowance.databinding.FragmentEditKidBinding
import net.bradball.allowance.util.currencyEditText.CurrencyEditText
import net.bradball.allowance.util.getColorFromAttr
import java.time.LocalDate

class EditKidFragment : AllowanceFragment() {

    private val viewModel by viewModels<EditKidViewModel> { viewModelFactory }

    private lateinit var binding: FragmentEditKidBinding

    private lateinit var spendingBalance: CurrencyEditText
    private lateinit var savingsBalance: CurrencyEditText

    private val args by navArgs<EditKidFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
0
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_edit_kid, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        spendingBalance = view.findViewById(R.id.edit_kid_spending_balance)
        savingsBalance = view.findViewById(R.id.edit_kid_savings_balance)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.loadKid(args.kidId)

        viewModel.showBirthDatePicker.observe(viewLifecycleOwner, Observer { birthDate ->
           showDatePicker(birthDate)
        })

        viewModel.kidSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_kid, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.menu_edit_kid_save).icon.setTint(requireContext().getColorFromAttr(R.attr.colorSecondary))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit_kid_save -> viewModel.saveKid(spendingBalance.currencyValue, savingsBalance.currencyValue)
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePicker(date: LocalDate) {
        val picker = BottomSheetDatePicker.newInstance(date) { selectedDate ->
            viewModel.onBirthdateSelected(selectedDate)
        }
        picker.show(childFragmentManager, "EditKidFragment")
    }

}
