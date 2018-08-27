package com.faraz.app.radius.data_manager.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.faraz.app.radius.data_manager.api.Exclusion
import com.faraz.app.radius.data_manager.api.ExclusionList
import com.faraz.app.radius.data_manager.api.Facility
import com.faraz.app.radius.data_manager.api.Option

/**
 * Created by root on 23/8/18.
 */
@Database(entities = [Facility::class,Option::class,Exclusion::class,ExclusionList::class],version = 1)
@TypeConverters(ExclusionConverter::class)
abstract class RadiusDatabase: RoomDatabase() {

    abstract fun facilitiesDao(): FacilitiesDao
}