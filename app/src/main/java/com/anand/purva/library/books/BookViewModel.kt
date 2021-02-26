package com.anand.purva.library.books

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anand.purva.library.books.BookManager
import com.anand.purva.library.books.SearchResult
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : ViewModel() {
    val searchResult = MutableLiveData<SearchResult>()
    val bookManager = BookManager(viewModelScope, application)
    init {
        viewModelScope.launch {
            searchResult.postValue(bookManager.allBooks)
        }
    }
    fun onQuery(query: String) {
        viewModelScope.launch {
            if (query.length > 2) {
                val result = bookManager.search(query)
                searchResult.postValue(result)
            } else {
                searchResult.postValue(bookManager.allBooks)
            }
        }
    }
}