
package com.example.dermatologistcare.ui.resource

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val apiService = ResourceApiConfig.provideResourceApiService()

    private val _articlesState = mutableStateOf<ArticlesUiState>(ArticlesUiState.Loading)
    val articlesState: State<ArticlesUiState> = _articlesState

    fun fetchArticles() {
        viewModelScope.launch {
            _articlesState.value = ArticlesUiState.Loading
            try {
                val response = apiService.getResource()
                if (response.isSuccessful) {
                    _articlesState.value = ArticlesUiState.Success(response.body() ?: emptyList())
                } else {
                    _articlesState.value = ArticlesUiState.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                _articlesState.value = ArticlesUiState.Error("Network error: Check your internet Connection")
            }
        }
    }
}

// Sealed class for UI states
sealed class ArticlesUiState {
    object Loading : ArticlesUiState()
    data class Success(val articles: List<ResourceResponse>) : ArticlesUiState()
    data class Error(val message: String) : ArticlesUiState()
}