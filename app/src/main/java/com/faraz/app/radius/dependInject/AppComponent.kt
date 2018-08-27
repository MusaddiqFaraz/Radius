package com.faraz.app.radius.dependInject

import com.faraz.app.radius.Radius
import com.faraz.app.radius.ui.MainVM
import dagger.Component
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by root on 24/8/18.
 */
@Singleton
@Component(modules = [AppModule::class,ActivityModule::class,AndroidSupportInjectionModule::class])
interface AppComponent {

    val mainVM: MainVM

    fun inject(radius: Radius)
}