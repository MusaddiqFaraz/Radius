package com.faraz.app.radius.ui

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.faraz.app.radius.R
import com.faraz.app.radius.data_manager.RxBus
import com.faraz.app.radius.data_manager.RxClickEvent
import com.faraz.app.radius.data_manager.api.Exclusion
import com.faraz.app.radius.data_manager.api.FacilitiesAndOptions
import com.faraz.app.radius.data_manager.api.Facility
import com.faraz.app.radius.data_manager.api.Option
import com.faraz.app.radius.extensions.KotlinRVAdapter
import kotlinx.android.synthetic.main.item_facility.view.*
import kotlinx.android.synthetic.main.item_options.view.*

/**
 * Created by root on 25/8/18.
 */
class BaseVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    private  var optionsAdapter: KotlinRVAdapter<Option, BaseVH> ? = null
    fun bindFacility(facility: FacilitiesAndOptions) {

        with(itemView) {




            llFacility.visibility = View.VISIBLE
            tvFacility.text = facility.facility?.name
            rvOptions.layoutManager = GridLayoutManager(context, 3)

//
                optionsAdapter = KotlinRVAdapter(context,
                        R.layout.item_options, { it ->
                    BaseVH(it)
                }, { holder, item, position ->
                    holder.bindOptions(item,adapterPosition)
                }, facility.options.toMutableList())

                rvOptions.adapter = optionsAdapter
                optionsAdapter?.notifyDataSetChanged()





        }
    }




     fun bindOptions(option: Option, facilityPos:Int,showFacility:Boolean = false) {
        with(itemView) {

            //only to show selected facility with options after user is done selecting options
            if(showFacility) {
                tvSelectedFacility.visibility = View.VISIBLE
                tvSelectedFacility.text = "Selected: ${option.facilityName}"

            }


            tvOption.text = option.name

           val color = when {
               option.disabled -> ContextCompat.getColor(context,R.color.disabled)
               option.isSelected -> ContextCompat.getColor(context,R.color.colorPrimary)
               else -> ContextCompat.getColor(context,R.color.gray)
           }
            ivOption.setColorFilter(color,PorterDuff.Mode.SRC_ATOP)



            tvOption.setTextColor(color)
            val name = if(option.icon == "no-room") "no_room" else option.icon
            val id = context.resources.getIdentifier(name+"2x","drawable",context.packageName)
            ivOption.setImageResource(id)
            llOption.setOnClickListener {
                if(!option.disabled && !showFacility) {
                    RxBus.sendSticky(RxClickEvent(facilityPos,adapterPosition,option.facilityId,option.id))
                }

            }
        }
    }

}