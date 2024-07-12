package com.example.new_practice.app.viewHolders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.R

class ViewIconHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val iconChannel: ImageView

    init {
        iconChannel = itemView.findViewById<ImageView>(R.id.channelIconItem)
    }

    fun getIconChannel(): ImageView {
        return iconChannel
    }
}