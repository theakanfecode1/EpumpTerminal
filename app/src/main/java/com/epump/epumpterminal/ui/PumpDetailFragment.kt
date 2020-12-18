package com.epump.epumpterminal.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import com.epump.epumpterminal.models.PumpToSend
import com.epump.epumpterminal.utils.Constants
import com.epump.epumpterminal.utils.CustomActions
import com.epump.epumpterminal.viewmodel.PumpDetailsViewModel
import com.epump.epumpterminal.viewmodel.PumpViewModel
import com.epump.epumpterminal.viewmodel.SummaryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PumpDetailFragment : Fragment() {

    lateinit var backPressButton: ImageButton
    lateinit var submitButton: Button
    lateinit var manualReading: EditText
    lateinit var digitalReading: EditText
    lateinit var price: EditText
    lateinit var lastSeen: TextView
    lateinit var pumpDetailsTitle: TextView
    lateinit var onlineStatus: TextView
    lateinit var pump: Pump
    private val pumpDetailsViewModel: PumpDetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pump_detail, container, false)
        pump = PumpDetailFragmentArgs.fromBundle(requireArguments()).pump
        backPressButton = view.findViewById(R.id.pump_details_back_button)
        manualReading = view.findViewById(R.id.manual_reading)
        digitalReading = view.findViewById(R.id.digital_reading)
        price = view.findViewById(R.id.price_pump)
        pumpDetailsTitle = view.findViewById(R.id.pump_details_name)
        pumpDetailsTitle.text = pump.displayName
        lastSeen = view.findViewById(R.id.pump_details_date)
        onlineStatus = view.findViewById(R.id.pump_details_online_status)
        onlineStatus.text = pump.tankName
        lastSeen.text = pump.lastSeen
        backPressButton = view.findViewById(R.id.pump_details_back_button)

        submitButton = view.findViewById(R.id.submit_pump_button)
        backPressButton.setOnClickListener {
            findNavController().popBackStack()
        }
        submitButton.setOnClickListener {
            val manualReading = manualReading.text.toString().toDouble()
            val digitalReading = digitalReading.text.toString().toDouble()
            val price = price.text.toString().toDouble()

            val tempPump = PumpToSend(
                dbId = 0,
                pumpId = pump.id,
                manualReading = manualReading,
                digitalReading = digitalReading,
                displayName = pump.displayName,
                price = price,
                totalMultiplier = 0.00,
                amountMultiplier = 0.00,
                volumeMultiplier = 0.00
            )
            pumpDetailsViewModel.addPump(tempPump)
            Toast.makeText(context,"Pump details saved.",Toast.LENGTH_SHORT).show()
            findNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.PUMP_EDITED,pump.id)
            findNavController().popBackStack()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CustomActions.closeKeyboard(requireActivity() as AppCompatActivity,requireContext())

    }
}