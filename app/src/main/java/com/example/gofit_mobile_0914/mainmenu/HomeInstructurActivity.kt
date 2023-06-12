package com.example.gofit_mobile_0914.mainmenu

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gofit_mobile_0914.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeInstructurActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_instructur)
        supportActionBar?.hide()

        val sharedPref = applicationContext.getSharedPreferences("login", Context.MODE_PRIVATE)
        val LoginAs = sharedPref.getString("LoginAs","")
        println(LoginAs)
//        val fragmentProfile = FragmentProfile()
//        val fragmentRs = FragmentRS()
        val fragmentMainMenu = FragmentMainMenu()
//        val cameraActivity = CameraActivity()

        setCurrentFragment(fragmentMainMenu)
//
        val bottom_nav : BottomNavigationView = findViewById(R.id.bottom_navigationIns)
//
        bottom_nav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.ic_home-> {
                    setCurrentFragment(fragmentMainMenu)
                    true
                }
                R.id.ic_schedule-> {
//                    setCurrentFragment(fragmentRs)
                    true
                }
                R.id.ic_request-> {
                    setCurrentFragment(IjinFragment())
                    true
                }
                else -> false
            }
        }
//
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Disini kita menghubungkan menu yang telah kita buat dengan activity ini
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu_member, menu)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment){
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.layout_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

//    fun setActivity(activity: AppCompatActivity){
//        val moveActivity = Intent(this@HomeActivity, EditProfileActivity::class.java)
//        startActivity(moveActivity)
//    }
}