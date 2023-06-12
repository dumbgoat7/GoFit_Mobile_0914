package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.Ijin

class IjinAdapter (private val ijinList: MutableList<Ijin>) :
        RecyclerView.Adapter<IjinAdapter.IjinViewHolder>() {

        inner class IjinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tanggalTextView: TextView = itemView.findViewById(R.id.tanggalTV)
            private val tanggalIjinTextView: TextView = itemView.findViewById(R.id.tanggalijinTv)
            private val substituteTextView: TextView = itemView.findViewById(R.id.substituteTv)
            private val keteranganTextView: TextView = itemView.findViewById(R.id.rvketeranganTv)
            private val statusTextView : TextView = itemView.findViewById(R.id.statusIjinTv)

        fun bind(ijin: Ijin) {
            tanggalTextView.text = ijin.tanggalIjin
            tanggalIjinTextView.text = ijin.tanggalIjin
            substituteTextView.text = ijin.pengganti
            keteranganTextView.text = ijin.keterangan
            statusTextView.text = ijin.status
        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IjinViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_ijin, parent, false)
            return IjinViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: IjinViewHolder, position: Int) {
            val currentItem = ijinList[position]
            holder.bind(currentItem)
        }

        override fun getItemCount(): Int {
            return ijinList.size
        }
}