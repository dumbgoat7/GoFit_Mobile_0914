package com.example.gofit_mobile_0914.api

class ApiUrl {
    companion object{
        val  BASE_URl = "http://192.168.1.5:8080/P3L/GoFit_0914/public/api"

        val LoginPegawai = BASE_URl+"/loginMO"
        val loginInstruktur = BASE_URl+"/loginInstruktur"
        val loginMember = BASE_URl+"/loginMember"

        val resetPasswordMO = BASE_URl + "/resetPasswordMO/"
        val resetPasswordIns = BASE_URl + "/resetPasswordIns/"

        val getdetailsBooking = BASE_URl + "/detailsBooking"
        val booking = BASE_URl + "/bookingGym"
        val getJadwalHarian = BASE_URl + "/jadwalHarianforall"

        val getActiveDeposit = BASE_URl + "/showActiveDepositKelasMember/"

        val getActivationMember = BASE_URl + "/showActivationMember/"
        val getDepositKlsMember = BASE_URl + "/showDepositKelasMember/"
        val getDepositRegMember = BASE_URl + "/showDepositRegulerMember/"
        val getBookingKlsMember = BASE_URl + "/showBookingKelasMember/"
        val getBookingGymMember = BASE_URl + "/showBookingbyMember/"
    }
}