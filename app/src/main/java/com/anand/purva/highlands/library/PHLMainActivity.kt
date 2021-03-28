package com.anand.purva.highlands.library

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anand.purva.highlands.library.books.BookViewModel
import com.anand.purva.highlands.library.books.BookViewModelFactory
import com.anand.purva.highlands.library.databinding.ActivityMainBinding

class PHLMainActivity : AppCompatActivity() {
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