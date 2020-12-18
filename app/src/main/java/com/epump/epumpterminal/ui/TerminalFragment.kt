package com.epump.epumpterminal.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.epump.epumpterminal.R
import com.epump.epumpterminal.tcp.TcpClient
import com.epump.epumpterminal.utils.Constants

class TerminalFragment : Fragment() {

    lateinit var fetchFileButton: CardView
    lateinit var sendEditText: EditText
    lateinit var receiveEditText: EditText
    lateinit var sendToPumpButton: Button
    lateinit var wifiManager: WifiManager


    //    private val client: TcpClient = TcpClient()

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_terminal, container, false)
        fetchFileButton = view.findViewById(R.id.fetch_files)
        sendEditText = view.findViewById(R.id.send_to_pump_edit_text)
        wifiManager =
            requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        receiveEditText = view.findViewById(R.id.receive_from_pump_edit_text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            startActivityForResult(panelIntent, 0)
        }

        sendToPumpButton = view.findViewById(R.id.send_to_pump_button)
        sendToPumpButton.setOnClickListener {
//            client.write(sendEditText.text.toString())
        }
//        receiveEditText.setText(client.read())
        fetchFileButton.setOnClickListener {
            findNavController().navigate(R.id.file_bottom_sheet)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled)
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("ssid")
                ?.observe(viewLifecycleOwner,
                    Observer {
                        connectToWifi(it)
                    })


            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context?.registerReceiver(wifiScanReceiver, intentFilter)
            val success = wifiManager.startScan()
            if (!success) {
                scanFailure()
            }

        }


        return view
    }

    private fun connectToWifi(ssid: String) {
        val wifiConfiguration = WifiConfiguration()
        wifiConfiguration.SSID = "\"${ssid}\""
        wifiConfiguration.preSharedKey = "\"${Constants.PASSWORD}\""
        val netId = wifiManager.addNetwork(wifiConfiguration)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
    }

    private fun scanFailure() {
        val results = wifiManager.scanResults
        Toast.makeText(requireContext(), "Cannot access wifi results", Toast.LENGTH_SHORT).show()

    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        val bundle = bundleOf("results" to results)
        context?.unregisterReceiver(wifiScanReceiver)
//        findNavController().navigate(R.id.wifi_bottom_sheet, bundle)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            wifiManager.isWifiEnabled = false
            context?.unregisterReceiver(wifiScanReceiver)
        }
    }

}