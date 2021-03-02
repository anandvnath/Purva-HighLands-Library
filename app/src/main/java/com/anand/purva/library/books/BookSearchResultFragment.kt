package com.anand.purva.library.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.activityViewModels
import com.anand.purva.library.R
import com.anand.purva.library.databinding.FragmentSearchResultBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BookSearchResultFragment : Fragment() {
    private val viewModel: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchResultBinding
    private var searchResult: SearchResult? = null
    private val adapter = BookViewAdapter()

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
        val navView = binding.navView
        navView.setOnNavigationItemSelectedListener { menu ->
            displayResults(menu.itemId)
            true
        }
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResult ->
            this.searchResult = searchResult
            displayResults(navView.selectedItemId)
        })
    }

    private fun displayResults(@IdRes selection: Int) {
        searchResult?.let {
            when (selection) {
                R.id.action_author -> adapter.data = mutableSetOf(it.authorsBooks).flatten()
                R.id.action_title -> adapter.data = mutableSetOf(it.titleBooks).flatten()
                R.id.action_category -> adapter.data = mutableSetOf(it.categoryBooks).flatten()
                else -> adapter.data = mutableSetOf(it.authorsBooks, it.titleBooks, it.categoryBooks).flatten().sortedBy { it.title }
            }
        }

    }
}