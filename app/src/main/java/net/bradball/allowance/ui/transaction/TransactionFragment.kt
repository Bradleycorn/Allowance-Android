package net.bradball.allowance.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.ChipGroup
import net.bradball.allowance.R
import net.bradball.allowance.databinding.FragmentEditKidBinding
import net.bradball.allowance.databinding.FragmentTransactionBinding
import net.bradball.allowance.ui.AllowanceFragment

class TransactionFragment: AllowanceFragment() {

    private val viewModel by viewModels<TransactionViewModel> { viewModelFactory }
    private val navArgs by navArgs<TransactionFragmentArgs>()
    private lateinit var binding: FragmentTransactionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val view = binding.root

        return view
    }

}