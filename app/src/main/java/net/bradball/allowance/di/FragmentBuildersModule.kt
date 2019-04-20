package net.bradball.allowance.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.bradball.allowance.ui.editKid.EditKidFragment
import net.bradball.allowance.ui.KidList.KidListFragment
import net.bradball.allowance.ui.transaction.TransactionFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeKidListFragment(): KidListFragment

    @ContributesAndroidInjector
    abstract fun contributeEditKidFragment(): EditKidFragment

    @ContributesAndroidInjector
    abstract fun contributeTransactionFragment(): TransactionFragment


}
