package com.dicoding.asclepius.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.entity.History
import com.dicoding.asclepius.data.room.AsclepiusDatabase
import com.dicoding.asclepius.data.room.HistoryDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RepositoryHistory(application: Application) {
    private val daoHistory: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AsclepiusDatabase.getDatabase(application)
        daoHistory = db.historyDao()
    }

    fun get(): LiveData<List<History>> = daoHistory.get()

    fun create(history: History) {
        executorService.execute { daoHistory.insert(history) }
    }
}