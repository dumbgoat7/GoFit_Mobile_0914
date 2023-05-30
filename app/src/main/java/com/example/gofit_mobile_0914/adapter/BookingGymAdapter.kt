package com.example.gofit_mobile_0914.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.models.BookingGym

class BookingGymAdapter(private val bookings: MutableList<BookingGym>) :
    RecyclerView.Adapter<BookingGymAdapter.BookingGymViewHolder>() {

    inner class BookingGymViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingTextView: TextView = itemView.findViewById(R.id.tvbooking)
        val tanggalBookingTextView: TextView = itemView.findViewById(R.id.tvTanggalBookinggym)
        val slotTextView: TextView = itemView.findViewById(R.id.tvslot)
        val statusBookingTextView: TextView = itemView.findViewById(R.id.tvstatusbookinggym)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingGymViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_showbookinggym, parent, false)
        return BookingGymViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookingGymViewHolder, position: Int) {
        val currentBooking = bookings[position]
        holder.bookingTextView.text = "Booking Gym #"+(position+1)
        holder.tanggalBookingTextView.text = currentBooking.tanggalBooking
        holder.slotTextView.text = currentBooking.slotWaktu
        holder.statusBookingTextView.text = currentBooking.statusBooking
    }

    override fun getItemCount(): Int {
        return bookings.size
    }
}
