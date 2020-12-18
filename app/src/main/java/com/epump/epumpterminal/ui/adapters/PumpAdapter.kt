package com.epump.epumpterminal.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController

import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.ui.PumpFragmentDirections
import java.util.*

class PumpAdapter(private val mContext: Context,private val navController: NavController) :
    RecyclerView.Adapter<PumpAdapter.PumpViewHolder>() , Filterable{

    private var pumps = emptyList<Pump>()
    private var filteredPumps = emptyList<Pump>()


    inner class PumpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pumpName: TextView = itemView.findViewById(R.id.pump_name_list)
        val checkedView: ImageView = itemView.findViewById(R.id.checked)
        val pumpDetail: TextView = itemView.findViewById(R.id.pump_details)
        val pumpCard : CardView = itemView.findViewById(R.id.card_view_pump)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PumpViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.pump_list_item, parent, false)
        return PumpViewHolder(view)
    }

    override fun onBindViewHolder(holder: PumpViewHolder, position: Int) {
        if (filteredPumps[position].isChecked == true){
            holder.checkedView.visibility = View.VISIBLE
        }else{
            holder.checkedView.visibility = View.GONE
        }
        holder.pumpName.text = filteredPumps[position].displayName
        holder.pumpDetail.text = filteredPumps[position].lastSeen
        holder.pumpCard.setOnClickListener {
            val action = PumpFragmentDirections.actionPumpFragmentToPumpDetailFragment(filteredPumps[position])
            navController.navigate(action)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredPumps = pumps
                } else {

                    val filteredList: MutableList<Pump> = mutableListOf()
                    pumps.forEach {
                        if (it.displayName?.toLowerCase(Locale.ROOT)!!
                                .contains(charString.toLowerCase(Locale.ROOT))

                        ) {
                            filteredList.add(it)
                        }
                    }

                    filteredPumps = filteredList
                }

                val filterResult = FilterResults()
                filterResult.values = filteredPumps;
                return filterResult

            }

            override fun publishResults(charSequence: CharSequence?, p1: FilterResults?) {
                filteredPumps = p1?.values as List<Pump>
                filteredPumps.sortedBy { it.displayName }
                notifyDataSetChanged()
            }

        }
    }


    override fun getItemCount(): Int {
        return filteredPumps.size
    }

    internal fun setPumps(pumps :List<Pump> ){
        this.pumps = pumps.sortedBy { it.displayName }
        this.filteredPumps = pumps.sortedBy { it.displayName }

        notifyDataSetChanged()
    }

}