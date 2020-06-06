package com.paras.baseapplication.application

import com.paras.baseapplication.base.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication : DaggerApplication() {

    // region application override method

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    // endregion

    // region Dagger override method

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this).build()
    }

    // endregion

    // region companion block for static variables

    companion object {
        lateinit var instance: BaseApplication
    }

    // endregion

}