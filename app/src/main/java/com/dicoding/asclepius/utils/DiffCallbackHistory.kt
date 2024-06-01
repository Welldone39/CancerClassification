package com.dicoding.asclepius.utils

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.entity.History

class DiffCallbackHistory(private val listOldHistory: List<History>, private val listNewHistory: List<History>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = listOldHistory.size
    override fun getNewListSize(): Int = listNewHistory.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listOldHistory[oldItemPosition].id == listNewHistory[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val historyOld = listOldHistory[oldItemPosition]
        val historyNew = listNewHistory[newItemPosition]
        return historyOld.id == historyNew.id && historyOld.category == historyNew.category
    }
}