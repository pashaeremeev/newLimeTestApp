package com.example.new_practice.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.new_practice.repos.EpgRepo
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.storage.entities.Epg

class ChannelDiffUtilCallback(
    private val oldChannels: List<Channel>,
    private val newChannels: List<Channel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldChannels.size
    }

    override fun getNewListSize(): Int {
        return newChannels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: Channel = oldChannels[oldItemPosition]
        val new: Channel = newChannels[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: Channel = oldChannels[oldItemPosition]
        val new: Channel = newChannels[newItemPosition]
        return old.isFavorite == new.isFavorite
                && old.name == new.name && old.image == new.image
    }

}