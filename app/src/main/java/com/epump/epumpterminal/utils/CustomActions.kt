package com.epump.epumpterminal.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.epump.epumpterminal.R

class CustomActions {

    companion object {
        fun loader(message: String, activity: Activity): Dialog {
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.loader, null)
            val loaderMessage = view.findViewById<TextView>(R.id.loader_message)
            loaderMessage.text = message
            val alertDialog = Dialog(activity, R.style.DialogTheme)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            return alertDialog
        }

        fun closeKeyboard(activity: Activity, context: Context) {
            val view: View? = activity.currentFocus
            if (view != null) {
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }

        }

    }
}