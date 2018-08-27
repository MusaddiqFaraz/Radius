package com.faraz.app.radius.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.faraz.app.radius.R
import com.faraz.app.radius.component
import com.faraz.app.radius.data_manager.AppRxSchedulers
import com.faraz.app.radius.data_manager.RxBus
import com.faraz.app.radius.data_manager.RxClickEvent
import com.faraz.app.radius.data_manager.api.*
import com.faraz.app.radius.data_manager.registerInBus
import com.faraz.app.radius.extensions.KotlinRVAdapter
import com.faraz.app.radius.extensions.vmProviderForActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appRxSchedulers: AppRxSchedulers
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
    private var exclusionMap = HashMap<Exclusion,List<Exclusion>>()

    private val mainVM by vmProviderForActivity { component.mainVM }
    private lateinit var facilitiesAdapter:KotlinRVAdapter<FacilitiesAndOptions,BaseVH>
    private var facilitiesAndOptions = ArrayList<FacilitiesAndOptions>()
    private var selectedOptions = HashMap<String,String>()
    private var previousSelectedPos = HashMap<Int,Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvFacility.layoutManager = LinearLayoutManager(this)
        getFacilities()

        btnFilter.setOnClickListener {
            Log.d("MainAct","selected options $selectedOptions")
        }
    }

    override fun onPause() {
        super.onPause()
        RxBus.unregister(this)
    }

    private fun getFacilities() {
        mainVM.getFacilities().subscribe({
            when(it.type) {
                TYPE.SUCCESS -> {
                    Toast.makeText(this,"From ${it.source}",Toast.LENGTH_SHORT).show()
                    Log.d("MainAc","success data ${it.data}")
                    it.data?.let {
                        exclusionMap.clear()
                        for (exclusion in it.exclusions) {
                            if(exclusion.exclusions.size > 1) {
                                val key = exclusion.exclusions[0]
                                val value = exclusion.exclusions.subList(1,exclusion.exclusions.size)
                                exclusionMap[key] = value
                            }
                        }


                        var count =1
                        for (facility in it.facilitiesAndOptions){
                            facility.facility?.let {
                                selectedOptions[it.facilityId] = ""
                                previousSelectedPos[count++] = -1
                            }

                        }
                        Log.d("MainAct","hashmap ${exclusionMap}")
                        displayFacilities(it.facilitiesAndOptions)
                    }
                }
                TYPE.ERROR -> {
                    Toast.makeText(this,"Error ${it.source}",Toast.LENGTH_SHORT).show()
                    Log.d("MainAc","success error ${it.data}")
                }

            }
        },{
            it.printStackTrace()
        })
    }

    private fun displayFacilities(facilitiesAndOptions: List<FacilitiesAndOptions>) {
        this.facilitiesAndOptions.clear()
        this.facilitiesAndOptions.addAll(facilitiesAndOptions)
        facilitiesAdapter = KotlinRVAdapter(this,
                R.layout.item_facility,
                { BaseVH(it) }, {
            holder, item ,position ->
            holder.bindFacility(item,selectedOptions)

        },facilitiesAndOptions.toMutableList())

        rvFacility.adapter= facilitiesAdapter
        facilitiesAdapter.notifyDataSetChanged()
        listenToSelectEvents()
    }



    private fun listenToSelectEvents(){
        RxBus.observeSticky<RxClickEvent>()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("MainAct","Click event consumed with $it")
                    val facPos =it.facilityPos
                    val optionPos = it.optionPos
                    if(previousSelectedPos.containsKey(facPos)) {
                        val prevOptionPos = previousSelectedPos[facPos]
                        prevOptionPos?.let {
                            if(prevOptionPos != -1 && prevOptionPos != optionPos)
                                this.facilitiesAndOptions[facPos].options[prevOptionPos].isSelected = !this.facilitiesAndOptions[facPos].options[prevOptionPos].isSelected
                        }
                    }
                    this.facilitiesAndOptions[facPos].options[optionPos].isSelected = !this.facilitiesAndOptions[facPos].options[optionPos].isSelected
                    facilitiesAdapter.notifyItemChanged(facPos)
                    val isSelected = this.facilitiesAndOptions[facPos].options[optionPos].isSelected
                    if(isSelected)
                        previousSelectedPos[facPos] = optionPos
                    else
                        previousSelectedPos[facPos] = -1
                    val exclusion = Exclusion(it.facilityId,it.optionId,0)
                    val exclusions = exclusionMap[exclusion]
                    val fromPos = if(isSelected) facPos+1 else facPos
                    if(exclusions != null && exclusions.isNotEmpty()) {
                        disableFeature(fromPos,exclusions)
                    } else {
                        deselectAll(fromPos)
                    }
                },{
                    it.printStackTrace()
                }).registerInBus(this)
    }

    fun deselectAll(fromPos:Int) {
        for (i in fromPos until facilitiesAndOptions.size ) {
            for (option in facilitiesAndOptions[i].options) {
                option.disabled = false
                option.isSelected = false
            }
            facilitiesAdapter.notifyItemChanged(i)
        }
    }

    fun disableFeature(fromPos: Int,exclusions: List<Exclusion>) {
        for (i in fromPos until facilitiesAndOptions.size ) {
            for (option in facilitiesAndOptions[i].options) {
                val exclusion = Exclusion(option.facilityId, option.id, 0)
                option.disabled = exclusions.contains(exclusion)
            }
            facilitiesAdapter.notifyItemChanged(i)
        }
    }






}
