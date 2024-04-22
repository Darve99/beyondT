package com.example.beyond_travel

import  android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReportAdapter(private val reportList: List<ReportItema>) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item_layout, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentItem = reportList[position]
        Glide.with(holder.itemView.context).load(currentItem.photoUri).into(holder.imageView)
        holder.descriptionTextView.text = currentItem.description
    }

    override fun getItemCount() = reportList.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.reportImageView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.reportDescriptionTextView)
    }
}
