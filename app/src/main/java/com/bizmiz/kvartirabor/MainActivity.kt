package com.bizmiz.kvartirabor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navControl: NavController =
            Navigation.findNavController(this, R.id.mainFragmentContener)
          bottom_navigation.setupWithNavController(navControl)
        visibility(false)
    }
    fun visibility(boolean: Boolean){
        if (boolean){
            bottom_navigation.visibility = View.GONE
        }else{
            bottom_navigation.visibility = View.VISIBLE
        }
    }
}