package com.epump.epumpterminal.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.ui.StationFragmentDirections
import java.util.*

class StationAdapter(private val mContext: Context, private val navController: NavController?) :
    RecyclerView.Adapter<StationAdapter.StationView>(), Filterable {

    private var stations = emptyList<Station>()
    private var filteredStations = emptyList<Station>()


    inner class StationView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById(R.id.station_name)
        val stationAddress: TextView = itemView.findViewById(R.id.station_address)
        val cardView: CardView = itemView.findViewById(R.id.card_view_station)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationView {
        val view = LayoutInflater.from(mContext).inflate(R.layout.station_list_item, parent, false)
        return StationView(view)

    }

    override fun onBindViewHolder(holder: StationView, position: Int) {
        holder.stationName.text = filteredStations[position].name
        holder.stationAddress.text = filteredStations[position].city
        holder.cardView.setOnClickListener {
            val action = StationFragmentDirections.actionStationFragmentToPumpFragment(
                filteredStations[position].id ?: "", filteredStations[position].name ?: ""
            )
            navController?.navigate(action)
        }

    }


    override fun getItemCount(): Int {
        return filteredStations.size
    }

    internal fun setStations(stations: List<Station>) {
        this.stations = stations.sortedBy { it.name }
        this.filteredStations = stations.sortedBy { it.name }
        Log.d("ADAPTER", "setStations: "+stations.get(0).name)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredStations = stations
                } else {

                    val filteredList: MutableList<Station> = mutableListOf()
                    stations.forEach {
                        if (it.name?.toLowerCase(Locale.ROOT)!!
                                .contains(charString.toLowerCase(Locale.ROOT))

                        ) {
                            filteredList.add(it)
                        }
                    }

                    filteredStations = filteredList
                }

                val filterResult = FilterResults()
                filterResult.values = filteredStations;
                return filterResult

            }

            override fun publishResults(charSequence: CharSequence?, p1: FilterResults?) {
                filteredStations = p1?.values as List<Station>
                filteredStations.sortedBy { it.name }
                notifyDataSetChanged()
            }

        }
    }
}