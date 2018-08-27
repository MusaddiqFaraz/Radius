package com.faraz.app.radius.ui

import android.arch.lifecycle.ViewModel
import com.faraz.app.radius.data_manager.FacilityRepo
import javax.inject.Inject

/**
 * Created by root on 24/8/18.
 */
class MainVM @Inject constructor(private val facilityRepo: FacilityRepo): ViewModel(){



    fun getFacilities() = facilityRepo.getFacilities()



}