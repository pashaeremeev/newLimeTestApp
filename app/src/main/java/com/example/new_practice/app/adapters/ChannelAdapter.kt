package com.example.new_practice.app.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.new_practice.app.ClickChannelListener
import com.example.new_practice.R
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.app.diffUtils.ChannelDiffUtilCallback
import com.example.new_practice.app.diffUtils.EpgDiffUtilCallback
import com.example.new_practice.app.presentation.viewHolders.ViewChannelHolder

class ChannelAdapter(
    context: Context,
    epgs: List<EpgModel>,
    clickListener: ClickChannelListener,
    favClickListener: ClickChannelListener
) : RecyclerView.Adapter<ViewChannelHolder?>() {
    private val clickListener: ClickChannelListener
    private val context: Context
    private var channels: List<ChannelModel> = listOf()
    private var epgs: List<EpgModel> = listOf()
    private val favClickListener: ClickChannelListener

    init {
        this.clickListener = clickListener
        this.context = context
        this.epgs = epgs
        this.favClickListener = favClickListener
    }

    fun setChannels(newChannels: List<ChannelModel>): DiffUtil.DiffResult {
        val diffUtil = ChannelDiffUtilCallback(channels, newChannels)
        this.channels = newChannels
        return DiffUtil.calculateDiff(diffUtil)
    }

    fun setEpgs(newEpgs: List<EpgModel>): DiffUtil.DiffResult {
        val diffUtil = EpgDiffUtilCallback(epgs, newEpgs)
        this.epgs = newEpgs
        return DiffUtil.calculateDiff(diffUtil)
    }

    fun getChannels(): List<ChannelModel> {
        return this.channels
    }

    fun getEpgs(): List<EpgModel> {
        return this.epgs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewChannelHolder {
        return ViewChannelHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.channel_field, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewChannelHolder, position: Int) {
        val channelItem: ChannelModel = channels[position]
        val epgItem: EpgModel? = epgs.firstOrNull { it.channelId == channelItem.id }
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