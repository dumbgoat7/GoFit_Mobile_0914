package com.example.gofit_mobile_0914.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.DepositReguler

class DepositRegulerAdapter(private val deposits: MutableList<DepositReguler>) :
    RecyclerView.Adapter<DepositRegulerAdapter.DepositRegulerViewHolder>() {

    inner class DepositRegulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noStrukTextView: TextView = itemView.findViewById(R.id.tvnoStruk)
        val tanggalTransaksiTextView: TextView = itemView.findViewById(R.id.tvtanggal_transaksireg)
        val depositTextView: TextView = itemView.findViewById(R.id.tvMutasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositRegulerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_deposit_reguler, parent, false)
        return DepositRegulerViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DepositRegulerViewHolder, position: Int) {
        val currentDeposit = deposits[position]
        holder.noStrukTextView.text = currentDeposit.noStruk
        holder.tanggalTransaksiTextView.text = currentDeposit.tanggalTransaksi
        holder.depositTextView.text = (currentDeposit.deposit?.plus(currentDeposit.bonusDeposit!!)).toString()
    }

    override fun getItemCount(): Int {
        return deposits.size
    }
}
