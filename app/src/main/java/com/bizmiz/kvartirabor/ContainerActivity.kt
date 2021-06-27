package com.bizmiz.kvartirabor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.ui.auth.LoginFragment
import com.bizmiz.kvartirabor.ui.map.MapFragment

class ContainerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        when(intent.extras!!.getInt("num")){
            1-> {
                val loginFragment = LoginFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frameContainer,loginFragment).commit()
            }
            2-> {
                val mapFragment = MapFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frameContainer,mapFragment).commit()
            }
        }
    }

}