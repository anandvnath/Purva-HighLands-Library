package com.anand.purva.highlands.library

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val bookName: TextView = itemView.findViewById(R.id.book_name)
    val bookAuthor: TextView = itemView.findViewById(R.id.book_author)
    val bookCategory: TextView = itemView.findViewById(R.id.book_category)
}