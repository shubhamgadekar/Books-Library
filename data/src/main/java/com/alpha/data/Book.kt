package com.alpha.data

internal data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
)

internal data class VolumeInfo(
    val title: String?,
    val subtitle: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val imageLinks: ImageLinks?,
)

internal data class ImageLinks(
    val thumbnail: String?,
)
