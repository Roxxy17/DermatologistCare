package com.example.dermatologistcare.ui.resource

data class ResourceResponse(
    val article_id: Int,
    val title: String,
    val author: String,
    val source_name: String,
    val publication_date: String,
    val abstract: String,
    val category: String,
    val source_type: String,
    val url: String,
    val created_at: String
)