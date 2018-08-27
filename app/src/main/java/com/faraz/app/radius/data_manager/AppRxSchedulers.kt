package com.faraz.app.radius.data_manager

import io.reactivex.Scheduler

/**
 * Created by root on 23/8/18.
 */
data class AppRxSchedulers(
        val io: Scheduler,
        val computation: Scheduler,
        val main: Scheduler
)