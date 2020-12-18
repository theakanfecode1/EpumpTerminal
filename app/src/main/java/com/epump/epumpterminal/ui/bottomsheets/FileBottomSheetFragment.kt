package com.epump.epumpterminal.ui.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.ui.adapters.FileAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FileBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var fileRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.file_list, container, false)
        fileRecyclerView = view.findViewById(R.id.file_recycler_view)
        fileRecyclerView.apply {
            adapter = FileAdapter(view.context)
            layoutManager = LinearLayoutManager(view.context)
        }

        return view
    }
}