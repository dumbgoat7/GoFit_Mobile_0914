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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.IjinAdapter
import com.example.gofit_mobile_0914.adapter.PresensiInstrukturAdapter
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.models.Ijin
import com.example.gofit_mobile_0914.models.PresensiInstruktur
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class FragmentProfileInstruktur : Fragment() {
    var sharedPref : SharedPreferences? = null
    private lateinit var queue: RequestQueue
    private lateinit var late : TextView
    private lateinit var adapterPresensiInstruktur: PresensiInstrukturAdapter
    private lateinit var adapterIjinAdapter: IjinAdapter

    private lateinit var dataListIjin : MutableList<Ijin>
    private lateinit var dataListPresensi : MutableList<PresensiInstruktur>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_instruktur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("data_instruktur", Context.MODE_PRIVATE)
        val id = sharedPref?.getInt("id", 0)
        println(id)
        println("wue")
        val name : TextView = view.findViewById(R.id.ins_name)
        val email : TextView = view.findViewById(R.id.ins_email)
        val phone : TextView = view.findViewById(R.id.ins_phone)
        late = view.findViewById(R.id.ins_late)

        name.text = sharedPref?.getString("nama_instruktur", "")
        email.text = sharedPref?.getString("email_instruktur", "")
        phone.text = sharedPref?.getString("no_telp", "")


        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val token = sharedPref!!.getString("access_token", "")
        queue = Volley.newRequestQueue(requireContext())

        val btnlogout = view.findViewById<ImageView>(R.id.logoutprofileins)
        val btnactivity = view.findViewById<Button>(R.id.btn_activityIns)

        fetchdataInstruktur(token!!, id!!, late)

        btnactivity.setOnClickListener {
            println("wue1")
            showFormDialog(token, id)
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

    private fun showFormDialog(token : String, id : Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.pop_up_activityinstruktur)

        val listPresensi = dialog.findViewById<RecyclerView>(R.id.list_presensi_instruktur)
        val listIjin = dialog.findViewById<RecyclerView>(R.id.list_ijin_instruktur)

        val judul = dialog.findViewById<TextView>(R.id.tv_judulActivityIns)
        val judul2 = dialog.findViewById<TextView>(R.id.tv_judulActivityIns2)
        println(judul.text.toString())
        listPresensi.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        listIjin.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        dataListPresensi = mutableListOf()
        dataListIjin = mutableListOf()

        adapterPresensiInstruktur = PresensiInstrukturAdapter(dataListPresensi)
        adapterIjinAdapter = IjinAdapter(dataListIjin)
        println("wue4")
        listPresensi.adapter = adapterPresensiInstruktur
        listIjin.adapter = adapterIjinAdapter

        fetchPresensiInstruktur(token, id, listPresensi, judul, late)
        fetchIjinInstruktur(token, id, listIjin, judul2)
        dialog.show()
    }

    private fun fetchdataInstruktur(token: String, id:Int, late : TextView ){
        val url = ApiUrl.instruktur + id
        val request : StringRequest = object : StringRequest(Method.GET,url,
        { response->
            try{
                val jsonResponse = JSONObject(response)
                val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                if(jsonArray.length() == 0){
                    late.text = "0"
                }
                else {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        val waktuTerlambat =
                            if (jsonObject.getString("waktu_terlambat") == "null") {
                                0
                            } else {
                                jsonObject.getString("waktu_terlambat")
                            }
                        late.text = waktuTerlambat.toString()
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        },{ error->

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

    private fun fetchPresensiInstruktur(token: String, id : Int, list : RecyclerView, judul : TextView, late: TextView)     {
        val url = ApiUrl.getpresensiInstruktur + id

        val request : StringRequest =
        object : StringRequest(Method.GET,url,
            { response ->
                try{
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("data") == "[]") {
                        list.visibility = View.GONE
                        judul.setText("You haven't requested any absence yet")
                    } else {
                        val jsonArray : JSONArray = jsonResponse.getJSONArray("data")
                        for(i in 0 until jsonArray.length()){
                            val jsonObject : JSONObject = jsonArray.getJSONObject(i)
                            val waktuTerlambat = late.text.toString().toInt()
                            val data = PresensiInstruktur(
                                jsonObject.getInt("id"),
                                jsonObject.getString("nama_kelas"),
                                jsonObject.getString("jam_mulai"),
                                jsonObject.getString("jam_datang"),
                                waktuTerlambat
                            )
                            dataListPresensi.add(data)
                        }
                        adapterPresensiInstruktur.notifyDataSetChanged()
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            },{ error ->
                println(error.toString())
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

    private fun fetchIjinInstruktur(token: String, id: Int, list: RecyclerView, judul: TextView) {
        println("fetch")
        val url = ApiUrl.showByInstruktur +id
        println(url)
        val stringRequest : StringRequest = object : StringRequest(Method.GET, url,Response.Listener
        { response ->
            try {
                val jsonResponse = JSONObject(response)
                if(jsonResponse.getString("data") == "null") {
                    list.visibility = View.GONE
                    judul.setText("You haven't requested any absence yet")
                } else {
                    val jsonObject = JSONObject(response)
                    val jsonArray : JSONArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val dataObject: JSONObject = jsonArray.getJSONObject(i)
                        val check = dataObject.getInt("status")
                        val status = if(check == 1) {
                            "Confirmed"
                        } else {
                            "Not yet Confirmed"
                        }
                        val data = Ijin (
                            dataObject.getInt("id"),
                            dataObject.getString("tanggal_ijin"),
                            dataObject.getString("nama_instruktur_pengganti"),
                            dataObject.getString("keterangan"),
                            status
                        )
                        dataListIjin.add(data)
                    }
                    println(dataListIjin.size)
                    adapterIjinAdapter.notifyDataSetChanged()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        },
            Response.ErrorListener { error ->

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(stringRequest)
    }

}