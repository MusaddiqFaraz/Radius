package com.faraz.app.radius.dependInject

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.faraz.app.radius.Radius
import dagger.android.AndroidInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by root on 24/8/18.
 */
object AppInjector {

    fun init(radius: Radius) {


        radius.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }

        })
    }

    private fun handleInjection(activity: Activity){
        if (activity is HasSupportFragmentInjector)
            AndroidInjection.inject(activity)
    }

}