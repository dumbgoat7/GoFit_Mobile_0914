package com.example.gofit_mobile_0914

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gofit_mobile_0914.api.ApiUrl
import com.example.gofit_mobile_0914.mainmenu.HomeActivity
import com.example.gofit_mobile_0914.mainmenu.HomeInstructurActivity
import com.example.gofit_mobile_0914.mainmenu.HomeMOActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var loginAs: Spinner
    private lateinit var loading: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        val ResetPasswordTxt : TextView = findViewById(R.id.TextReset)
        val btnLogin : Button = findViewById(R.id.btnLogin)
        loginAs = findViewById(R.id.spinner)


        btnLogin.setOnClickListener {
            performLogin()
        }


        ResetPasswordTxt.setOnClickListener(View.OnClickListener {
            val userType = loginAs.selectedItem.toString()
            val username: String = inputUsername.getEditText()?.getText().toString()

            when (userType) {
                "Manager Operational" -> resetPasswordMO(username)
                "Instructur" -> resetPasswordInstruktur(username)
                "Member" -> Toast.makeText(this@LoginActivity, "Please go to the cashier staff to reset your password", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetPasswordMO(username: String) {
        if(username == null || username == "") {
            Toast.makeText(this@LoginActivity, "Input your username", Toast.LENGTH_SHORT).show()
            return
        }
        val params = HashMap<String, String>()
        params["username"] = username
        val stringRequest = object : StringRequest(
            Method.PUT, ApiUrl.resetPasswordMO + username,
            Response.Listener { response ->
                // Handle successful password reset response
                Toast.makeText(this@LoginActivity, "Password has reset to default", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle error case
                Toast.makeText(this, "Password reset failed", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(stringRequest)
    }

    private fun resetPasswordInstruktur(username: String) {
        if(username == null) {
            Toast.makeText(this@LoginActivity, "Input your username", Toast.LENGTH_SHORT).show()
        }
        println(username)
        val params = HashMap<String, String>()
        params["username"] = username

        val stringRequest = object : StringRequest(
            Method.PUT, ApiUrl.resetPasswordIns + username,
            Response.Listener { response ->
                // Handle successful password reset response
                Toast.makeText(this@LoginActivity, " Instructur password reset successful", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle error case
                Toast.makeText(this, "Password reset failed", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(stringRequest)

    }


    private fun saveAccessToken(accessToken: String, LoginAs:String) {
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("LoginAs",LoginAs)
        editor.putBoolean("isLogin",true)
        editor.apply()
    }
    private fun performLogin() {
        val userType = loginAs.selectedItem.toString()
        val username: String = inputUsername.getEditText()?.getText().toString()
        val password: String = inputPassword.getEditText()?.getText().toString()
        println(username)
        when (userType) {
            "Manager Operational" -> loginAsMO(username,password)
            "Instructur" -> loginAsInstruktur(username,password)
            "Member" -> loginAsMember(username,password)
        }
    }

    private fun loginAsMO(username: String, password: String) {

        val stringRequest = object : StringRequest(
            Method.POST,
            ApiUrl.LoginPegawai,
            Response.Listener<String> { response ->
                // Handle successful login response
                val jsonResponse = JSONObject(response)
                val user = jsonResponse.getString("user")
                val data = JSONObject(user)
                val id_pegawai = data.getInt("id_pegawai")
                sharedPreferences = this.getSharedPreferences("data_mo", Context.MODE_PRIVATE)
                var editor = sharedPreferences?.edit()
                editor?.putInt("id_pegawai", id_pegawai)
                editor?.putString("nama_pegawai", data.getString("nama_pegawai"))
                editor?.putString("no_telp", data.getString("no_telp"))
                editor?.putString("alamat", data.getString("alamat"))
                editor?.putString("email", data.getString("email_pegawai"))
                editor?.putString("role", data.getString("role"))
                editor?.putString("tanggal_lahir", data.getString("tanggal_lahir"))
                editor?.commit()

                val accessToken = jsonResponse.getString("access_token")
                saveAccessToken(accessToken, "MO")
                startActivity(Intent(this@LoginActivity, HomeMOActivity::class.java))
                Toast.makeText(this@LoginActivity, "Login successfully as MO", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                val errorResponse = error.networkResponse?.data
                if (errorResponse != null) {
                    val errorString = String(errorResponse)
                    try {
                        val errorObject = JSONObject(errorString)
                        val errorMessage = errorObject.getString("message")
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, "Error: An error occurred", Toast.LENGTH_SHORT).show()
                }
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        // Add the request to the RequestQueue
        queue.add(stringRequest)
    }

    private fun loginAsInstruktur(username: String, password: String) {
        println("wue")
        val stringRequest = object : StringRequest(
            Method.POST,
            ApiUrl.loginInstruktur,
            Response.Listener { response ->
                println("wue2")
                // Handle successful login response
                val jsonResponse = JSONObject(response)
                val user = jsonResponse.getString("user")
                val data = JSONObject(user)
                val id = data.getInt("id")
                sharedPreferences = this.getSharedPreferences("data_instruktur", Context.MODE_PRIVATE)
                var editor = sharedPreferences?.edit()
                editor?.putInt("id", id)
                editor?.putString("nama_instruktur", data.getString("nama_instruktur"))
                editor?.putString("alamat_instruktur", data.getString("alamat_instruktur"))
                editor?.putString("no_telp", data.getString("no_telp"))
                editor?.putString("email_instruktur", data.getString("email_instruktur"))
                editor?.putString("tanggal_lahir", data.getString("tanggal_lahir"))
                editor?.commit()
                println("wue3")
                val accessToken = jsonResponse.getString("access_token")
                println(accessToken)
                saveAccessToken(accessToken, "Instructur")
                startActivity(Intent(this@LoginActivity, HomeInstructurActivity::class.java))
                Toast.makeText(this@LoginActivity, "Login successfully as Instructur", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                val errorResponse = error.networkResponse?.data
                if (errorResponse != null) {
                    val errorString = String(errorResponse)
                    try {
                        val errorObject = JSONObject(errorString)
                        val errorMessage = errorObject.getString("message")
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, "Error: An error occurred", Toast.LENGTH_SHORT).show()
                }
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        queue.add(stringRequest)

    }

    private fun loginAsMember(username: String, password: String) {
//        val url = "http://192.168.1.11:8080/P3L/GoFit_0914/public/api/loginInstruktur"

        val stringRequest = object : StringRequest(
            Method.POST,
            ApiUrl.loginMember,
            Response.Listener<String> { response ->
                // Handle successful login response
                val jsonResponse = JSONObject(response)
                val user = jsonResponse.getString("user")
                val data = JSONObject(user)
                val id_member = data.getString("id_member")
                val accessToken = jsonResponse.getString("access_token")
                saveAccessToken(accessToken, "Member")

                sharedPreferences = this.getSharedPreferences("data_member", Context.MODE_PRIVATE)
                var dataMember = sharedPreferences?.edit()
                dataMember?.putString("id_member",id_member )
                dataMember?.putString("nama_member", data.getString("nama_member"))
                dataMember?.putString("alamat_member", data.getString("alamat_member"))
                dataMember?.putString("no_telp", data.getString("no_telp"))
                dataMember?.putFloat("deposit_member", data.getString("deposit_member").toFloat())
                dataMember?.putFloat("deposit_kelas", data.getString("deposit_kelas").toFloat())
                dataMember?.putString("masa_berlaku", data.getString("masa_berlaku"))
                dataMember?.putString("tgl_lahir", data.getString("tanggal_lahir"))
                dataMember?.putString("email", data.getString("email_member"))
                dataMember?.commit()

                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                Toast.makeText(this@LoginActivity, "Login successfully as Member", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                val errorResponse = error.networkResponse?.data
                if (errorResponse != null) {
                    val errorString = String(errorResponse)
                    try {
                        val errorObject = JSONObject(errorString)
                        val errorMessage = errorObject.getString("message")
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: An error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, "Error: An error occurred", Toast.LENGTH_SHORT).show()
                }
            }) {

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        queue.add(stringRequest)

    }
    override fun onDestroy() {
        super.onDestroy()
        queue.stop()
    }
    override fun onBackPressed() {
        // Leave this method empty to suppress the back button
        sharedPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)

        if(sharedPreferences?.getBoolean("isLogin", false) == true){
        }else{
            super.onBackPressed()
        }
    }

}