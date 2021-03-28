package com.anand.purva.highlands.library.books

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anand.purva.highlands.library.BookItemViewHolder
import com.anand.purva.highlands.library.R
import com.anand.purva.highlands.library.books.data.Book

class BookViewAdapter : RecyclerView.Adapter<BookItemViewHolder>() {
    var data = listOf<Book>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.book_item_view, parent, false)
        return BookItemViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        val item = data[position]
        holder.bookAuthor.text = item.author
        holder.bookName.text = item.title
        holder.bookCategory.text = item.category
    }
}