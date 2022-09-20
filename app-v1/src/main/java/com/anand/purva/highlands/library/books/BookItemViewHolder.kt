package com.anand.purva.highlands.library.books

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anand.purva.highlands.library.R

class BookItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val bookName: TextView = itemView.findViewById(R.id.book_name)
    val bookAuthor: TextView = itemView.findViewById(R.id.book_author)
    val bookCategory: TextView = itemView.findViewById(R.id.book_category)
}