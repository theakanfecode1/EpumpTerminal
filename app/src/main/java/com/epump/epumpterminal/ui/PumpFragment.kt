package com.epump.epumpterminal.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.ui.adapters.PumpAdapter
import com.epump.epumpterminal.utils.Constants
import com.epump.epumpterminal.utils.CustomActions
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.viewmodel.PumpViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PumpFragment : Fragment() {

    lateinit var addDeviceBtn: Button
    lateinit var saveReportBtn: Button
    lateinit var backPressButton: ImageButton
    lateinit var pumpAdapter: PumpAdapter
    lateinit var findPumpFilter: EditText
    lateinit var pumpsView: RecyclerView
    lateinit var stationTitle: TextView
    lateinit var pumpSwipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: PumpViewModel by viewModels()

    companion object {
        lateinit var branchId: String
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pump, container, false)
        backPressButton = view.findViewById(R.id.pump_back_button)
        findPumpFilter = view.findViewById(R.id.find_pump)
        stationTitle = view.findViewById(R.id.station_title)
        findPumpFilter.setText("")
        pumpSwipeRefreshLayout = view.findViewById(R.id.pump_swipe_to_refresh)
        pumpSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(Constants.PUMP_EDITED)?.observe(viewLifecycleOwner, Observer {
            viewModel.setCheckedForPump(it)
        })
        stationTitle.text = PumpFragmentArgs.fromBundle(requireArguments()).stationName
        branchId = PumpFragmentArgs.fromBundle(requireArguments()).branchId
        pumpSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getPumps(branchId)
        }
        findPumpFilter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pumpAdapter.filter.filter(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        backPressButton.setOnClickListener {
            findNavController().popBackStack()
        }
        addDeviceBtn = view.findViewById(R.id.add_device)
        saveReportBtn = view.findViewById(R.id.save_report)
        saveReportBtn.setOnClickListener {
            val action = PumpFragmentDirections.actionPumpFragmentToSummaryFragment(
                PumpFragmentArgs.fromBundle(requireArguments()).stationName
            )
            findNavController().navigate(action)
        }
        addDeviceBtn.setOnClickListener {
            findNavController().navigate(R.id.action_pumpFragment_to_addDeviceFragment)
        }
        pumpsView = view.findViewById(R.id.pump_recycler_view)
        pumpAdapter = PumpAdapter(view.context, findNavController())
        pumpsView.apply {
            adapter = pumpAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
        subscribeObservers()
        return view
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    pumpSwipeRefreshLayout.isRefreshing = false;
                    pumpAdapter = PumpAdapter(requireContext(), findNavController())
                    pumpsView.apply {
                        adapter = pumpAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                    pumpAdapter.setPumps(dataState.data!!)
                }
                Resource.Status.LOADING -> {
                    pumpSwipeRefreshLayout.isRefreshing = true;

                }
                Resource.Status.ERROR -> {
                    pumpSwipeRefreshLayout.isRefreshing = false;
                    Toast.makeText(context, dataState.message, Toast.LENGTH_LONG).show()


                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CustomActions.closeKeyboard(requireActivity() as AppCompatActivity,requireContext())

    }

}