package com.adspb.rsshool2021_android_task_pomodoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.adspb.rsshool2021_android_task_pomodoro.databinding.StopwatchItemBinding

class StopwatchAdapter (private val listener: StopwatchListener
): ListAdapter<Stopwatch, StopwatchViewHolder>(itemComparator) {

    //Когда адаптер готовиться что бы начать заполнять шаблон и начать его печатать
    // inflate надувает View из шаблона разметки и оно потом храниться в памяти
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.stopwatch_item, parent, false)

        //вариант zig, передается binding
        /*val layoutInflater = LayoutInflater.from(parent.context)
        val binding = StopwatchItemBinding.inflate(layoutInflater, parent, false)*/

//        передаем View для заполнения
        return StopwatchViewHolder(view, listener)
    }

    //заполняет надутый вью из листа по указанной позиции
    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
//        передаем позицию из списка
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<Stopwatch>() {

            override fun areItemsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stopwatch, newItem: Stopwatch): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }

            //костыль от блика
            override fun getChangePayload(oldItem: Stopwatch, newItem: Stopwatch) = Any()
        }
    }
}