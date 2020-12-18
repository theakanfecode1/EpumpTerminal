package com.epump.epumpterminal.ui

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.PumpToSend
import com.epump.epumpterminal.ui.adapters.SummaryAdapter
import com.epump.epumpterminal.utils.CustomActions
import com.epump.epumpterminal.utils.Resource
import com.epump.epumpterminal.viewmodel.SummaryViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@AndroidEntryPoint
class SummaryFragment : Fragment() {
    lateinit var backPressButton: ImageButton
    lateinit var clearButton: Button
    lateinit var submitButton: Button
    private val REQUEST_CODE = 1
    lateinit var loaderDialog : Dialog
    lateinit var summaryRecyclerView: RecyclerView
    lateinit var summaryAdapter: SummaryAdapter
    lateinit var signaturePad: SignaturePad
    lateinit var stationName: TextView
    lateinit var managerName: EditText
    lateinit var managerPhone: EditText
    private var isSigned: Boolean = false
    private var chosenPumps: List<PumpToSend> = mutableListOf()
    private val PERMISSIONS_STORAGE =
        arrayOf<String>(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

    private val summaryViewModel: SummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        summaryRecyclerView = view.findViewById(R.id.summary_recycler_view)
        signaturePad = view.findViewById(R.id.signature_pad)
        managerName = view.findViewById(R.id.manager_name)
        signaturePad.setPenColor(Color.BLACK)
        stationName = view.findViewById(R.id.summary_station_name)
        managerPhone = view.findViewById(R.id.manager_phone)
        clearButton = view.findViewById(R.id.clear_signature)
        submitButton = view.findViewById(R.id.submit_summary_details)
        loaderDialog = CustomActions.loader("Submitting Pumps", activity as AppCompatActivity)
        askForPermissions()
        clearButton.setOnClickListener {
            signaturePad.clear()
        }
        stationName.text = SummaryFragmentArgs.fromBundle(requireArguments()).stationName
        submitButton.setOnClickListener {
            if (chosenPumps.isNotEmpty()) {
                if (isSigned) {
                    if (managerName.text.isNotEmpty()) {
                        loaderDialog.show()
                        addJpgSignatureToGallery(signature = signaturePad.signatureBitmap)
                    } else {
                        Snackbar.make(it, "Enter manager name", Snackbar.LENGTH_SHORT).show()

                    }
                } else {
                    Snackbar.make(
                        it,
                        "Kindly sign, you cannot without signature",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                Snackbar.make(it, "No Pump selected", Snackbar.LENGTH_SHORT).show()
            }
        }
        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                isSigned = true
            }

            override fun onSigned() {
                isSigned = true
            }

            override fun onClear() {
                isSigned = false
            }

        })
        summaryAdapter = SummaryAdapter(view.context)
        backPressButton = view.findViewById(R.id.summary_list_back_button)

        backPressButton.setOnClickListener {
            findNavController().popBackStack()
        }

        summaryViewModel.pumps.observe(viewLifecycleOwner, Observer {
            summaryRecyclerView.apply {
                adapter = summaryAdapter
                layoutManager = LinearLayoutManager(view.context)
            }
            summaryAdapter.setPumps(it)
            chosenPumps = it
        })
        return view

    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionsReadAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions() {
        if (!isPermissionsAllowed()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        PERMISSIONS_STORAGE,
                        REQUEST_CODE
                    )
                }
                return
            }

        }
    }

    fun getAlbumStorageDir(albumName: String?): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
        }
        return file
    }


    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {

        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        var float: Float = 0.00.toFloat()
        val paint = Paint()
        paint.color = Color.BLACK
        canvas.drawBitmap(bitmap, float, float, null)

        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
//        val namePart: RequestBody = RequestBody.create(MultipartBody.FORM,  photo)

    }

    fun addJpgSignatureToGallery(signature: Bitmap): Boolean {
        var result = false
        try {
            val photo = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature, photo)
            Log.d("TAG", "addJpgSignatureToGallery: ")
            sendToServer(photo, managerName.text.toString(), managerPhone.text.toString())
            scanMediaFile(photo)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri: Uri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        requireActivity().sendBroadcast(mediaScanIntent)
    }

    private fun subscribeObserver() {
        summaryViewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            loaderDialog.dismiss()
            when (dataState.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(context, "Pumps successfully submitted", Toast.LENGTH_LONG).show()
                    summaryViewModel.clearPumpDb()
                    findNavController().popBackStack()
                }
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    Toast.makeText(context, dataState.message, Toast.LENGTH_LONG).show()

                }
            }



        })
    }

    private fun sendToServer(file: File, managerName: String, phoneNumber: String) {
        val reqFile = file
            .asRequestBody("image/jpeg".toMediaTypeOrNull())
        val manager = managerName.toRequestBody("text/plain".toMediaTypeOrNull())
        val branch = PumpFragment.branchId.toRequestBody("text/plain".toMediaTypeOrNull())
        val phone = phoneNumber
            .toRequestBody("text/plain".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("signatureFile", file.name, reqFile)

        val json = Gson().toJson(chosenPumps).toRequestBody("text/plain".toMediaTypeOrNull())
        summaryViewModel.submitPumps(json, branch, manager, phone, body)
        subscribeObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CustomActions.closeKeyboard(requireActivity() as AppCompatActivity,requireContext())

    }

}