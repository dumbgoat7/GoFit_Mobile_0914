package com.example.gofit_mobile_0914.mainmenu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.LoginActivity
import com.example.gofit_mobile_0914.ProfileActivity
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.JadwalHarianAdapter
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.databinding.FragmentMainmenuBinding
import com.example.gofit_mobile_0914.models.JadwalHarian
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class FragmentMainMenu : Fragment(R.layout.fragment_mainmenu) {
    private var _binding : FragmentMainmenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JadwalHarianAdapter
    private lateinit var dataList: MutableList<JadwalHarian>
    private lateinit var queue: RequestQueue
    var sharedPref: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View? {

        _binding = FragmentMainmenuBinding.inflate(inflater, container, false)
        val rootView = binding.root

        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        println(sharedPref!!.getBoolean("isLogin", false))
        if(sharedPref?.getString("LoginAs","") == "MO") {
            binding.profile.visibility = View.GONE
        } else {
            binding.profile.visibility = View.VISIBLE
            binding.logout.visibility = View.GONE

        binding.profile.setOnClickListener {

            sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
            println(sharedPref!!.getBoolean("isLogin", false))
            if (sharedPref!!.getBoolean("isLogin", false) == false) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            } else {
                if(sharedPref!!.getString("LoginAs","") == "Member") {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction!!.replace(R.id.layout_fragment, FragmentProfileMember())
                    transaction.addToBackStack(null)
                    transaction.commit()
                } else if (sharedPref!!.getString("LoginAs","") == "Instructur") {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction!!.replace(R.id.layout_fragment, FragmentProfileInstruktur())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

                println("sudah login")
            }
        }

        }
        binding.logout.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(rootView.context)
            builder.setMessage("Do you want to exit?")
            builder.setPositiveButton("OK", object:DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    val intent = Intent ( requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            })
                .setNegativeButton("NO",DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })
            builder.show()

        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val Token = sharedPref!!.getString("token", "")
        binding.listSession.layoutManager = LinearLayoutManager(requireContext())
        queue = Volley.newRequestQueue(requireContext())
        dataList = mutableListOf()
        adapter = JadwalHarianAdapter(dataList)
        binding.listSession.adapter = adapter
        fetchDataJadwalharian()
    }

    private fun fetchDataJadwalharian() {
        val stringRequest : StringRequest = object : StringRequest(Method.GET, ApiUrl.getJadwalHarian, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonArray : JSONArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val dataObject: JSONObject = jsonArray.getJSONObject(i)
                    val id = dataObject.getInt("id")
                    val tanggal = dataObject.getString("tanggal")
                    val status = dataObject.getString("status")
                    val sesiJadwal = dataObject.getInt("sesi_jadwal")
                    val hari = dataObject.getString("hari")
                    val jam = dataObject.getString("jam_mulai")
                    val namaKelas = dataObject.getString("nama_kelas")
                    val namaInstruktur = dataObject.getString("nama_instruktur")
                    val jadwalharian = JadwalHarian(id, tanggal, status, sesiJadwal, hari, jam, namaKelas, namaInstruktur)
                    dataList.add(jadwalharian)
                }
//                println(dataList)
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue.add(stringRequest)
    }

}