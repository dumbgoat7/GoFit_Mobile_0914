package com.example.gofit_mobile_0914.introPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gofit_mobile_0914.R
import com.example.gofit_mobile_0914.mainmenu.HomeActivity

class IntroPage1Fragment : Fragment() {
    private lateinit var skip : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intropage1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skip = view.findViewById(R.id.skipText)

        skip.setOnClickListener {
            val intent = Intent(activity, HomeActivity::class.java)
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.remove(this)
            transaction.commit()

            startActivity(intent)
        }
    }
}