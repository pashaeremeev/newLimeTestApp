package com.example.new_practice.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.new_practice.app.ClickIconListener
import com.example.new_practice.R
import com.example.new_practice.app.diffUtils.ChannelDiffUtilCallback
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.app.presentation.viewHolders.ViewIconHolder

class ChannelIconAdapter(context: Context,
clickListener: ClickIconListener
): RecyclerView.Adapter<ViewIconHolder?>() {

    private val context: Context
    private var channels: List<ChannelModel> = listOf()
    private val clickListener: ClickIconListener
    private var selectedIndex: Int? = null

    init {
        this.context = context
        this.clickListener = clickListener
    }

    fun setChannels(newChannels: List<ChannelModel>): DiffUtil.DiffResult {
        val diffUtil = ChannelDiffUtilCallback(channels, newChannels)
        this.channels = newChannels
        return DiffUtil.calculateDiff(diffUtil)
    }

    fun setSelected(position: Int) {
        val oldSelectedPosition = selectedIndex
        selectedIndex = position
        if (oldSelectedPosition != null) {
            notifyItemChanged(oldSelectedPosition)
        }
        notifyItemChanged(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewIconHolder {
        return ViewIconHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.channel_icon_item,
                    parent,
                    false))
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    override fun onBindViewHolder(holder: ViewIconHolder, position: Int) {
        val channelItem: ChannelModel = channels[position]
        Glide.with(context)
            .load(channelItem.image)
            .error(R.drawable.image_not_supported)
            .into(holder.getIconChannel())
        holder.itemView.setOnClickListener { clickListener.invoke(channelItem) }
        if (position == selectedIndex) {
            holder.itemView.setBackgroundResource(R.drawable.icon_selected_bg)
        } else {
            holder.itemView.setBackgroundResource(android.R.drawable.screen_background_light_transparent)
        }
    }
}