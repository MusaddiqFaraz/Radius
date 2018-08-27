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
    fun bindFacility(facility: FacilitiesAndOptions, selectedOptions: HashMap<String,String>) {

        with(itemView) {

            /*if (adapterPosition > 0 && selectedOptions["${adapterPosition}"].isNullOrEmpty()) {
                llFacility.visibility = View.GONE
                return
            }*/

            Log.d("BaseVH","option $adapterPosition ${selectedOptions["${adapterPosition}"]}")
            llFacility.visibility = View.VISIBLE
            tvFacility.text = facility.facility?.name
            rvOptions.layoutManager = GridLayoutManager(context, 3)

//            if (optionsAdapter == null) {
                optionsAdapter = KotlinRVAdapter(context,
                        R.layout.item_options, { it ->
                    BaseVH(it)
                }, { holder, item, position ->
                    holder.bindOptions(item,adapterPosition)
                }, facility.options.toMutableList())

                rvOptions.adapter = optionsAdapter
                optionsAdapter?.notifyDataSetChanged()


//            for (i in 0 until facility.options.size)
//                if(exclusion.optionId == facility.options[i].facilityId)
//                    optionsAdapter?.notifyItemChanged(i)




        }
    }


    fun detached() {
        RxBus.unregister(this)
    }

    fun bindOptions(option: Option,facilityPos:Int) {
        with(itemView) {
            tvOption.text = option.name

           val color =  if(option.disabled) {
                ContextCompat.getColor(context,R.color.disabled)
            } else if(option.isSelected)
                ContextCompat.getColor(context,R.color.colorPrimary)
            else
                ContextCompat.getColor(context,R.color.gray)
            ivOption.setColorFilter(color,PorterDuff.Mode.SRC_ATOP)



            tvOption.setTextColor(color)
            val name = if(option.icon == "no-room") "no_room" else option.icon
            val id = context.resources.getIdentifier(name+"2x","drawable",context.packageName)
            ivOption.setImageResource(id)
            llOption.setOnClickListener {
                if(!option.disabled) {
                    RxBus.sendSticky(RxClickEvent(facilityPos,adapterPosition,option.facilityId,option.id))
                }

            }
        }
    }

}