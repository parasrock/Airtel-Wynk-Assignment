package com.paras.baseapplication.base.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by parasj on 10/8/18.
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    // initialize viewModelFactory VIA Dagger

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}

/**
 * this method is responsible for creating or getting ViewModel for provided class T
 */
inline fun <reified T : ViewModel> BaseActivity.getViewModel(): T {
    return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
}