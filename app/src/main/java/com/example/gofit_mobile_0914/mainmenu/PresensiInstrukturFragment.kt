package com.example.gofit_mobile_0914.mainmenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.TodayScheduleAdapter
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.models.todaySchedule
import com.google.android.material.appbar.MaterialToolbar
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PresensiInstrukturFragment : Fragment() {
    var sharedPref: SharedPreferences? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodayScheduleAdapter
    private lateinit var dataList: MutableList<todaySchedule>
    private lateinit var queue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_presensiinstruktur, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val token = sharedPref!!.getString("access_token", "")

        recyclerView = view.findViewById(R.id.list_schedule)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataList = mutableListOf()
        adapter = TodayScheduleAdapter(dataList) { position ->

            val clickedItem = dataList[position]
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Confirm instructor attendance?")
            builder.setPositiveButton("OK", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    presensiInstruktur(token!!, clickedItem.id, clickedItem.idInstructur)
                }
            })
                .setNegativeButton("NO", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })
            builder.show()
        }
        recyclerView.adapter = adapter

        queue = Volley.newRequestQueue(requireContext())

        val calendar = Calendar.getInstance()

        // Mapping the day of week value to a string representation
        val dayOfWeekString = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Unknown"
        }
        println(dayOfWeekString)

        getTodaySchedule(token!!, dayOfWeekString)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getTodaySchedule(token : String, day : String) {
        val url = ApiUrl.showbyDay + day

        val stringRequest : StringRequest =
        object : StringRequest(
            Method.GET, url, Response.Listener
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val dataObject = jsonArray.getJSONObject(i)
                        val todaySchedule = todaySchedule(
                            dataObject.getInt("id"),
                            dataObject.getInt("id_instruktur"),
                            dataObject.getString("nama_kelas"),
                            dataObject.getString("nama_instruktur"),
                            dataObject.getString("jam_mulai"),
                        )
                        dataList.add(todaySchedule)
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                val errorResponse = error.networkResponse?.data
                if (errorResponse != null) {
                    val errorString = String(errorResponse)
                    try {
                        val errorObject = JSONObject(errorString)
                        val errorMessage = errorObject.getString("message")
                        println(errorMessage)
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "Error: An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Error: An error occurred", Toast.LENGTH_SHORT).show()
                }
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers : MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }
            queue.add(stringRequest)
        }

    private fun presensiInstruktur(token : String, id : Int, idInstruktur : Int) {
        val stringRequest = object : StringRequest(
            Method.POST,
            ApiUrl.presensiInstruktur,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val message = jsonObject.getString("message")
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                println("test2")
                val errorResponse = error.networkResponse?.data
                if (errorResponse != null) {
                    val errorString = String(errorResponse)
                    try {
                        val errorObject = JSONObject(errorString)
                        val errorMessage = errorObject.getString("message")
                        println(errorMessage)
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "Error: An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Error: An error occurred", Toast.LENGTH_SHORT).show()
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"
                return headers
            }
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_jadwal"] = id.toString()
                params["id_instruktur"] = idInstruktur.toString()
                return params
            }
        }
        queue.add(stringRequest)
    }

}