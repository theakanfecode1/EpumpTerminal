package com.epump.epumpterminal.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.PumpToSend

class SummaryAdapter(private val mContext : Context) : RecyclerView.Adapter<SummaryAdapter.SummaryView>() {

    private var pumps = emptyList<PumpToSend>()

    inner class SummaryView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pumpName : TextView = itemView.findViewById(R.id.summary_pump_name)
        val manualReading : TextView= itemView.findViewById(R.id.summary_manual_reading)
        val digitalReading : TextView= itemView.findViewById(R.id.summary_digital_reading)
        val price : TextView= itemView.findViewById(R.id.summary_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryView {
        val view = LayoutInflater.from(mContext).inflate(R.layout.summary_list_item,parent,false)
        return SummaryView(view)
    }

    override fun onBindViewHolder(holder: SummaryView, position: Int) {
        holder.pumpName.text = pumps[position].displayName
        holder.manualReading.text = pumps[position].manualReading.toString()
        holder.digitalReading.text = pumps[position].digitalReading.toString()
        holder.price.text = pumps[position].price.toString()

    }

    override fun getItemCount(): Int {
      return pumps.size
    }

    internal fun setPumps(pumps: List<PumpToSend> ){
        this.pumps = pumps
        notifyDataSetChanged()
    }
}