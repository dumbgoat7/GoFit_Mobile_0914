package com.example.gofit_mobile_0914.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gofit_mobile_0914.introPage.IntroPage1Fragment
import com.example.gofit_mobile_0914.introPage.IntroPage2Fragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                IntroPage1Fragment()
            }
            1 -> {
                IntroPage2Fragment()
            }
            else -> {
                throw Resources.NotFoundException("Position Not Found")
            }
        }
    }
}