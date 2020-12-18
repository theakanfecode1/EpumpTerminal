package com.epump.epumpterminal.ui.adapters

import android.content.Context
import android.net.wifi.ScanResult
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController

import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import java.io.File

class WifiAdapter(private val mContext: Context, private val navController: NavController) :
    RecyclerView.Adapter<WifiAdapter.WifiViewHolder>() {

    private var results = emptyList<ScanResult>()

    inner class WifiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wifiName: TextView = itemView.findViewById(R.id.wifi_name)
        val wifiCard: CardView = itemView.findViewById(R.id.wifi_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.wifi_list_item, parent, false)
        return WifiViewHolder(view)
    }

    override fun onBindViewHolder(holder: WifiViewHolder, position: Int) {

        holder.wifiName.text = if (results[position].SSID == "") "NO SSID" else results[position].SSID
        holder.wifiCard.setOnClickListener {
            if (holder.wifiName.text != "") {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "ssid",
                    results[position].SSID
                )
                navController.popBackStack()
            } else {
                Toast.makeText(mContext, "This Wifi cannot be used", Toast.LENGTH_SHORT).show()
            }


        }

    }

    override fun getItemCount(): Int {
        return results.size
    }

    internal fun setResults(results: List<ScanResult>) {
        this.results = results
        notifyDataSetChanged()
    }

}