package com.faraz.app.radius

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import com.faraz.app.radius.dependInject.AppComponent
import com.faraz.app.radius.dependInject.AppInjector
import com.faraz.app.radius.dependInject.AppModule
import com.faraz.app.radius.dependInject.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by root on 24/8/18.
 */
class Radius: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @set:VisibleForTesting
    lateinit var component: AppComponent

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
        AppInjector.init(this)
    }
}
val Context.component: AppComponent
    get() = (applicationContext as Radius).component

val Fragment.component: AppComponent
    get() = activity!!.component
