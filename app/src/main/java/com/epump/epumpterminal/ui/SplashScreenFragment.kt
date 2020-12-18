package com.epump.epumpterminal.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.epump.epumpterminal.R
import com.epump.epumpterminal.utils.Constants
import com.epump.epumpterminal.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    private  val TAG = "SplashScreenFragment"
    companion object {
        lateinit var token: String
        lateinit var firstName: String

    }

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        sharedPreferences =
            view.context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        CoroutineScope(Main).launch {
            switchPage()
        }
        return view
    }

    private suspend fun switchPage() {
        if (sharedPreferences.getBoolean(Constants.LOGIN_STATE, false)) {
            splashScreenViewModel.userDetails.observe(viewLifecycleOwner, Observer {
                token = it.token ?: ""
                firstName = it.firstName ?: ""
                Log.d(TAG, "switchPage: ${it.token} from db")
                Log.d(TAG, "switchPage: ${token} from object")
                Log.d(TAG, "switchPage: ${it.firstName} from db")
                Log.d(TAG, "switchPage: ${firstName} from object")

            })
            delay(5000)
            findNavController().navigate(R.id.action_splashScreenFragment_to_mappingFragment)
        } else {
            delay(5000)
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }

    }
}