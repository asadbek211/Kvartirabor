package com.bizmiz.kvartirabor.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.ActivityMainBinding
import com.bizmiz.kvartirabor.ui.auth.login.LoginFragment
import com.bizmiz.kvartirabor.ui.elon.ElonBerish.ElonBerishFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth:FirebaseAuth
    var check = false
    lateinit var binding: ActivityMainBinding
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setNavigationBarColor(this, android.R.color.holo_orange_light)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
           window.statusBarColor = ContextCompat.getColor(this, R.color.white)
      }else{
        window.statusBarColor = ContextCompat.getColor(this, R.color.marshmallow)
      }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val navControl: NavController =
            Navigation.findNavController(this, R.id.mainFragmentContener)
        binding.apply {
        bottomNavView.setupWithNavController(navControl)
        bottomNavView.background = null
        visibility(false)
       fab.setOnClickListener {
            if (mAuth.currentUser != null) {
                navigate(R.id.elonBerishFragment)
            } else {
                navigate(R.id.loginFragment)
            }
        }
        }
    }

    fun visibility(boolean: Boolean) {
        if (boolean) {
            binding.container.visibility = View.GONE

        } else {
            binding.container.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (!check) {
            super.onBackPressed()
        }
    }
    private fun setNavigationBarColor(activity: Activity, color: Int) {
        val window: Window = activity.window
        val c = activity.resources.getColor(color)
        window.navigationBarColor = c
    }
    private fun navigate(destination:Int){
        val navController: NavController =
            Navigation.findNavController(
                this,
                R.id.mainFragmentContener
            )
        navController.navigate(destination)
    }
}