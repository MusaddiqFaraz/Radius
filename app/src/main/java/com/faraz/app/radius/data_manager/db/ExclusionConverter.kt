package com.faraz.app.radius.data_manager.db

import android.arch.persistence.room.TypeConverter
import com.faraz.app.radius.data_manager.api.Exclusion
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList
import com.google.gson.Gson
import java.util.*


/**
 * Created by root on 24/8/18.
 */
class ExclusionConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Exclusion> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Exclusion>>() {

        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Exclusion>): String {
        return gson.toJson(someObjects)
    }
}