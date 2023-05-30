package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.JadwalHarian

class JadwalHarianAdapter(
    private val item: MutableList<JadwalHarian>
): RecyclerView.Adapter<JadwalHarianAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hari : TextView = itemView.findViewById(R.id.tvHari)
        private val tanggal : TextView = itemView.findViewById(R.id.tvTanggal)
        private val namaKelas : TextView = itemView.findViewById(R.id.tvNamaKelas)
        private val namaInstruktur : TextView = itemView.findViewById(R.id.tvNamaInstruktur)
        private val jamKelas : TextView = itemView.findViewById(R.id.tvJamKelas)
        private val status : TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(item: JadwalHarian) {
            hari.text = item.hari
            tanggal.text = item.tanggal
            namaKelas.text = item.namaKelas
            namaInstruktur.text = item.namaInstruktur
            jamKelas.text = item.jamMulai
            status.text = item.status

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalHarianAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_timetables, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalHarianAdapter.ViewHolder, position: Int) {
        val data = item[position]

        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return item.size
    }

}