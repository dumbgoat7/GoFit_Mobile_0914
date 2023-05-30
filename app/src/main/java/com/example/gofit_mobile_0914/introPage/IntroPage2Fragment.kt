package com.example.gofit_mobile_0914.introPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gofit_mobile_0914.mainmenu.HomeActivity
import com.example.gofit_mobile_0914.databinding.FragmentIntropage2Binding


class IntroPage2Fragment : Fragment(){
    private lateinit var binding : FragmentIntropage2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntropage2Binding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.startText.setOnClickListener {

            val myActivity = requireActivity()

            val intent = Intent(myActivity, HomeActivity::class.java)
            val transaction = myActivity?.supportFragmentManager?.beginTransaction()
            transaction!!.remove(this)
            transaction.commit()

            startActivity(intent)
        }
    }


}