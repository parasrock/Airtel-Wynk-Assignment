package com.paras.baseapplication.base.di

import com.paras.baseapplication.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    /**
     * If we want any class scope only for MainActivity using Dagger,
     * we can make subcomponent and can get the class object using provide annotation
     */
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

}