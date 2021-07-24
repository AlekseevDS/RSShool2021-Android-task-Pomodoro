package com.adspb.rsshool2021_android_task_pomodoro

data class Stopwatch(
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean,
    val START_VALUE_MS: Long = currentMs
)


