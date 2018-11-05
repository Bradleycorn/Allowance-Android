package net.bradball.allowance.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.bradball.allowance.AllowanceApp
import net.bradball.allowance.di.ActivityBuildersModule
import javax.inject.Singleton

/**
 * Dagger2 Component for allowing dynamic injection into our Activities, Fragments, ViewModels, etc.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuildersModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AllowanceApp): Builder

        fun build(): ApplicationComponent
    }

    fun inject(allowanceApp: AllowanceApp)
}
