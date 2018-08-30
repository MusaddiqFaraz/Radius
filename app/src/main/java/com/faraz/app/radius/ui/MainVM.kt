package com.faraz.app.radius.ui

import android.arch.lifecycle.ViewModel
import com.faraz.app.radius.data_manager.FacilityRepo
import com.faraz.app.radius.data_manager.api.FacilitiesAndOptions
import com.faraz.app.radius.data_manager.api.FacilitiesDbResponse
import com.faraz.app.radius.data_manager.api.Resource
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by root on 24/8/18.
 */
class MainVM @Inject constructor(private val facilityRepo: FacilityRepo): ViewModel(){


    private var facilitiesObservable:Observable<Resource<FacilitiesDbResponse>>? = null

    fun getFacilities(): Observable<Resource<FacilitiesDbResponse>>?  {
        if(facilitiesObservable == null)
            facilitiesObservable = facilityRepo.getFacilities()
        return facilitiesObservable
    }



}