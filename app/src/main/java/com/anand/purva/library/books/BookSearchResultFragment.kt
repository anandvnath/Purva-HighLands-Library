package com.anand.purva.library.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.anand.purva.library.R
import com.anand.purva.library.databinding.FragmentFirstBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BookSearchResultFragment : Fragment() {
    private val viewModel: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)
        val adapter = BookViewAdapter()
        binding.searchResultsView.adapter = adapter
        binding.searchResultsView.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
        viewModel.searchResult.observe(viewLifecycleOwner, Observer { searchResult ->
            adapter.data = mutableSetOf(searchResult.authorsBooks, searchResult.titleBooks).flatten()
        })
    }
}