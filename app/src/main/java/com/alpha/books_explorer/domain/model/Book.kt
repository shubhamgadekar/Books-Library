package com.alpha.books_explorer.domain.model


data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
)

data class VolumeInfo(
    val title: String?,
    val subtitle: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
)

data class ImageLinks(
    val thumbnail: String?
)
