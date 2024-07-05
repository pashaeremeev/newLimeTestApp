package com.example.new_practice.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.ClickQualityListener
import com.example.new_practice.Quality
import com.example.new_practice.R
import com.example.new_practice.viewHolders.QualityHolder

class QualityAdapter(
    context: Context,
    qualities: List<Quality>,
    clickListener: ClickQualityListener
) :
    RecyclerView.Adapter<QualityHolder?>() {
    private val clickListener: ClickQualityListener
    private val context: Context
    private val qualities: List<Quality>

    init {
        this.clickListener = clickListener
        this.context = context
        this.qualities = qualities
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QualityHolder {
        return QualityHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.quality_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QualityHolder, position: Int) {
        val item: Quality = qualities[position]
        if (item.height == -1) {
            holder.qualityText.text = "AUTO"
        } else {
            holder.qualityText.text = item.height.toString() + "p"
        }
        if (item.isCurrent != 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#0077FF"))
            holder.qualityText.setTextColor(Color.WHITE)
        }
        holder.itemView.setOnClickListener {
            clickListener.invoke(
                item
            )
        }
    }

    override fun getItemCount(): Int {
        return qualities.size
    }
}