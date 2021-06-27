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
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.elonlarFragment -> {
                    navControl.navigate(R.id.elonlarFragment)
                }
                R.id.saralanganlarFragment -> {
                    navControl.navigate(R.id.saralanganlarFragment)
                }
                R.id.elonBerishFragment -> {
                    navControl.navigate(R.id.elonBerishFragment)
                }
                R.id.profil -> {
                    val mAuth = FirebaseAuth.getInstance()
                    if (mAuth.currentUser != null) {
                        navControl.navigate(R.id.authUserFragment)
                    } else {
                        val intent = Intent(this,ContainerActivity::class.java)
                        intent.putExtra("num",1)
                        startActivity(intent)
                        bottom_navigation.menu.findItem(R.id.profil).isCheckable = false

                    }}
            }
            return@setOnNavigationItemSelectedListener true
        }

    }
    override fun onBackPressed() {
            finish()

    }}