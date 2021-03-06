package com.adspb.rsshool2021_android_task_pomodoro

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adspb.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/*  //если сразу передать binding
class StopwatchViewHolder (private val binding: StopwatchItemBinding):
    RecyclerView.ViewHolder(binding.root) {*/

// передается item:View для заполнения
class StopwatchViewHolder (item: View, private val listener: StopwatchListener): RecyclerView.ViewHolder(item) {
    // получаем через байндинг доступ к элементам (отдельным вью) на шаблоне
    //используем .bind , т.к. View уже надуто и Inflate не нужен, как делалось в Main

    private val binding = StopwatchItemBinding.bind(item)
    private var timer: CountDownTimer? = null
    private var statusCustomCircleView = false

    //заполняем элементы (View) находящиеся на item данными содержащимися в переданном data object (stopwatch)
    fun bind(stopwatch: Stopwatch) {

        binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()

        if (stopwatch.currentMs != stopwatch.START_VALUE_MS) {
            binding.root.setCardBackgroundColor(Color.WHITE)
            binding.deleteButton.setBackgroundColor(Color.WHITE)
        }

        binding.customViewTwo.isVisible = stopwatch.currentMs != stopwatch.START_VALUE_MS
        binding.customViewTwo.setPeriod(stopwatch.START_VALUE_MS)

        if (stopwatch.isStarted) {
            startTimer(stopwatch)
        } else {
            stopTimer(stopwatch)
        }

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.isStarted) {
                listener.stop(stopwatch.id, stopwatch.currentMs)
            } else {
                listener.start(stopwatch.id)
            }
        }

        binding.deleteButton.setOnClickListener {
            timer?.cancel()
            statusCustomCircleView = false
            binding.customViewTwo.setCurrent(0)
            listener.delete(stopwatch.id) }
    }

    private fun progressCyrcle(stopwatch: Stopwatch) {
        GlobalScope.launch {
            var current = stopwatch.currentMs
            while (current > 0 && statusCustomCircleView) {
                current -= UNIT_ONE_SEC_IN_MS
                binding.customViewTwo.setCurrent(current)
                delay(UNIT_ONE_SEC_IN_MS)
            }
        }
    }

    private fun startTimer(stopwatch: Stopwatch) {
        binding.startPauseButton.text = "STOP"
        binding.customViewTwo.isVisible = true
        binding.root.setCardBackgroundColor(Color.WHITE)
        binding.deleteButton.setBackgroundColor(Color.WHITE)

        timer?.cancel()
        timer = getCountDownTimer(stopwatch)
        timer?.start()

        binding.blinkingIndicator.isInvisible = false
        (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
        statusCustomCircleView = true
        progressCyrcle(stopwatch)
    }

    private fun stopTimer(stopwatch: Stopwatch) {
        binding.startPauseButton.text = "START"

        timer?.cancel()
        statusCustomCircleView = false

        binding.blinkingIndicator.isInvisible = true
        (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
    }

    private fun getCountDownTimer(stopwatch: Stopwatch): CountDownTimer {
        return object : CountDownTimer(stopwatch.currentMs, UNIT_ONE_SEC_IN_MS) {
            val interval = UNIT_ONE_SEC_IN_MS

            override fun onTick(millisUntilFinished: Long) {
                stopwatch.currentMs -= interval
                listener.checkTimer(stopwatch.currentMs)
                binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
            }

            override fun onFinish() {

                stopwatch.currentMs = stopwatch.START_VALUE_MS
                stopwatch.isStarted = false
                binding.stopwatchTimer.text = START_TIME
                listener.finish()
                binding.root.setCardBackgroundColor(Color.RED)
                binding.deleteButton.setBackgroundColor(Color.RED)
                binding.customViewTwo.setCurrent(0)

                //Индикация 00.00.00 миганием и вызовом тоста.
                Handler(Looper.getMainLooper()).apply {
                postDelayed({binding.root.setCardBackgroundColor(Color.WHITE)
                    binding.deleteButton.setBackgroundColor(Color.WHITE)}, 1000)
                postDelayed({binding.root.setCardBackgroundColor(Color.RED)
                    binding.deleteButton.setBackgroundColor(Color.RED)
                    binding.stopwatchTimer.text = stopwatch.currentMs.displayTime()
                    stopTimer(stopwatch)}, 2000)
                }
            }
        }
    }

    //ПЕРЕНЕСЕН в файл Utils.kt
 /*   //блок отображения времени на таймере
    private fun Long.displayTime(): String {

        if (this <= 0L) {
            return START_TIME
        }
        val h = this / 1000 / 3600
        val m = this / 1000 % 3600 / 60
        val s = this / 1000 % 60

        return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}"
    }

    private fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }*/

    private companion object {

        private const val START_TIME = "00:00:00"
        private const val UNIT_ONE_SEC_IN_MS = 1000L
    }
}




