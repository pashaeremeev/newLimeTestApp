package com.example.new_practice.presentation.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.R

class ViewChannelHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val iconChannel: ImageView
    private val favoriteView: ImageView
    private val nameChannel: TextView
    private val tvShow: TextView

    init {
        iconChannel = itemView.findViewById<ImageView>(R.id.iconChannel)
        favoriteView = itemView.findViewById<ImageView>(R.id.isFavoriteView)
        nameChannel = itemView.findViewById<TextView>(R.id.nameChannel)
        tvShow = itemView.findViewById<TextView>(R.id.nameTvShow)
    }

    fun getIconChannel(): ImageView {
        return iconChannel
    }

    fun getFavoriteView(): ImageView {
        return favoriteView
    }

    fun getNameChannel(): TextView {
        return nameChannel
    }
    fun getTvShow(): TextView {
        return tvShow
    }

}