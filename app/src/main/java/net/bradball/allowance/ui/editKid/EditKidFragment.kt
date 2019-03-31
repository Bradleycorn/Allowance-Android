package net.bradball.allowance.ui.editKid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText

import net.bradball.allowance.R
import net.bradball.allowance.ui.AllowanceFragment
import net.bradball.allowance.util.BottomSheetDatePicker
import net.bradball.allowance.util.getValue
import net.bradball.allowance.databinding.FragmentEditKidBinding

class EditKidFragment : AllowanceFragment() {

    private val viewModel by viewModels<EditKidViewModel> { viewModelFactory }

    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var birthDate: TextInputEditText
    private lateinit var image: ImageView

    private lateinit var binding: FragmentEditKidBinding

    private val args by navArgs<EditKidFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_edit_kid, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        firstName = view.findViewById(R.id.edit_kid_first_name)
        lastName = view.findViewById(R.id.edit_kid_last_name)
        birthDate = view.findViewById(R.id.edit_kid_birthdate)
        image = view.findViewById(R.id.edit_kid_image)

        birthDate.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { showDatePicker() } }
        birthDate.setOnClickListener { showDatePicker() }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadKid(args.kidId)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun showDatePicker() {
        val picker = BottomSheetDatePicker.newInstance(viewModel.parseBirthDate(birthDate.getValue())) { selectedDate ->
            viewModel.onBirthdateSelected(selectedDate)
        }
        picker.show(childFragmentManager, "EditKidFragment")
    }

}
