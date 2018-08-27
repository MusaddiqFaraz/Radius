package com.faraz.app.radius.dependInject

import com.faraz.app.radius.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by root on 24/8/18.
 */
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun getMainActivity(): MainActivity
}