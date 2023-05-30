package com.example.gofit_mobile_0914.introPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.adapter.ViewPagerAdapter
import com.example.gofit_mobile_0914.databinding.ActivityIntropageBinding
import me.relex.circleindicator.CircleIndicator3

class IntroPageActivity  : AppCompatActivity()  {
    private lateinit var binding: ActivityIntropageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntropageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

//        setContentView(R.layout.activity_intropage)

//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.pager, IntroPage1Fragment())
//        transaction.addToBackStack(null)
//        transaction.commit()

//        postToList()
//
//        pager.adapter = ViewPagerAdapter(titlesList, descList)
//        pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//

        binding.pager.adapter = ViewPagerAdapter(this)
        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(binding.pager)
//        val adapter = ViewPagerAdapter(this)
//        val fragsList = listOf(IntroPage1Fragment(), IntroPage2Fragment())
//        adapter.fragsListHere.addAll(fragsList)
    }

    override fun onBackPressed() {
        if (binding.pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            binding.pager.currentItem = binding.pager.currentItem - 1
        }
    }

}