package com.example.new_practice.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.new_practice.storage.entities.Epg

class EpgDiffUtilCallback(
    private val oldChannels: List<Epg>,
    private val newChannels: List<Epg>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldChannels.size
    }

    override fun getNewListSize(): Int {
        return newChannels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: Epg = oldChannels[oldItemPosition]
        val new: Epg = newChannels[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: Epg = oldChannels[oldItemPosition]
        val new: Epg = newChannels[newItemPosition]
        return old.title == new.title && old.timeStart == new.timeStart
                && old.timeStop == new.timeStop
    }
}