package com.example.new_practice.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.R

class QualityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val qualityText: TextView

    init {
        qualityText = itemView.findViewById<TextView>(R.id.qualityItem)
    }
}