package net.bradball.allowance.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import net.bradball.allowance.AllowanceApp
import net.bradball.allowance.data.store.IDataStore
import net.bradball.allowance.data.store.firebase.FirebaseDataStore
import javax.inject.Singleton

@Module(includes = [ViewModelBuildersModule::class])
open class AppModule {

    @Provides
    open fun provideApp(allowanceApp: AllowanceApp): Application = allowanceApp

    @Provides
    open fun provideContext(allowanceApp: AllowanceApp): Context = allowanceApp.applicationContext

    @Singleton
    @Provides
    open fun provideDataStore(dataStore: FirebaseDataStore): IDataStore = dataStore

//    @Provides
//    open fun providesAppExecutors(): AppExecutors {
//        return AppExecutors.instance
//    }

//    @Singleton
//    @Provides
//    open fun providesAppDatabase(app: AllowanceApp): IAppDatabase {
//        return Room.databaseBuilder(app, AppDatabase::class.java, "AppDB").build()
//    }

//    @Provides
//    open fun provideCdiApi(volleyCdiApi: VolleyCdiApi): ICdiApi = volleyCdiApi

}