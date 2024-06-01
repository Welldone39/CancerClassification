package com.dicoding.asclepius.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.adapter.AdapterHistory.ViewHolderHistory
import com.dicoding.asclepius.data.entity.History
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.utils.DiffCallbackHistory
import com.dicoding.asclepius.utils.Number

class AdapterHistory: RecyclerView.Adapter<AdapterHistory.ViewHolderHistory>() {
    private val Histories = ArrayList<History>()

    fun setHistories(Histories: List<History>) {
        val diffCalback = DiffCallbackHistory(this.Histories, Histories)
        val diffResult = DiffUtil.calculateDiff(diffCalback)
        this.Histories.clear()
        this.Histories.addAll(Histories)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderHistory(binding)
    }

    override fun getItemCount(): Int = Histories.size

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        holder.bind(Histories[position])
    }


    inner class ViewHolderHistory(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            with(binding) {
                val bitmap = BitmapFactory.decodeByteArray(history.image, 0, history.image.size)
                historyImage.setImageBitmap(bitmap)
                "Category: ${history.category}".also { historyCategory.text = it }
                "Score: ${Number.decimalToPercentage(history.score)}".also { historyScore.text = it }
            }
        }

    }
}