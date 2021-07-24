package com.adspb.rsshool2021_android_task_pomodoro

interface StopwatchListener {
    fun start(id: Int)

    fun stop(id: Int, currentMs: Long)

    fun delete(id: Int)

    fun finish()

    fun checkTimer(currentMs: Long)


}