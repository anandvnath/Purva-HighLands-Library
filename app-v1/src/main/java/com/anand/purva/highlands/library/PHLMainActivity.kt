package com.anand.purva.highlands.library

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anand.purva.highlands.library.books.BookViewModel
import com.anand.purva.highlands.library.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PHLMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        var menuItem = menu.findItem(R.id.search)
        var searchView = menuItem.actionView as SearchView
        searchView.isIconified = false
        searchView.isIconifiedByDefault = false
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