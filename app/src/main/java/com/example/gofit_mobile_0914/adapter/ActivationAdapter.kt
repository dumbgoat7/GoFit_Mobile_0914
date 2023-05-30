package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.Activation

class ActivationAdapter(private val activations: MutableList<Activation>) :
    RecyclerView.Adapter<ActivationAdapter.ActivationViewHolder>() {

    inner class ActivationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noStrukTextView: TextView = itemView.findViewById(R.id.tvnoStrukakt)
        val tanggalTransaksiTextView: TextView = itemView.findViewById(R.id.tvtanggal_transaksiakt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_activate, parent, false)
        return ActivationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActivationViewHolder, position: Int) {
        val currentActivation = activations[position]
        holder.noStrukTextView.text = currentActivation.noStrukAkt
        holder.tanggalTransaksiTextView.text = currentActivation.tanggalTransaksi
    }

    override fun getItemCount(): Int {
        return activations.size
    }
}