package com.example.new_practice.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.ClickChannelListener
import com.example.new_practice.storage.entities.Epg
import com.example.new_practice.R
import com.example.new_practice.diffUtils.ChannelDiffUtilCallback
import com.example.new_practice.diffUtils.EpgDiffUtilCallback
import com.example.new_practice.viewHolders.ViewChannelHolder

class ChannelAdapter(
    context: Context,
    epgs: List<Epg>,
    clickListener: ClickChannelListener,
    favClickListener: ClickChannelListener
) : RecyclerView.Adapter<ViewChannelHolder?>() {
    private val clickListener: ClickChannelListener
    private val context: Context
    private var channels: List<Channel> = listOf()
    private var epgs: List<Epg> = listOf()
    private val favClickListener: ClickChannelListener

    init {
        this.clickListener = clickListener
        this.context = context
        this.epgs = epgs
        this.favClickListener = favClickListener
    }

    fun setChannels(newChannels: List<Channel>): DiffUtil.DiffResult {
        val diffUtil = ChannelDiffUtilCallback(channels, newChannels)
        this.channels = newChannels
        return DiffUtil.calculateDiff(diffUtil)
    }

    fun setEpgs(newEpgs: List<Epg>): DiffUtil.DiffResult {
        val diffUtil = EpgDiffUtilCallback(epgs, newEpgs)
        this.epgs = newEpgs
        return DiffUtil.calculateDiff(diffUtil)
    }

    fun getChannels(): List<Channel> {
        return this.channels
    }

    fun getEpgs(): List<Epg> {
        return this.epgs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewChannelHolder {
        return ViewChannelHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.channel_field, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewChannelHolder, position: Int) {
        val channelItem: Channel = channels[position]
        val epgItem: Epg? = epgs.firstOrNull { it.channelId == channelItem.id }
        val urlImage = Uri.parse(channelItem.image)
        holder.getNameChannel().text = channelItem.name
        holder.getTvShow().text = epgItem?.title
        Glide.with(context)
            .load(urlImage)
            .error(R.drawable.image_not_supported)
            .into(holder.getIconChannel())
        if (channelItem.isFavorite) {
            holder.getFavoriteView().setImageResource(R.drawable.star_selected)
        } else {
            holder.getFavoriteView().setImageResource(R.drawable.star_unselected)
        }
        holder.getFavoriteView().setOnClickListener { favClickListener.invoke(channelItem) }
        holder.itemView.setOnClickListener { clickListener.invoke(channelItem) }
    }

    override fun getItemCount(): Int {
        return channels.size
    }
}