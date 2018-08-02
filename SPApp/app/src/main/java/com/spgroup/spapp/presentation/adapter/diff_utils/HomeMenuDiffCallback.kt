package com.spgroup.spapp.presentation.adapter.diff_utils

import android.support.v7.util.DiffUtil
import com.spgroup.spapp.domain.model.TopLevelServiceCategory

class HomeMenuDiffCallback(
        val oldList: List<TopLevelServiceCategory>,
        val newList: List<TopLevelServiceCategory>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldId = oldList[oldPos].id
        val newId = newList[newPos].id
        return oldId == newId
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldObj = oldList[oldPos]
        val newObj = newList[newPos]

        return oldObj.id == newObj.id && oldObj.name.equals(newObj.name)
    }
}