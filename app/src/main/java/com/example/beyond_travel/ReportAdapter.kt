package com.example.beyond_travel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private var reportsList: List<Report> = ArrayList()

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val currentReport = reportsList[position]
        holder.descriptionTextView.text = currentReport.description
        holder.locationTextView.text = "Lat: ${currentReport.latitude}, Long: ${currentReport.longitude}"

        // Cargar la imagen utilizando Picasso
        Picasso.get().load(currentReport.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    fun setData(reports: List<Report>) {
        this.reportsList = reports
        notifyDataSetChanged()
    }
}
