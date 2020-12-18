package com.epump.epumpterminal.ui.bottomsheets

import android.net.wifi.ScanResult
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.ui.adapters.FileAdapter
import com.epump.epumpterminal.ui.adapters.WifiAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WifiBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var wifiRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.wifi_list, container, false)
        wifiRecyclerView = view.findViewById(R.id.wifi_recycler_view)
        val wifiAdapter =  WifiAdapter(view.context,findNavController())
        wifiAdapter.setResults(arguments?.getParcelableArrayList<ScanResult>("results")?.toList() ?: mutableListOf())
        wifiRecyclerView.apply {
            adapter = wifiAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        return view
    }
}