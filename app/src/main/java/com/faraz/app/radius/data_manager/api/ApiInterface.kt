package com.faraz.app.radius.data_manager.api

import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by root on 23/8/18.
 */
interface ApiInterface {
    @GET("iranjith4/ad-assignment/db")
    fun getFacilities(): Flowable<Response<FacilityResponse>>
}