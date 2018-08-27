package com.faraz.app.radius.data_manager.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.util.Log
import com.faraz.app.radius.data_manager.api.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by root on 23/8/18.
 */
@Dao
abstract class FacilitiesDao {

    @Query("SELECT * FROM Facility")
    abstract fun getFacilities() : Single<List<FacilitiesAndOptions>>

    @Query("SELECT * FROM ExclusionList")
    abstract fun getExclusions() : Single<List<ExclusionList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFacilities(facilities: Array<Facility>): Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOptions(options: Array<Option>): Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertExclusions(exclusions: Array<ExclusionList>): Array<Long>

    fun getResponse():Observable<Resource<FacilitiesDbResponse>> {
      return  Observable.zip<List<FacilitiesAndOptions>,List<ExclusionList>,Resource<FacilitiesDbResponse>>(
                getFacilities().toObservable()
                        .onErrorResumeNext(Function {
                            Observable.just(ArrayList())
                        }),
                getExclusions().toObservable()
                        .onErrorResumeNext(Function {
                            Observable.just(ArrayList())
                        }), BiFunction { facilities, exclusions ->
          Log.d("FacilityDao","exclusions list ${exclusions}")
            Resource(TYPE.SUCCESS,Source.Database,FacilitiesDbResponse(facilities,exclusions))
        }
        ).subscribeOn(Schedulers.io())
    }


}