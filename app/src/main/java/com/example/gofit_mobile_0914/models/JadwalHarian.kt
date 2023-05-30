package com.example.gofit_mobile_0914.models

data class JadwalHarian (
    val id: Int,
    val tanggal: String,
    val status: String,
    val sesiJadwal: Int,
    val hari: String,
    val jamMulai: String,
    val namaKelas: String,
    val namaInstruktur: String
)