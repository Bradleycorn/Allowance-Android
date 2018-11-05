package net.bradball.allowance

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.bradball.allowance.di.DaggerApplicationComponent
import javax.inject.Inject

class AllowanceApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        // Am I red? If so, you probably need to generate the DI code.
        DaggerApplicationComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

    companion object {
        private val TAG = AllowanceApp::class.java.simpleName
    }
}