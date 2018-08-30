package com.faraz.app.radius.data_manager.api

import android.arch.persistence.room.*
import com.faraz.app.radius.data_manager.db.ExclusionConverter
import com.google.gson.annotations.SerializedName

/**
 * Created by root on 23/8/18.
 */
data class FacilityResponse(
        @SerializedName("facilities") val facilities: List<Facility>,
        @SerializedName("exclusions") val exclusions: List<List<Exclusion>>
)

data class FacilitiesDbResponse(
        val facilitiesAndOptions: List<FacilitiesAndOptions>,
        val exclusions: List<ExclusionList>
)

@Entity
data class ExclusionList (@PrimaryKey(autoGenerate = true) var id: Int,
                          @TypeConverters(ExclusionConverter::class) var exclusions: List<Exclusion>)


data class FacilitiesAndOptions(
    @Embedded
    var facility: Facility? = null,
    @Relation(parentColumn = "facilityId",entityColumn = "facilityId")
    var options: List<Option> =  ArrayList()){

    constructor() : this(null, emptyList())
}

@Entity
data class Facility(
        @PrimaryKey @SerializedName("facility_id") var facilityId: String,
        @SerializedName("name") var name: String,
        var createdTime: Long,
        @Ignore @SerializedName("options") val options: List<Option> = emptyList()
){
    constructor() : this("","",-1)
}

@Entity(foreignKeys = [(ForeignKey(entity = Facility::class,parentColumns = ["facilityId"],childColumns = ["facilityId"]))])
data class Option(
        @SerializedName("name") var name: String = "",
        @SerializedName("icon") var icon: String = "",
        @PrimaryKey @SerializedName("id") var id: String = "",
        var facilityId: String = "",
        var createdTime:Long= 0,
        @Ignore var isSelected:Boolean= false,
        @Ignore var disabled: Boolean = false
)

@Entity(foreignKeys = [
    ForeignKey(
            entity = Facility::class,parentColumns =["facilityId"] ,
    childColumns = ["facilityId"]
    ),
    ForeignKey(
            entity = Option::class,
                    parentColumns = ["id"],
    childColumns = ["optionId"])])
data class Exclusion(
        @PrimaryKey @SerializedName("facility_id") var facilityId: String,
        @SerializedName("options_id") var optionId: String,
        var createdTime: Long
) {
    override fun equals(other: Any?): Boolean {
        if(other is Exclusion){
            return other.facilityId == this.facilityId && other.optionId == this.optionId
        }
        return false
    }

    override fun hashCode(): Int {
        val prime = 23
        return (prime + facilityId.hashCode() + optionId.hashCode())

    }
}

