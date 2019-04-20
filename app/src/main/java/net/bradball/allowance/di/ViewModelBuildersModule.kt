package net.bradball.allowance.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.bradball.allowance.ui.editKid.EditKidViewModel

import net.bradball.allowance.ui.KidList.KidListViewModel
import net.bradball.allowance.ui.Ledger.LedgerViewModel
import net.bradball.allowance.ui.transaction.TransactionViewModel

@Suppress("unused")
@Module
abstract class ViewModelBuildersModule {

    @Binds
    @IntoMap
    @ViewModelKey(KidListViewModel::class)
    abstract fun bindKidListViewModel(kidListViewModel: KidListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditKidViewModel::class)
    abstract fun bindEditKidViewModel(editKidViewModel: EditKidViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel::class)
    abstract fun bindTransactionViewModel(transactionViewModel: TransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LedgerViewModel::class)
    abstract fun bindLedgerViewModel(kidListViewModel: LedgerViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
