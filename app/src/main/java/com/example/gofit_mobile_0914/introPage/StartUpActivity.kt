package com.example.gofit_mobile_0914.introPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.mainmenu.HomeActivity

class StartUpActivity : AppCompatActivity() {
    private lateinit var logo : ImageView
    private lateinit var text : TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("LoginAs","Guest")
//        editor.putBoolean("isLogin",false)
//        editor.apply()
//        val test = sharedPreferences.getString("LoginAs","Guest")
//        println(test)
//        println(sharedPreferences.getString("isLogin",""))
        val isFirstTimeOpened = sharedPreferences.getBoolean("first_time", true)
//        val isFirstTimeOpened = true


            setContentView(R.layout.activity_startup)
            supportActionBar?.hide()
            val splash = AnimationUtils.loadAnimation(this, R.anim.splash)
            text = findViewById(R.id.judul)
            logo = findViewById(R.id.logo)
            logo.startAnimation(splash)
            text.startAnimation(splash)


        if(isFirstTimeOpened) {
            sharedPreferences
                .edit()
                .putBoolean("first_time", false)
                .apply()

            sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("LoginAs","Guest")
            editor.putBoolean("isLogin",false)
            editor.putString("access_token", "")
            editor.apply()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, IntroPageActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
        else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}