package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.PresensiInstruktur

class PresensiInstrukturAdapter(private val presensiList: MutableList<PresensiInstruktur>) :
    RecyclerView.Adapter<PresensiInstrukturAdapter.PresensiInstrukturViewHolder>() {

    inner class PresensiInstrukturViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val kelasTV: TextView = itemView.findViewById(R.id.kelasTV)
        private val jamMulaiTv: TextView = itemView.findViewById(R.id.jamMulaiTv)
        private val jamDatangTv: TextView = itemView.findViewById(R.id.jamDatangTv)
        private val waktuTerlambatTv: TextView = itemView.findViewById(R.id.waktuTerlambatTv)

        fun bind(presensi: PresensiInstruktur) {
            kelasTV.text = presensi.namaKelas
            jamMulaiTv.text = presensi.jamMulai
            jamDatangTv.text = presensi.jamDatang
            waktuTerlambatTv.text = presensi.waktuTerlambat.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresensiInstrukturViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_presensi_instruktur, parent, false)
        return PresensiInstrukturViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresensiInstrukturViewHolder, position: Int) {
        holder.bind(presensiList[position])
    }

    override fun getItemCount(): Int {
        return presensiList.size
    }
}
