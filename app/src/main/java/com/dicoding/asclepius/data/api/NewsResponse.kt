package com.dicoding.asclepius.data.api

import com.google.gson.annotations.SerializedName

data class NewsResponse(
	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>,

	@field:SerializedName("status")
	val status: String
)

data class ArticlesItem(
	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("urlToImage")
	val urlToImage: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("description")
	val description: String?,
)
