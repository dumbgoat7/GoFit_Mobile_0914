package com.example.gofit_mobile_0914.mainmenu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gofit_mobile_0914.R

class FragmentProfileInstruktur : Fragment() {
    var sharedPref : SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_instruktur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = this.getActivity()?.getSharedPreferences("data_instruktur", Context.MODE_PRIVATE)
        val name : TextView = view.findViewById(R.id.ins_name)
        val email : TextView = view.findViewById(R.id.ins_email)
        val phone : TextView = view.findViewById(R.id.ins_phone)

        name.text = sharedPref?.getString("nama_instruktur", "")
        email.text = sharedPref?.getString("email_instruktur", "")
        phone.text = sharedPref?.getString("no_telp", "")

        val btnlogout = view.findViewById<ImageView>(R.id.logoutprofileins)

        btnlogout.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Do you want to exit?")
            builder.setPositiveButton("OK", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    sharedPref = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
                    val editor = sharedPref!!.edit()
                    editor.putString("access_token", "")
                    editor.putString("LoginAs","Guest")
                    editor.putBoolean("isLogin",false)
                    editor.apply()
                    Toast.makeText(requireContext(),"Successfully Logged Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent ( requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                }
            })
                .setNegativeButton("NO", DialogInterface.OnClickListener{
                        dialog, id -> dialog.cancel()
                })
            builder.show()

        }


    }

}