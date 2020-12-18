package com.epump.epumpterminal.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController

import androidx.recyclerview.widget.RecyclerView
import com.epump.epumpterminal.R
import com.epump.epumpterminal.models.Pump
import java.io.File

class FileAdapter(private val mContext: Context) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    private var files = emptyList<File>()

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.file_name)
        val fileCard : CardView = itemView.findViewById(R.id.file_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.file_list_item, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.fileName.text = files[position].name
        holder.fileCard.setOnClickListener {
        }

    }

    override fun getItemCount(): Int {
        return files.size
    }

    internal fun setPumps(file :List<File> ){
        this.files = file
        notifyDataSetChanged()
    }

}