package com.example.gofit_mobile_0914.mainmenu

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.LoginActivity
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.*
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.models.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class FragmentProfileMember : Fragment() {
    private lateinit var recyclerView: RecyclerView
    var sharedPref: SharedPreferences? = null

    private lateinit var adapterActivation: ActivationAdapter
    private lateinit var adapterdepositReg: DepositRegulerAdapter
    private lateinit var adapterdepositKls: DepositKelasAdapter
    private lateinit var adapterBookingKls: BookingClassAdapter
    private lateinit var adapterBookingGym: BookingGymAdapter


    private lateinit var dataListact: MutableList<Activation>
    private lateinit var dataListdeporeg: MutableList<DepositReguler>
    private lateinit var dataListdepokls: MutableList<DepositKelas>
    private lateinit var dataListbookkls: MutableList<BookingKelas>
    private lateinit var dataListbookgym: MutableList<BookingGym>

    private lateinit var queue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("data_member", Context.MODE_PRIVATE)
        val id = sharedPref!!.getString("id_member", "")
        val name : TextView = view.findViewById(R.id.member_name)
        val email : TextView = view.findViewById(R.id.member_email)
        val phone : TextView = view.findViewById(R.id.phone)
        val deposit: TextView = view.findViewById(R.id.deposit)
        val activeUntil : TextView = view.findViewById(R.id.activeUntil)
        val activedeposit : TextView = view.findViewById(R.id.activeDepositKls)

        name.setText(sharedPref!!.getString("nama_member", ""))
        email.setText(sharedPref!!.getString("email", ""))
        phone.setText(sharedPref!!.getString("no_telp", ""))
        deposit.setText(sharedPref!!.getFloat("deposit_member", 0.0F).toString())
        activeUntil.setText(sharedPref!!.getString("masa_berlaku", ""))

        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val isLogin = sharedPref!!.getBoolean("isLogin", false)
        val Token = sharedPref!!.getString("access_token", "")
        queue = Volley.newRequestQueue(requireContext())

        getActiveDepositKls(Token!!, activedeposit, id!!)
        val btnactivity = view.findViewById<TextView>(R.id.btn_activity)
        val btnlogout = view.findViewById<ImageView>(R.id.logoutprofile)
        btnactivity.setOnClickListener {
            showFormDialog(Token, id!!)
        }

        btnlogout.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Do you want to exit?")
            builder.setPositiveButton("OK", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = sharedPref!!.edit()
                    editor.putString("access_token", "")
                    editor.putString("LoginAs","Guest")
                    editor.putBoolean("isLogin",false)
                    editor.apply()
                    Toast.makeText(requireContext(),"Successfully Logged Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent ( requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                }
            })
                .setNegativeButton("NO", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })
            builder.show()

        }

    }

    private fun showFormDialog(Token: String, id: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.pop_up_activity)

        val listActivate = dialog.findViewById<RecyclerView>(R.id.list_activate)
        val listDeposit = dialog.findViewById<RecyclerView>(R.id.list_deposit)
        val listDepositKelas = dialog.findViewById<RecyclerView>(R.id.list_deposit_kelas)
        val listGymSession = dialog.findViewById<RecyclerView>(R.id.list_gym_session)
        val listGymClass = dialog.findViewById<RecyclerView>(R.id.list_gym_class)

        val judul = dialog.findViewById<TextView>(R.id.tv_judul)
        val judul2 = dialog.findViewById<TextView>(R.id.tv_judul2)
        val judul3 = dialog.findViewById<TextView>(R.id.tv_judul3)
        val judul4 = dialog.findViewById<TextView>(R.id.tv_judul4)
        val judul5 = dialog.findViewById<TextView>(R.id.tv_judul5)


        listActivate.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listDeposit.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listDepositKelas.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listGymSession.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listGymClass.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        dataListact = mutableListOf()
        dataListdeporeg = mutableListOf()
        dataListdepokls = mutableListOf()
        dataListbookkls = mutableListOf()
        dataListbookgym = mutableListOf()

        adapterActivation = ActivationAdapter(dataListact)
        adapterdepositReg = DepositRegulerAdapter(dataListdeporeg)
        adapterdepositKls = DepositKelasAdapter(dataListdepokls)
        adapterBookingKls = BookingClassAdapter(dataListbookkls)
        adapterBookingGym = BookingGymAdapter(dataListbookgym)

        listActivate.adapter = adapterActivation
        listDeposit.adapter = adapterdepositReg
        listDepositKelas.adapter = adapterdepositKls
        listGymSession.adapter = adapterBookingGym
        listGymClass.adapter = adapterBookingKls

        fetchActivation(Token, id)
        fetchDepositReguler(Token, listDeposit, judul2, id)
        fetchDepositKelas(Token, listDepositKelas, judul3, id)
        fetchBookingKelas(Token, listGymClass,judul4, id)
        fetchBookingGym(Token, listGymSession,judul5, id)

        dialog.show()
    }

    private fun fetchActivation(token: String, id: String) {
        val url = ApiUrl.getActivationMember + id

        val request : StringRequest = object : StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("message") != "Empty"){
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()){
                            val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                            val data = Activation(
                                jsonObject.getString("no_struk_akt"),
                                jsonObject.getString("tanggal_transaksi"),
                                jsonObject.getDouble("biaya_transaksi"),
                            )
                            dataListact.add(data)
                            println(dataListact)
                        }
                        adapterActivation.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(request)
    }

    private fun fetchDepositReguler(token: String, listDeposit : RecyclerView,judul2: TextView, id: String) {
        val url = ApiUrl.getDepositRegMember + id

        val request : StringRequest = object: StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    println("eiyo")
                    if(jsonResponse.getString("data") == "null") {
                        listDeposit.visibility = View.GONE
                        judul2.setText("You have not made any deposit yet")
                    } else {
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()){
                            val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("Y-M-d", Locale.getDefault())
                            val date: Date = inputFormat.parse(jsonObject.getString("tanggal_transaksi"))
                            val tanggal = outputFormat.format(date)
                            val data = DepositReguler(
                                jsonObject.getString("no_struk"),
                                tanggal,
                                jsonObject.getDouble("deposit"),
                                jsonObject.getDouble("bonus_deposit"),
                                jsonObject.getDouble("sisa_deposit"),
                                jsonObject.getDouble("total_deposit"),
                                jsonObject.getDouble("deposit") + jsonObject.getDouble("bonus_deposit")
                            )
                            dataListdeporeg.add(data)
                        }
                        println(dataListdeporeg)
                        adapterdepositReg.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(request)
    }

    private fun fetchDepositKelas(token: String, listDepositKelas : RecyclerView,judul3:TextView, id: String) {
        val url = ApiUrl.getDepositKlsMember + id

        val request : StringRequest = object : StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("data") == "null") {
                        listDepositKelas.visibility = View.GONE
                        judul3.setText("You have not made any class deposit yet")
                    } else {
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()){
                            val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("Y-M-d", Locale.getDefault())
                            val date: Date = inputFormat.parse(jsonObject.getString("tanggal_transaksi"))
                            val tanggal = outputFormat.format(date)
                            val data = DepositKelas(
                                jsonObject.getString("no_struk"),
                                jsonObject.getString("nama_kelas"),
                                tanggal,
                                jsonObject.getDouble("deposit"),
                                jsonObject.getInt("deposit_kelas"),
                                jsonObject.getString("masa_berlaku"),
                             )
                            dataListdepokls.add(data)
                        }
                        adapterdepositKls.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(request)

    }

    private fun fetchBookingGym(token: String, listGymSession: RecyclerView ,judul4: TextView, id: String) {
        val url = ApiUrl.getBookingGymMember + id

        val request : StringRequest = object : StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("data") == "null") {
                        listGymSession.visibility = View.GONE
                        judul4.setText("You have not made any booking class yet")
                    } else {
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val data = BookingGym(
                                jsonObject.getString("id"),
                                jsonObject.getString("tanggal_booking"),
                                jsonObject.getString("status_booking"),
                                jsonObject.getString("slot_waktu"),
                            )
                            dataListbookgym.add(data)
                        }
                        println(dataListbookgym)
                        adapterBookingGym.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(request)
    }

    private fun fetchBookingKelas(token: String, listGymClass :RecyclerView,judul5: TextView, id: String) {
        val url = ApiUrl.getBookingKlsMember + id
        val request : StringRequest = object : StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("data") == "null") {
                        listGymClass.visibility = View.GONE
                        judul5.setText("You have not made any booking class yet")
                    } else {
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val data = BookingKelas(
                                jsonObject.getString("id"),
                                jsonObject.getString("tanggal_booking"),
                                jsonObject.getString("status_booking"),
                                jsonObject.getString("nama_kelas"),
                                jsonObject.getString("jam_mulai"),
                            )
                            dataListbookkls.add(data)
                        }
                        adapterBookingKls.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            { error->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(request)
    }

    private fun getActiveDepositKls(token: String, activedeposit: TextView, id : String) {
        val url = ApiUrl.getActiveDeposit + id

        val request : StringRequest = object: StringRequest(Method.GET,url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("data") == "null") {
                        activedeposit.setText("-")
                    } else {
                        val data = jsonResponse.getJSONArray("data")

                        if(data.length() > 0) {
                            val jsonObject = data.getJSONObject(0)
                            val namaKelas = jsonObject.getString("nama_kelas")
                            activedeposit.setText(namaKelas)
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        queue.add(request)

    }
}