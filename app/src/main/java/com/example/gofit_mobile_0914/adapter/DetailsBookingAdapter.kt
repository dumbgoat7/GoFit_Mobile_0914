package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.DetailsBookingGym

class DetailsBookingAdapter(private val dataList: MutableList<DetailsBookingGym>) :
    RecyclerView.Adapter<DetailsBookingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val Sesi : TextView = itemView.findViewById(R.id.slot_waktu)
        private val Kapasitas : TextView = itemView.findViewById(R.id.kapasitas)

        fun bind(item: DetailsBookingGym) {
            Sesi.text = item.slotWaktu
            Kapasitas.text = item.sisaKapasitas.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
