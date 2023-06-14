package com.example.gofit_mobile_0914.api

class ApiUrl {
    companion object{
        val  BASE_URl = "http://192.168.1.4:8080/P3L/GoFit_0914/public/api"

        val LoginPegawai = BASE_URl+"/loginMO"
        val loginInstruktur = BASE_URl+"/loginInstruktur"
        val loginMember = BASE_URl+"/loginMember"

        val resetPasswordMO = BASE_URl + "/resetPasswordMO/"
        val resetPasswordIns = BASE_URl + "/resetPasswordIns/"

        val getdetailsBooking = BASE_URl + "/detailsBooking"
        val booking = BASE_URl + "/bookingGym"
        val getJadwalHarian = BASE_URl + "/jadwalHarianforall"
        val showbyDay = BASE_URl + "/showbyDay/"
        val getActiveDeposit = BASE_URl + "/showActiveDepositKelasMember/"

        val getActivationMember = BASE_URl + "/showActivationMember/"
        val getDepositKlsMember = BASE_URl + "/showDepositKelasMember/"
        val getDepositRegMember = BASE_URl + "/showDepositRegulerMember/"
        val getBookingKlsMember = BASE_URl + "/showBookingKelasMember/"
        val getBookingGymMember = BASE_URl + "/showBookingbyMember/"

        val instruktur = BASE_URl + "/instrukturMobile/"
        val getpresensiInstruktur = BASE_URl + "/presensiInstruktur/"
        val presensiInstruktur = BASE_URl + "/presensiInstruktur"
        val ijinInstruktur = BASE_URl + "/ijinInstruktur"
        val showByInstruktur = BASE_URl + "/showByInstruktur/"
        val presensiKelas = BASE_URl + "/presensi/"
        val bookingKelas = BASE_URl + "/bookingKelasMobile"
    }
}