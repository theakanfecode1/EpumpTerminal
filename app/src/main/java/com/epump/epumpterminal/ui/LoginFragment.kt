package com.epump.epumpterminal.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.User
import com.epump.epumpterminal.utils.Constants
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    private val viewModel: LoginViewModel by viewModels()

    lateinit var emailEditText: AutoCompleteTextView
    lateinit var passwordEditText: EditText
    lateinit var loginButton: ConstraintLayout
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        activity?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.custom_background))
        loginButton = view.findViewById(R.id.login_button)
        sharedPreferences = view.context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        loginButton.isEnabled = true
        emailEditText = view.findViewById(R.id.email_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
//        emailEditText.addTextChangedListener(emailTextWatcher)
//        passwordEditText.addTextChangedListener(passwordTextWatcher)
        loginButton.setOnClickListener {
            login_text.visibility = View.INVISIBLE
            login_progress_bar.visibility = View.VISIBLE
            loginButton.isEnabled = false
            val userName = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val user: User = User(userName = userName, password = password,id = 0)
            viewModel.loginUser(user)
        }

        subscribeObservers()

        return view
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

            loginButton.isEnabled = true
            text?.isEmpty().also {
                emailEditText.error = "Username cannot be empty"
                emailEditText.requestFocus()

            }

            text?.isNotEmpty().also {
                emailEditText.error = null
                emailEditText.requestFocus()

            }
        }
    }
    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

            loginButton.isEnabled = true
            Toast.makeText(context, "CHANGED", Toast.LENGTH_LONG).show()
            text?.isEmpty().also {
                emailEditText.error = "Username cannot be empty"
                emailEditText.requestFocus()

            }

            text?.isNotEmpty().also {
                Toast.makeText(context, "CHANGED", Toast.LENGTH_LONG).show()
                emailEditText.error = null
                emailEditText.requestFocus()

            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    val sharedPreferencesEditor : SharedPreferences.Editor = sharedPreferences.edit()
                    sharedPreferencesEditor.apply {
                        putBoolean(Constants.LOGIN_STATE,true)
                        putString(Constants.ACCESS_TOKEN,dataState.data?.token?:"")
                    }.apply()
                    SplashScreenFragment.token = dataState.data?.token?:""
                    SplashScreenFragment.firstName = dataState.data?.firstName?: ""
                    findNavController().navigate(R.id.action_loginFragment_to_mappingFragment)
                }
                Resource.Status.LOADING -> {
                    loginButton.isEnabled = false

                    login_text.visibility = View.INVISIBLE
                    login_progress_bar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    loginButton.isEnabled = true

                    login_text.visibility = View.VISIBLE
                    login_progress_bar.visibility = View.INVISIBLE
                    Toast.makeText(activity,dataState.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


}