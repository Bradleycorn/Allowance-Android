package net.bradball.allowance.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.bradball.allowance.ui.MainActivity

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

}