package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.BookingKelas

class BookingClassAdapter(private val bookingclass: MutableList<BookingKelas>) :
    RecyclerView.Adapter<BookingClassAdapter.BookingKelasViewHolder>() {

    inner class BookingKelasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaKelasTextView: TextView = itemView.findViewById(R.id.tvnamaKelas)
        val tanggalBookingTextView: TextView = itemView.findViewById(R.id.tvTanggalBookingkls)
        val jamMulaiTextView: TextView = itemView.findViewById(R.id.tvjamMulai)
        val statusBookingTextView: TextView = itemView.findViewById(R.id.tvstatusbooking)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingKelasViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_bookingkelas, parent, false)
        return BookingKelasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookingKelasViewHolder, position: Int) {
        val currentBooking = bookingclass[position]
        holder.namaKelasTextView.text = currentBooking.namaKelas
        holder.tanggalBookingTextView.text = currentBooking.tanggalBooking
        holder.jamMulaiTextView.text = currentBooking.jamMulai
        holder.statusBookingTextView.text = currentBooking.statusBooking
    }

    override fun getItemCount(): Int {
        return bookingclass.size
    }
}