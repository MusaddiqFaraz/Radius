package com.faraz.app.radius.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.faraz.app.radius.R
import com.faraz.app.radius.data_manager.RxBus
import com.faraz.app.radius.data_manager.RxFilterEvent
import com.faraz.app.radius.data_manager.api.Option
import com.faraz.app.radius.data_manager.registerInBus
import com.faraz.app.radius.extensions.KotlinRVAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity:AppCompatActivity() {


    private lateinit var selectedOptionsAdapter: KotlinRVAdapter<Option,BaseVH>
    private var options = ArrayList<Option>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
    }


    override fun onResume() {
        super.onResume()
        RxBus.observeSticky<RxFilterEvent>()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    RxBus.sendSticky("")
                    options.clear()
                    options.addAll(it.options)
                    Log.d("FilterAct","options ${it.options}")
                    displaySelectedOptions()

                },{
                    it.printStackTrace()
                }).registerInBus(this)
    }

    override fun onPause() {
        super.onPause()
        RxBus.unregister(this)
    }

    private fun displaySelectedOptions() {
        Log.d("FilterAct","options rec $options")
        rvSelectedOptions.layoutManager = LinearLayoutManager(this)

        selectedOptionsAdapter = KotlinRVAdapter(this,
                R.layout.item_options,{ BaseVH(it) },
                {
                    holder, item, position ->
                    holder.bindOptions(item,0,true)
                },options)


        rvSelectedOptions.adapter = selectedOptionsAdapter
        selectedOptionsAdapter.notifyDataSetChanged()

    }
}