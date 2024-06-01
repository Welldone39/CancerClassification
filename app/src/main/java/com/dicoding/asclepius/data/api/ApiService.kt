package com.dicoding.asclepius.data.api


import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("q") query: String,
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
    ): retrofit2.Call<NewsResponse>
}