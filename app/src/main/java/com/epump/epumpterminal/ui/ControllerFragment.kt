package com.epump.epumpterminal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.epump.epumpterminal.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class ControllerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_controller, container, false)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.app_fragment) as NavHostFragment
        view.findViewById<BottomNavigationView>(R.id.app_bottom_nav).setupWithNavController(navController = navHostFragment.navController)
        return view
    }

}