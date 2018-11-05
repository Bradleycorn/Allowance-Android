package net.bradball.allowance.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.bradball.allowance.ui.EditKid.EditKidFragment
import net.bradball.allowance.ui.KidList.KidListFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeKidListFragment(): KidListFragment

    @ContributesAndroidInjector
    abstract fun contributeEditKidFragment(): EditKidFragment

}
