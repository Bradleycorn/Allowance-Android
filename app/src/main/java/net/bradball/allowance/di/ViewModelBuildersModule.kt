package net.bradball.allowance.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.bradball.allowance.ui.KidList.KidListViewModel
import net.bradball.allowance.ui.Ledger.LedgerViewModel

@Suppress("unused")
@Module
abstract class ViewModelBuildersModule {
    @Binds
    @IntoMap
    @ViewModelKey(KidListViewModel::class)
    abstract fun bindKidListViewModel(kidListViewModel: KidListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LedgerViewModel::class)
    abstract fun bindLedgerViewModel(kidListViewModel: LedgerViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
