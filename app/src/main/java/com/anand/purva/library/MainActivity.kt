package com.anand.purva.library

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.anand.purva.library.databinding.ActivityMainBinding
import com.anand.purva.library.books.BookViewModel
import com.anand.purva.library.books.BookViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: BookViewModelFactory
    private lateinit var viewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = BookViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        var menuItem = menu.findItem(R.id.search)
        var searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("LibApp", "onQueryTextChange query: $newText")
                viewModel.onQuery(newText)
                return false
            }

        })
        return true
    }
}