package com.example.gofit_mobile_0914.mainmenu

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.IjinAdapter
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.models.DetailsBookingGym
import com.example.gofit_mobile_0914.models.Ijin
import com.example.gofit_mobile_0914.models.Instructur
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class IjinFragment : Fragment() {
    var sharedPref: SharedPreferences? = null

    private lateinit var cardView2 : View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IjinAdapter
    private lateinit var dataList: MutableList<Ijin>
    private lateinit var listInstruktur: MutableList<Instructur>
    private lateinit var queue: RequestQueue
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ijin, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardView2 = view.findViewById(R.id.ijinCard2)

        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val token = sharedPref!!.getString("access_token", "")

        sharedPref = this.getActivity()?.getSharedPreferences("data_instruktur", Context.MODE_PRIVATE)
        val id = sharedPref!!.getInt("id", 0)
        recyclerView = view.findViewById(R.id.list_ijin)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataList = mutableListOf()
        listInstruktur = mutableListOf()
        adapter = IjinAdapter(dataList)
        recyclerView.adapter = adapter

        queue = Volley.newRequestQueue(requireContext())
        fetchData(token!!, id)
        getDataInstruktur(token)

        val btnIjin : Button = view.findViewById(R.id.ijinbtn)

        btnIjin.setOnClickListener {
            showFormDialog(id, token)
        }

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchData(token: String, id: Int) {
        println("fetch")
        val url = ApiUrl.showByInstruktur +id
        println(url)
        val stringRequest : StringRequest = object : StringRequest(Method.GET, url,Response.Listener
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    if(jsonResponse.getString("message") != "Empty"){
                        println("masuk")
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
                                dataObject.getString("tanggal_pembuatan_ijin"),
                                dataObject.getString("tanggal_ijin"),
                                dataObject.getString("nama_instruktur_pengganti"),
                                dataObject.getString("keterangan"),
                                status
                            )
                            dataList.add(data)
                        }
                        println(dataList)
                        println(dataList.size)
                        adapter.notifyDataSetChanged()
                        if(dataList.size == 0){
                            recyclerView.visibility = View.GONE
                            cardView2.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            cardView2.visibility = View.GONE
                        }
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

    private fun showFormDialog(id: Int, token: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.pop_up_ijin)
        val desc : TextInputLayout = dialog.findViewById(R.id.keterangan)
        var selectedId: Int? = null

        val tanggalIjin : TextInputEditText = dialog.findViewById(R.id.et_tanggal_ijin)
        tanggalIjin.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    tanggalIjin.setText(year.toString() + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth))
                }, year, month, day)
            datePicker.show()
        }

        val dropdownItems = listInstruktur.map { it.nama }.toTypedArray()
        val dropdownIds = listInstruktur.map { it.id }.toTypedArray()
        val autoCompleteTextView = dialog.findViewById<AutoCompleteTextView>(R.id.ed_subs)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, dropdownItems)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            selectedId = dropdownIds[position]
        }

        val btnSubmit : Button = dialog.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val enteredDate = tanggalIjin.text.toString()
            val keterangan : String = desc.editText?.text.toString()
            if(selectedId == null) {
                Toast.makeText(context, "Please select a session", Toast.LENGTH_SHORT).show()
            }
            else if(enteredDate == "") {
                Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
            } else if(keterangan == "") {
                Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
            }
            else {
                println(selectedId)
                println(enteredDate)
                println(keterangan)
                ijin(id, selectedId!!, enteredDate, keterangan, token)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun getDataInstruktur(token: String) {
        println("getData")
        val stringRequest = object : StringRequest(Method.GET, ApiUrl.instruktur,
            Response.Listener { response ->
                // Parse the JSON response and retrieve the instructor data
                val jsonObject = JSONObject(response)
                val jsonArray : JSONArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val dataObject: JSONObject = jsonArray.getJSONObject(i)
                    val id = dataObject.getInt("id")
                    val nama = dataObject.getString("nama_instruktur")
                    val instructur = Instructur(id, nama)
                    listInstruktur.add(instructur)
                }
                // Populate the dropdown with the retrieved data
            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun ijin(id: Int, selectedId: Int, date: String, desc: String, token: String) {
        val stringRequest = object : StringRequest(
            Method.POST,
            ApiUrl.ijinInstruktur,
            Response.Listener { response ->
                println("test1")
                val jsonResponse = JSONObject(response)
                val message = jsonResponse.getString("message")

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
                    params["id_instruktur"] = id.toString()
                    params["id_instruktur_pengganti"] = selectedId.toString()
                    params["tanggal_ijin"] = date
                    params["keterangan"] = desc
                    return params
                }
            }
            queue.add(stringRequest)
        }
}