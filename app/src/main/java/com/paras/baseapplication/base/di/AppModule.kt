package com.paras.baseapplication.base.di

import android.app.Application
import android.content.Context
import com.paras.baseapplication.base.annotations.ApplicationContext
import dagger.Binds
import dagger.Module

/**
 * App has some modules that can be accessible inside the whole application anywhere.
 * Just for classification, otherwise we can have this method on AppComponent also.
 */
@Module(includes = [ViewModelModule::class])
abstract class AppModule {

    @Binds
    @ApplicationContext
    abstract fun providesContext(application: Application): Context

}