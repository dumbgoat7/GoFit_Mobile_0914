import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.LoginActivity
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.DetailsBookingAdapter
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.models.DetailsBookingGym
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class FragmentBookingGym : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetailsBookingAdapter
    private lateinit var dataList: MutableList<DetailsBookingGym>
    private lateinit var queue: RequestQueue
    var sharedPref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookinggym, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val isLogin = sharedPref!!.getBoolean("isLogin", false)
        val loginAs = sharedPref!!.getString("LoginAs","")
        val token = sharedPref!!.getString("access_token", "")

        println("wue")
        println(token)
        println(loginAs)
        sharedPref = this.getActivity()?.getSharedPreferences("data_member", Context.MODE_PRIVATE)
        val id = sharedPref!!.getString("id_member", "")
        recyclerView = view.findViewById(R.id.list_session)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataList = mutableListOf()
        adapter = DetailsBookingAdapter(dataList)
        recyclerView.adapter = adapter

        queue = Volley.newRequestQueue(requireContext())
        fetchData(token!!)

        val addbtn : Button = view.findViewById(R.id.addbtn)

        addbtn.setOnClickListener {
            if (isLogin) {
                showFormDialog(id!!, token)
            } else {
                Toast.makeText(requireContext(), "Please Login First", Toast.LENGTH_SHORT).show()
                val intent = Intent ( requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun showFormDialog(id: String, Token: String) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.pop_up_bookinggym)

        val autoCompleteTextView = dialog.findViewById<AutoCompleteTextView>(R.id.ed_sesi)
        val buttonSubmit = dialog.findViewById<Button>(R.id.buttonSubmit)
        val dateTextInput = dialog.findViewById<TextInputEditText>(R.id.et_tanggal)
        // Set up the dropdown items for the AutoCompleteTextView
        val dropdownItems = arrayOf("7 - 9", "9 - 11", "11 - 13", "13 - 15", "15 - 17", "17 - 19", "19 - 21")

        val dropdownItemIds = mapOf(
            "7 - 9" to "1",
            "9 - 11" to "2",
            "11 - 13" to "3",
            "13 - 15" to "4",
            "15 - 17" to "5",
            "17 - 19" to "6",
            "19 - 21" to "7"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, dropdownItems)
        autoCompleteTextView.setAdapter(adapter)


        dateTextInput.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dateTextInput.setText(year.toString() + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth))
                }, year, month, day)
            datePicker.show()
        }
        buttonSubmit.setOnClickListener {
            // Handle submit button click here

            // Retrieve the selected value from the AutoCompleteTextView
            val selectedValue = autoCompleteTextView.text.toString()
            val selectedId = dropdownItemIds[selectedValue]
            // Retrieve the entered date from the TextInputEditText
            val enteredDate = dateTextInput.text.toString()
            if(selectedId == null) {
                Toast.makeText(context, "Please select a session", Toast.LENGTH_SHORT).show()
            }
            else if(enteredDate == "") {
                Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
            }else {
                bookingGym(id, selectedId, enteredDate, Token)

                dialog.dismiss()
            }
            // Perform necessary actions with the form data

        }

        dialog.show()
    }

    private fun bookingGym(id: String, selectedId: String, date: String, Token: String) {
        val url =ApiUrl.booking // Replace with your actual API endpoint

//        val requestQueue = Volley.newRequestQueue(requireContext())
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val message = jsonResponse.getString("message")

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle API error here
                println("test")
                // You can display an error message or perform other error handling
                if (error.networkResponse != null) {
                    val errorMessage = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(errorMessage)
                    println(errors.getString("message"))
                    Toast.makeText(context, errors.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $Token" // Add your authorization header value
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_member"] = id
                params["id_details_booking"] = selectedId
                params["tanggal_booking"] = date
                // Add more parameters if needed
                return params
            }
        }

        queue.add(stringRequest)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetchData(Token: String) {
        val stringRequest : StringRequest = object : StringRequest(Method.GET, ApiUrl.getdetailsBooking, Response.Listener { response ->
            try {
                val jsonObject = JSONObject(response)
                val jsonArray : JSONArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val dataObject: JSONObject = jsonArray.getJSONObject(i)
                    val id = dataObject.getInt("id")
                    val slotWaktu = dataObject.getString("slot_waktu")
                    val sisaKapasitas = dataObject.getInt("sisa_kapasitas")
                    val detailsBookingGym = DetailsBookingGym(id, slotWaktu, sisaKapasitas)
                    dataList.add(detailsBookingGym)
                }
                println(dataList)
                adapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers : MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $Token"
                return headers
            }
        }
        queue.add(stringRequest)
    }

}
