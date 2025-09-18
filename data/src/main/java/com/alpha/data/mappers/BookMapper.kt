//package com.alpha.data.mappers
//
//import com.alpha.data.local.entities.BookEntity
//import com.alpha.data.local.entities.ReadingListEntity
//import com.alpha.domain.model.Book
//import com.alpha.domain.model.ImageLinks
//import com.alpha.domain.model.VolumeInfo
//
//internal fun BookEntity.toBook(): Book {
//    val image = ImageLinks(thumbnail = thumbnail)
//    val volume =
//        VolumeInfo(
//            title = title,
//            subtitle = subtitle,
//            authors = authors,
//            publisher = publisher,
//            publishedDate = publishedDate,
//            description = description,
//            imageLinks = image,
//        )
//    return Book(
//        id = id,
//        volumeInfo = volume,
//    )
//}
//
//internal fun ReadingListEntity.toBook(): Book {
//    val image = ImageLinks(thumbnail = thumbnail)
//    val volume =
//        VolumeInfo(
//            title = title,
//            subtitle = subtitle,
//            authors = authors,
//            publisher = publisher,
//            publishedDate = publishedDate,
//            description = description,
//            imageLinks = image,
//        )
//    return Book(
//        id = id,
//        volumeInfo = volume,
//    )
//}
//
//internal fun Book.toBookEntity(): BookEntity {
//    return BookEntity(
//        id = this.id.toString(),
//        title = this.volumeInfo?.title,
//        subtitle = this.volumeInfo?.subtitle,
//        authors = this.volumeInfo?.authors,
//        publisher = this.volumeInfo?.publisher,
//        publishedDate = this.volumeInfo?.publishedDate,
//        description = this.volumeInfo?.description,
//        thumbnail = this.volumeInfo?.imageLinks?.thumbnail,
//    )
//}
//
//internal fun Book.toReadingListEntity(): ReadingListEntity {
//    return ReadingListEntity(
//        id = this.id.toString(),
//        title = this.volumeInfo?.title,
//        subtitle = this.volumeInfo?.subtitle,
//        authors = this.volumeInfo?.authors,
//        publisher = this.volumeInfo?.publisher,
//        publishedDate = this.volumeInfo?.publishedDate,
//        description = this.volumeInfo?.description,
//        thumbnail = this.volumeInfo?.imageLinks?.thumbnail,
//    )
//}
