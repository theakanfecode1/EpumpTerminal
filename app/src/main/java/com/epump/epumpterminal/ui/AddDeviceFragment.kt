package com.epump.epumpterminal.ui

import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Device
import com.epump.epumpterminal.utils.CustomActions
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.viewmodel.AddDeviceViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddDeviceFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var backPressButton: ImageButton
    lateinit var deviceTypeSpinner: Spinner
    lateinit var arrayAdapter: ArrayAdapter<String>
    lateinit var probeAddress: EditText
    lateinit var deviceId: EditText
    lateinit var phoneNumber: EditText
    lateinit var linearLayoutCheck: LinearLayout
    lateinit var addDeviceButton: Button
    lateinit var loaderDialog: Dialog
    private val titles: MutableList<String> = mutableListOf("Tank", "Pump")
    private val addDeviceViewModel: AddDeviceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_device, container, false)
        backPressButton = view.findViewById(R.id.back_button_add_device)
        probeAddress = view.findViewById(R.id.probe_address)
        phoneNumber = view.findViewById(R.id.phone_number)
        deviceId = view.findViewById(R.id.device_id)
        addDeviceButton = view.findViewById(R.id.add_device_button)
        loaderDialog = CustomActions.loader("Adding Devices..", activity as AppCompatActivity)
        addDeviceButton.setOnClickListener {
            generateObjects()
            subscribeSubmitObservers()
        }
        deviceTypeSpinner = view.findViewById(R.id.device_type)
        linearLayoutCheck = view.findViewById(R.id.linear_layout_add_device)
        backPressButton.setOnClickListener {
            findNavController().popBackStack()
        }
        arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, titles)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deviceTypeSpinner.adapter = arrayAdapter
        deviceTypeSpinner.onItemSelectedListener = this

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (position != 0) {
            subscribePumpsObservers()
            probeAddress.visibility = View.GONE

        } else {
            subscribeTankObservers()
            probeAddress.visibility = View.VISIBLE

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun subscribePumpsObservers() {
        addDeviceViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    linearLayoutCheck.removeAllViews()
                    dataState.data!!.forEach { pump ->
                        val checkBox =
                            CheckBox(ContextThemeWrapper(view?.context, R.style.CheckBoxStyle))
                        checkBox.apply {
                            text = pump.name
                            tag = pump.id
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                        linearLayoutCheck.addView(checkBox)
                    }

                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                }
            }
        })
    }

    private fun subscribeTankObservers() {
        addDeviceViewModel.tankDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    linearLayoutCheck.removeAllViews()
                    dataState.data!!.forEach { tank ->
                        val checkBox =
                            CheckBox(ContextThemeWrapper(view?.context, R.style.CheckBoxStyle))
                        checkBox.apply {
                            text = tank.name
                            tag = tank.id
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                        linearLayoutCheck.addView(checkBox)
                    }
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {

                }
            }
        })
    }

    private fun subscribeSubmitObservers() {
        addDeviceViewModel.addDeviceDataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    loaderDialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Devices submitted successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
                Resource.Status.LOADING -> {
                    loaderDialog.show()
                }
                Resource.Status.ERROR -> {
                    loaderDialog.dismiss()

                }
            }
        })
    }

    private fun generateObjects() {
        val count = linearLayoutCheck.childCount
        var device = Device()
        var idS = ""
        var checkIfACheckBoxIsSelected = false
        for (x in 0 until count) {
            val view = linearLayoutCheck.getChildAt(x) as CheckBox
            if (view.isChecked) {
                checkIfACheckBoxIsSelected = true
                device = if (deviceTypeSpinner.selectedItemPosition == 0) {
                    idS += if (x == (count - 1)) {
                        view.tag.toString()
                    } else {
                        view.tag.toString() + ","
                    }
                    Device(
                        deviceType = "Tank",
                        branchId = PumpFragment.branchId,
                        deviceId = deviceId.text.toString(),
                        tankId = idS,
                        probeAddress = probeAddress.text.toString(),
                        phoneNumber = phoneNumber.text.toString()
                    )
                } else {
                    idS += if (x == (count - 1)) {
                        view.tag.toString()
                    } else {
                        view.tag.toString() + ","
                    }
                    Device(
                        deviceType = "Pump",
                        branchId = PumpFragment.branchId,
                        deviceId = deviceId.text.toString(),
                        pumpId = idS,
                        phoneNumber = phoneNumber.text.toString()
                    )
                }

            }
        }
        if (checkIfACheckBoxIsSelected) {
            addDeviceViewModel.addDevice(device)
        } else {
            Toast.makeText(requireContext(), "Select a device", Toast.LENGTH_LONG).show()
        }
    }

//    private fun checkCommaSeparationOfProbeAddress(probeAddress : String): Boolean {
//        val count = linearLayoutCheck.childCount
//        if (deviceTypeSpinner.selectedItemPosition == 0) {
//            var index = 0
//            for (x in 0 until count) {
//                val view = linearLayoutCheck.getChildAt(x) as CheckBox
//                if (view.isChecked) {
//                    index++
//                }
//            }
//            if (index == 0){
//                if (probeAddress.contains(",")){
//                    return true
//                }
//            }else{
//                return if (probeAddress.contains(",")){
//                    false
//                }else{
//                    val splitter = probeAddress.split(",")
//                    splitter.size == (index)
//                }
//            }
//
//        }
//
//        return false
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        CustomActions.closeKeyboard(requireActivity() as AppCompatActivity, requireContext())

    }

}