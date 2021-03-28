package com.anand.purva.highlands.library.books.data

data class Book(val id: String, val title: String, val author: String, val category: String)

enum class BookAttribute { AUTHOR, TITLE, CATEGORY }