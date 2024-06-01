package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.AdapterHistory
import com.dicoding.asclepius.data.entity.History
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.model.ViewModelFactory
import com.dicoding.asclepius.model.ViewModelHistory

class HistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelHistory = obtainViewModel(this)
        viewModelHistory.getHistory().observe(this) { histories ->
            if (histories.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            }
            setHistoryData(histories)
        }
    }

    private fun setHistoryData(history: List<History>) {
        binding.listHistory.layoutManager = LinearLayoutManager(this)

        val adapterHistory = AdapterHistory()
        adapterHistory.setHistories(history)
        binding.listHistory.adapter = adapterHistory

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): ViewModelHistory {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ViewModelHistory::class.java]

    }
}