package com.dicoding.asclepius.model

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repository.RepositoryHistory

class ViewModelHistory(application: Application): ViewModel() {
    private val repositoryHistory = RepositoryHistory(application)

    fun getHistory() = repositoryHistory.get()
}