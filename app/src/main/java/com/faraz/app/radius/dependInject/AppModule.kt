package com.faraz.app.radius.dependInject

import android.arch.persistence.room.Room
import android.content.Context
import com.faraz.app.radius.data_manager.AppRxSchedulers
import com.faraz.app.radius.data_manager.api.ApiInterface
import com.faraz.app.radius.data_manager.api.RetrofitFactory
import com.faraz.app.radius.data_manager.api.RetrofitFactory.getRetrofitClient
import com.faraz.app.radius.data_manager.db.FacilitiesDao
import com.faraz.app.radius.data_manager.db.RadiusDatabase
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by root on 24/8/18.
 */
@Module
class AppModule(private val  context: Context) {

    @Singleton
    @Provides
    fun providesAppContext() = context

    @Singleton
    @Provides
    fun provideRxSchedulers() : AppRxSchedulers = AppRxSchedulers(
            io = Schedulers.io(),
            computation = Schedulers.computation(),
            main = AndroidSchedulers.mainThread()
    )

    @Singleton
    @Provides
    fun providesDB(context: Context): RadiusDatabase = Room.databaseBuilder(context, RadiusDatabase::class.java,"RadiusDB").build()


    @Singleton
    @Provides
    fun providesFacilityDao(radiusDatabase: RadiusDatabase): FacilitiesDao= radiusDatabase.facilitiesDao()

    @Provides
    fun getNetworkModule() : ApiInterface = getRetrofitClient(RetrofitFactory.BASE_URL)

}