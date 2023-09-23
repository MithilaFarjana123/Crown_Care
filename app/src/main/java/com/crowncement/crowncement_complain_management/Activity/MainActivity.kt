package com.crowncement.crowncement_complain_management.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.crowncement.crowncement_complain_management.Fragment.Frag_dashboard
import com.crowncement.crowncement_complain_management.Fragment.Frag_genarate_complain
import com.crowncement.crowncement_complain_management.Fragment.Frag_history
import com.crowncement.crowncement_complain_management.Fragment.Frag_notification
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationListener()
    }



    private fun bottomNavigationListener(){
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(Frag_dashboard())
                    true
                }
                R.id.complain -> {
                    loadFragment(Frag_genarate_complain())
                    true
                }
                R.id.history -> {
                    loadFragment(Frag_history())
                    true
                }
                R.id.notification -> {
                    loadFragment(Frag_notification())
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
    }



}