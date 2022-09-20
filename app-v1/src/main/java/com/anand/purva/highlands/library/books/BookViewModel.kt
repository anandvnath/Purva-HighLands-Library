package com.anand.purva.highlands.library.books

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anand.purva.highlands.library.books.data.IBookManager
import com.anand.purva.highlands.library.books.data.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val bookManager: IBookManager) : ViewModel() {
    val searchResult = MutableLiveData<SearchResult>()
    private val allBooksResultFlow = bookManager.initialize()

    init {
        viewModelScope.launch {
            allBooksResultFlow.collect {
                searchResult.postValue(it)
            }
        }
    }

    fun onQuery(query: String?) {
        viewModelScope.launch {
            if (query == null || query.length < 3) {
                allBooksResultFlow.collect {
                    searchResult.postValue(it)
                }
            } else {
                val result = bookManager.search(query)
                searchResult.postValue(result)
            }
        }
    }
}