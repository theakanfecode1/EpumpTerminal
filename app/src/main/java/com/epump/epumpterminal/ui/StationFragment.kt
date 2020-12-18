package com.epump.epumpterminal.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.epump.epumpterminal.R
import com.epump.epumpterminal.ui.adapters.StationAdapter
import com.epump.epumpterminal.models.Station
import com.epump.epumpterminal.utils.Constants
import com.epump.epumpterminal.utils.CustomActions
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.viewmodel.StationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class StationFragment : Fragment() {

    lateinit var stationRecyclerView: RecyclerView
    lateinit var stationAdapter: StationAdapter
    lateinit var findStationFilter : EditText
    lateinit var stationSwipeRefreshLayout: SwipeRefreshLayout

    private val viewModel: StationViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_station, container, false)
        stationRecyclerView = view.findViewById(R.id.station_recycler_view)
        viewModel.deletePumps()
        findStationFilter = view.findViewById(R.id.find_station)
        findStationFilter.setText("")
        stationAdapter = StationAdapter(view.context,findNavController())
        stationRecyclerView.apply {
            adapter = stationAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        stationSwipeRefreshLayout = view.findViewById(R.id.station_swipe_to_refresh)
        stationSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
        stationSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getStations()
        }
        findStationFilter.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                stationAdapter.filter.filter(p0.toString())

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        subscribeObservers()
        return view
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    stationSwipeRefreshLayout.isRefreshing= false;
                    stationAdapter = StationAdapter(requireContext(),findNavController())
                    stationRecyclerView.apply {
                        adapter = stationAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                    stationAdapter.setStations(dataState.data!!)


                }
                Resource.Status.LOADING -> {
                    stationSwipeRefreshLayout.isRefreshing= true;

                }
                Resource.Status.ERROR -> {
                    stationSwipeRefreshLayout.isRefreshing= false;
                    Toast.makeText(context,dataState.message,Toast.LENGTH_LONG).show()
                }

            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        CustomActions.closeKeyboard(requireActivity() as AppCompatActivity,requireContext())

    }

}