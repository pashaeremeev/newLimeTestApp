package com.example.new_practice.app.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.domain.models.ChannelModel

class ChannelDiffUtilCallback(
    private val oldChannels: List<ChannelModel>,
    private val newChannels: List<ChannelModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldChannels.size
    }

    override fun getNewListSize(): Int {
        return newChannels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: ChannelModel = oldChannels[oldItemPosition]
        val new: ChannelModel = newChannels[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: ChannelModel = oldChannels[oldItemPosition]
        val new: ChannelModel = newChannels[newItemPosition]
        return old.isFavorite == new.isFavorite
                && old.name == new.name && old.image == new.image
    }

}