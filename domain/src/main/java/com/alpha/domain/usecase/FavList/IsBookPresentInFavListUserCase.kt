//package com.alpha.domain.usecase.FavList

//import com.alpha.domain.model.Book
//import com.alpha.domain.repository.BookRepository
//import javax.inject.Inject
//import kotlinx.coroutines.flow.Flow
//
//class IsBookPresentInFavListUserCase
//@Inject
//constructor(
//    private val localDbRepository: BookRepository,
//) {
//    fun invoke(book: Book): Flow<Boolean> {
//        return localDbRepository.isBookPresentInFavList(book)
//    }
//}
