package com.alpha.books_explorer.data.mappers

import com.alpha.books_explorer.data.local.entities.BookEntity
import com.alpha.books_explorer.data.local.entities.ReadingListEntity
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.ImageLinks
import com.alpha.books_explorer.domain.model.VolumeInfo

fun BookEntity.toBook(): Book {
    val image = ImageLinks(thumbnail = thumbnail)
    val volume =
        VolumeInfo(
            title = title,
            subtitle = subtitle,
            authors = authors,
            publisher = publisher,
            publishedDate = publishedDate,
            description = description,
            imageLinks = image,
        )
    return Book(
        id = id,
        volumeInfo = volume,
    )
}

fun ReadingListEntity.toBook(): Book {
    val image = ImageLinks(thumbnail = thumbnail)
    val volume =
        VolumeInfo(
            title = title,
            subtitle = subtitle,
            authors = authors,
            publisher = publisher,
            publishedDate = publishedDate,
            description = description,
            imageLinks = image,
        )
    return Book(
        id = id,
        volumeInfo = volume,
    )
}

fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = this.id.toString(),
        title = this.volumeInfo?.title,
        subtitle = this.volumeInfo?.subtitle,
        authors = this.volumeInfo?.authors,
        publisher = this.volumeInfo?.publisher,
        publishedDate = this.volumeInfo?.publishedDate,
        description = this.volumeInfo?.description,
        thumbnail = this.volumeInfo?.imageLinks?.thumbnail,
    )
}

fun Book.toReadingListEntity(): ReadingListEntity {
    return ReadingListEntity(
        id = this.id.toString(),
        title = this.volumeInfo?.title,
        subtitle = this.volumeInfo?.subtitle,
        authors = this.volumeInfo?.authors,
        publisher = this.volumeInfo?.publisher,
        publishedDate = this.volumeInfo?.publishedDate,
        description = this.volumeInfo?.description,
        thumbnail = this.volumeInfo?.imageLinks?.thumbnail,
    )
}
