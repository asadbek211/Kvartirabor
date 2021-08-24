package com.bizmiz.kvartirabor.ui

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     var check = false
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.splashColor)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val navControl: NavController =
            Navigation.findNavController(this, R.id.mainFragmentContener)
       binding.btnNav.setupWithNavController(navControl)
        visibility(false)
    }

    fun visibility(boolean: Boolean) {
        if (boolean) {
            binding.btnNav.visibility = View.GONE
        } else {
           binding.btnNav.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {

           if (!check){
               super.onBackPressed()
           }
    }

}