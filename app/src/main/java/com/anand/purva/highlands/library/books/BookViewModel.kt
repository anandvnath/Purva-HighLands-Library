package com.anand.purva.highlands.library.books

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : ViewModel() {
    val searchResult = MutableLiveData<SearchResult>()
    val bookManager = BookManager(viewModelScope, application, this::callback)

    private fun callback(done: Boolean) {
        if (done) {
            searchResult.postValue(bookManager.allBooks)
        }
    }

    fun onQuery(query: String?) {
        viewModelScope.launch {
            if (query == null || query.length < 3) {
                searchResult.postValue(bookManager.allBooks)
            } else {
                val result = bookManager.search(query)
                searchResult.postValue(result)
            }
        }
    }
}