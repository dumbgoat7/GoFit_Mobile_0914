package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.DepositKelas

class DepositKelasAdapter(private val depositclass: MutableList<DepositKelas>) :
    RecyclerView.Adapter<DepositKelasAdapter.DepositKelasViewHolder>() {

    inner class DepositKelasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noStrukTextView: TextView = itemView.findViewById(R.id.tvnoStrukkls)
        val tanggalTransaksiTextView: TextView = itemView.findViewById(R.id.tvtanggal_transaksi)
        val kelasTextView: TextView = itemView.findViewById(R.id.tvkelas)
        val depositTextView: TextView = itemView.findViewById(R.id.tvdepositkls)
        val jumlahPemakaianTextView: TextView = itemView.findViewById(R.id.tvdeposit)
        val masaBerlakuTextView: TextView = itemView.findViewById(R.id.tvmasaberlaku)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositKelasViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_deposit_kelas, parent, false)
        return DepositKelasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DepositKelasViewHolder, position: Int) {
        val currentDeposit = depositclass[position]
        holder.noStrukTextView.text = currentDeposit.noStruk
        holder.tanggalTransaksiTextView.text = currentDeposit.tanggalTransaksi
        holder.kelasTextView.text = currentDeposit.namaKelas
        holder.depositTextView.text = currentDeposit.deposit.toString()
        holder.jumlahPemakaianTextView.text = currentDeposit.depositKelas.toString()
        holder.masaBerlakuTextView.text = currentDeposit.masaBerlaku
    }

    override fun getItemCount(): Int {
        return depositclass.size
    }
}
