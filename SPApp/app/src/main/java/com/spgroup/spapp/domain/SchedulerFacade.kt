package com.spgroup.spapp.domain

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerFacade {
    fun workerScheduler(): Scheduler = Schedulers.io()
    fun callbackScheduler(): Scheduler = AndroidSchedulers.mainThread()
}