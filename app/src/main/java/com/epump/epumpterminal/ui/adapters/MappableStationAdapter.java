package com.epump.epumpterminal.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.epump.epumpterminal.R;
import com.epump.epumpterminal.models.StationDetails;
import com.epump.epumpterminal.ui.MappableStationFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class MappableStationAdapter extends RecyclerView.Adapter<MappableStationAdapter.DataViewHolder> implements Filterable {
    private Context mContext;
    private List<StationDetails> mStationDetailsArrayList;
    private List<StationDetails> mStationDetailsArrayListFiltered;
    private NavController mNavigation;

    public MappableStationAdapter(Context context, List<StationDetails> stationDetailsArrayList, NavController navigation) {
        mContext = context;
        mStationDetailsArrayList = stationDetailsArrayList;
        mStationDetailsArrayListFiltered = stationDetailsArrayList;
        mNavigation = navigation;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mappable_list_item,parent,false);
        DataViewHolder data = new DataViewHolder(view);
        return data;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, final int position) {
        holder.mName.setText(mStationDetailsArrayListFiltered.get(position).getName());
        holder.mAddress.setText(mStationDetailsArrayListFiltered.get(position).getCity());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StationDetails stationDetails = mStationDetailsArrayListFiltered.get(position);
                MappableStationFragmentDirections.ActionMappableStationFragmentToMapStationFragment actionMappableStationFragmentToMapStationFragment = MappableStationFragmentDirections.actionMappableStationFragmentToMapStationFragment(stationDetails,mStationDetailsArrayList.get(position).getId());
                mNavigation.navigate(actionMappableStationFragmentToMapStationFragment);
            }
        });

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mStationDetailsArrayListFiltered = mStationDetailsArrayList;
                } else {
                    List<StationDetails> filteredList = new ArrayList<>();
                    for (StationDetails row : mStationDetailsArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mStationDetailsArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mStationDetailsArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mStationDetailsArrayListFiltered = (ArrayList<StationDetails>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mStationDetailsArrayListFiltered.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        TextView  mName;
        TextView  mAddress;
        CardView mCardView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.mappable_station_name);
            mAddress = (TextView)itemView.findViewById(R.id.mappable_address);
            mCardView = (CardView)itemView.findViewById(R.id.cardviewholder);
        }
    }
}
