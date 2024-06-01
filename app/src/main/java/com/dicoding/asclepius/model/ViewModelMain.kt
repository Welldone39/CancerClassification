package com.dicoding.asclepius.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.api.ApiConfig
import com.dicoding.asclepius.data.api.ArticlesItem
import com.dicoding.asclepius.data.api.NewsResponse
import com.dicoding.asclepius.data.entity.History
import com.dicoding.asclepius.data.repository.RepositoryHistory
import retrofit2.Call
import retrofit2.Response

class ViewModelMain(application: Application): ViewModel(){
    private val repositoryHistory: RepositoryHistory = RepositoryHistory(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _news = MutableLiveData<List<ArticlesItem>>()
    val news: LiveData<List<ArticlesItem>> = _news

    init {
        getNews()
    }

    fun createHistory(history: History) = repositoryHistory.create(history)

    private fun getNews() {
        _isLoading.value = true
        _news.value = arrayListOf()

        val client = ApiConfig.getApiService().getTopHeadlines("cancer")
        client.enqueue(object : retrofit2.Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _news.value = response.body()?.articles
                    _news.value = _news.value?.filter { it.title != "[Removed]" }
                }
                else Log.e(TAG, "onFailure: ${response.message()}")

            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }



    companion object {
        private const val TAG = "MainViewModel"
    }


}