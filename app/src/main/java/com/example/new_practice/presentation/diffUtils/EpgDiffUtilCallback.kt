package com.example.new_practice.presentation.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.new_practice.data.storage.entities.Epg
import com.example.new_practice.domain.models.EpgModel

class EpgDiffUtilCallback(
    private val oldChannels: List<EpgModel>,
    private val newChannels: List<EpgModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldChannels.size
    }

    override fun getNewListSize(): Int {
        return newChannels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: EpgModel = oldChannels[oldItemPosition]
        val new: EpgModel = newChannels[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: EpgModel = oldChannels[oldItemPosition]
        val new: EpgModel = newChannels[newItemPosition]
        return old.title == new.title && old.timeStart == new.timeStart
                && old.timeStop == new.timeStop
    }
}