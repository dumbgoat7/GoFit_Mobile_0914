package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.todaySchedule

class TodayScheduleAdapter(private val scheduleList: MutableList<todaySchedule>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<TodayScheduleAdapter.TodayScheduleViewHolder>() {

    inner class TodayScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kelasTV: TextView = itemView.findViewById(R.id.kelasTV)
        private val jamMulaiTv: TextView = itemView.findViewById(R.id.jamMulaiTv)
        private val namaInstrukturTv: TextView = itemView.findViewById(R.id.namaInstrukturTv)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(schedule: todaySchedule) {
            kelasTV.text = schedule.namaKelas
            jamMulaiTv.text = schedule.jamMulai
            namaInstrukturTv.text = schedule.namaInstruktur
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_schedule, parent, false)
        return TodayScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodayScheduleViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }
}