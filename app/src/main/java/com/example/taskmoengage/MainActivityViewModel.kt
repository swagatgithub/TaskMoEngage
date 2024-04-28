package com.example.taskmoengage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel()
{
    private val addressInput = MutableLiveData<StaticNewsResponse>()
    val addressInputNotMutable : LiveData<StaticNewsResponse> = addressInput
    private lateinit var newsArticleData : StaticNewsResponse

    fun startFetchingArticle()
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                newsArticleData = newsRepository.fetchArticle()
            }
            addressInput.value = newsArticleData
        }
    }
}