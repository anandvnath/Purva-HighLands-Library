package com.anand.purva.highlands.library.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.anand.purva.highlands.library.R
import com.anand.purva.highlands.library.books.data.SearchResult
import com.anand.purva.highlands.library.databinding.FragmentSearchResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BookSearchResultFragment : Fragment() {
    private val viewModel: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchResultBinding
    private var searchResult: SearchResult? = null
    private val adapter = BookViewAdapter()
    private lateinit var navView: BottomNavigationView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchResultBinding.bind(view)

        binding.searchResultsView.adapter = adapter
        binding.searchResultsView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
        navView = binding.navView
        navView.setOnNavigationItemSelectedListener { menu ->
            displayResults(menu.itemId)
            true
        }
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResult ->
            this.searchResult = searchResult
            displayResults(navView.selectedItemId)
        })
    }

    private fun updateBadges() {
        navView.menu.iterator().forEach { menuItem ->
            val badgeCount = getBadgeCount(menuItem)
            val badge = navView.getOrCreateBadge(menuItem.itemId)
            badge.isVisible = true
            badge.number = badgeCount
        }
    }

    private fun getBadgeCount(menuItem: MenuItem): Int {
        searchResult?.let {
            return when (menuItem.itemId) {
                R.id.action_author -> it.authorsBooks.size
                R.id.action_title -> it.titleBooks.size
                R.id.action_category -> it.categoryBooks.size
                R.id.action_all -> it.authorsBooks.size + it.titleBooks.size + it.categoryBooks.size
                else -> 0
            }
        }
        return 0;
    }


    private fun displayResults(@IdRes selection: Int) {
        searchResult?.let {
            when (selection) {
                R.id.action_author -> adapter.data = mutableSetOf(it.authorsBooks).flatten().sortedBy { it.author }
                R.id.action_title -> adapter.data = mutableSetOf(it.titleBooks).flatten().sortedBy { it.title }
                R.id.action_category -> adapter.data = mutableSetOf(it.categoryBooks).flatten().sortedBy { it.category }
                R.id.action_all -> adapter.data = mutableSetOf(it.authorsBooks, it.titleBooks, it.categoryBooks).flatten().sortedBy { it.title }
            }
        }
        updateBadges()
    }
}

