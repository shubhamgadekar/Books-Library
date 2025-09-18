//package com.alpha.domain.usecase.readingList
//
//import com.alpha.domain.model.Book
//import com.alpha.domain.repository.BookRepository
//import javax.inject.Inject
//import kotlinx.coroutines.flow.Flow
//
//class IsBookPresentInReadingListUseCase
//@Inject
//constructor(
//    private val repository: BookRepository,
//) {
//    fun invoke(book: Book): Flow<Boolean> {
//        return repository.isBookPresentInReadingList(book)
//    }
//}
