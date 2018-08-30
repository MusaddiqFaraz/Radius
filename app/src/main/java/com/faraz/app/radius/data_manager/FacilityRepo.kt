package com.faraz.app.radius.data_manager

import android.util.Log
import com.faraz.app.radius.data_manager.api.*
import com.faraz.app.radius.data_manager.db.FacilitiesDao
import com.faraz.app.radius.data_manager.db.RadiusDatabase
import io.reactivex.Observable
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by root on 23/8/18.
 */
class FacilityRepo @Inject constructor(private val radiusDatabase: RadiusDatabase,
                                       private val apiInterface: ApiInterface,
                                       private val appRxSchedulers: AppRxSchedulers) {

    //current time
    private val currentTime = Calendar.getInstance().timeInMillis
    //ttl is set to 24 hours, after that it'll load from network and save the new data
    private val ttl = 24 * 60 * 60 * 1000



    fun getFacilities(): Observable<Resource<FacilitiesDbResponse>> {
         return Observable.concat(radiusDatabase.facilitiesDao().getResponse(),
                getFacilitiesFromNetwork())
                 .filter {
                     if (it.data == null)
                         false
                     else
                         it.data.facilitiesAndOptions.isNotEmpty() && (currentTime - it.data.facilitiesAndOptions[0].facility!!.createdTime) < ttl
                 }

                 .first(Resource(TYPE.SUCCESS,Source.NETWORK,FacilitiesDbResponse(emptyList(), emptyList())))
                 .toObservable()
                 .observeOn(appRxSchedulers.main)

    }

    private fun getFacilitiesFromNetwork():Observable<Resource<FacilitiesDbResponse>> {
        return apiInterface.getFacilities().toObservable()

                .doAfterNext{
                    val response = it.body()
                    Log.d("FacilityRepo","exclusions list ${it.body()?.exclusions}")
                    response?.let {
                        radiusDatabase.runInTransaction {
                            val options = ArrayList<Option>()
                            val exclusions = ArrayList<ExclusionList>()
                            for (facility in it.facilities){
                                facility.createdTime = currentTime
                                for (option in facility.options) {
                                    option.createdTime= currentTime
                                    option.facilityId = facility.facilityId
                                    options.add(option)
                                }
                            }
                            for (i in 0 until it.exclusions.size)
                                exclusions.add(ExclusionList(i+1,it.exclusions[i]))
                            radiusDatabase.facilitiesDao().insertFacilities(it.facilities.toTypedArray())
                            radiusDatabase.facilitiesDao().insertOptions(options.toTypedArray())
                            radiusDatabase.facilitiesDao().insertExclusions(exclusions.toTypedArray())


                        }
                    }
                }.map {
                    val response = it.body()
                    val exclusions = ArrayList<ExclusionList>()
                    val facilities = ArrayList<FacilitiesAndOptions>()
                    response?.let {

                        for (i in 0 until it.exclusions.size)
                            exclusions.add(ExclusionList(i,it.exclusions[i]))

                        for (facility in it.facilities) {
                            facility.createdTime = currentTime
                            facilities.add(FacilitiesAndOptions(facility,facility.options))
                        }


                    }
                    Resource(TYPE.SUCCESS,Source.NETWORK,FacilitiesDbResponse(facilities,exclusions ?: emptyList() ))
                }
                .subscribeOn(appRxSchedulers.io)
    }
}