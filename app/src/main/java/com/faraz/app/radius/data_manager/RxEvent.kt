package com.faraz.app.radius.data_manager

import android.app.VoiceInteractor
import com.faraz.app.radius.data_manager.api.Option

/**
 * Created by root on 26/8/18.
 */
data class RxClickEvent(var facilityPos:Int,var optionPos:Int,var facilityId:String,var optionId:String)

data class RxFilterEvent(var options:ArrayList<Option>)