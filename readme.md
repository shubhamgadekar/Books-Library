# 📚 Book Explorer App

An Android app built with **Jetpack Compose** that lets you explore books using the **Google Books API / OpenLibrary API**.  
This project is designed to practice and demonstrate modern Android development concepts like **MVVM, Clean Architecture, Hilt, Flows, RoomDB, Paging 3, Navigation, and Unit Testing**.

---

## ✅ Build & Quality Status
![Detekt](https://github.com/shubhamgadekar/Books-Library/actions/workflows/detekt.yml/badge.svg)
![Unit Tests & Coverage](https://github.com/shubhamgadekar/Books-Library/actions/workflows/unitTests.yml/badge.svg)
[![codecov](https://codecov.io/gh/shubhamgadekar/Books-Library/branch/main/graph/badge.svg)](https://codecov.io/gh/shubhamgadekar/Books-Library)

This repository enforces **static analysis (Detekt)** and **unit test coverage checks** using GitHub Actions.

---

## 📸 Screenshots

<table>
  <tr>
    <td><img alt="Home Screen" src="https://github.com/shubhamgadekar/Books-Library/blob/main/Books-Home.png" height="300"/></td>
    <td><img alt="Home Screen" src="https://github.com/shubhamgadekar/Books-Library/blob/main/Books-Details.png" height="300"/></td>
    <td><img alt="Home Screen" src="https://github.com/shubhamgadekar/Books-Library/blob/main/Books-Search.png" height="300"/></td>
  </tr>
<tr>
<td><img alt="Home Screen" src="https://github.com/shubhamgadekar/Books-Library/blob/main/Books-Wishlist.png" height="300"/></td>
    <td><img alt="Home Screen" src="https://github.com/shubhamgadekar/Books-Library/blob/main/Books-Profile.png" height="300"/></td>
</tr>
</table>

---
## ✨ Features
- 🔎 Search books by keywords
- 📖 View detailed book information (title, author, description, cover image)
- ❤️ Save books to favorites/readList (stored locally with RoomDB)
- 📑 Pagination with infinite scroll using Paging 3
- 👤 Simple profile screen

---

## 🛠️ Tech Stack
- **UI:** Jetpack Compose (Material 3, Navigation Compose)
- **Architecture:** MVVM + Clean Architecture
- **Asynchronous:** Kotlin Coroutines, Flow
- **Dependency Injection:** Hilt
- **Networking:** Retrofit + OkHttp
- **Local Storage:** Room Database
- **Pagination:** Paging 3
- **Testing:** JUnit, MockK, Turbine
- **Static Analysis:** Detekt

---

## 🏗️ Project Structure

```plaintext
com.alpha.books_explorer/ 
│ 
├── data/                           # Data Layer (API + DB) 
│   ├── local/                      # Room database 
│   │   ├── converters/ 
│   │   │   ├── Converters.kt 
│   │   ├── dao/ 
│   │   │   ├── FavBookDao.kt 
│   │   │   ├── ReadingList.kt 
│   │   ├── entities/ 
│   │   │   ├── BookEntity.kt 
│   │   │   ├── ReadingListEntity.kt 
│   │   └── FavBookDatabase.kt 
│   │ 
│   ├── paging/  
│   │   ├── BooksPagingSource.kt 
│   ├── remote/                     # Retrofit API 
│   │   ├── BookApiService.kt 
│   │   └── dto/ 
│   │       ├── BookSearchResponse.kt 
│   │ 
│   ├── repository/                 # Repository implementation 
│   │   └── BookRepositoryImpl.kt 
│   │ 
│   └── mappers/                    # DTO ↔ Entity ↔ Domain 
│       ├── BookMapper.kt 
│ 
├── domain/                         # Domain Layer (business logic) 
│   ├── model/ 
│   │   ├── Book.kt 
│   │ 
│   ├── repository/                 # Abstract repository interfaces 
│   │   └── BookRepository.kt 
│   │ 
│   └── usecase/                    # Use cases 
│       ├── GetBooksUseCase.kt 
│       ├── SearchBooksUseCase.kt 
│       ├── GetBookDetailsUseCase.kt 
│       ├── SaveFavoriteBookUseCase.kt 
│       └── GetFavoriteBooksUseCase.kt 
│ 
├── di/                             # Dependency Injection (Hilt) 
│   ├── LocalDbModule.kt 
│   ├── NetworkModule.kt 
│ 
├── presentation/                   # Presentation Layer 
│   ├── ui/                         # Compose UI 
│   │   ├── home/ 
│   │   │   ├── HomeScreen.kt 
│   │   │   ├── HomeViewModel.kt 
│   │   │   └── HomeUiState.kt 
│   │   ├── search/ 
│   │   │   ├── SearchScreen.kt 
│   │   │   ├── SearchViewModel.kt 
│   │   │   └── SearchUiState.kt 
│   │   ├── details/ 
│   │   │   ├── BookDetailScreen.kt 
│   │   │   ├── BookDetailViewModel.kt 
│   │   │   └── BookDetailUiState.kt 
│   │   ├── favorites/ 
│   │   │   ├── FavoritesScreen.kt 
│   │   │   ├── FavoritesViewModel.kt 
│   │   │   └── FavoritesUiState.kt 
│   │   └── profile/ 
│   │       ├── ProfileScreen.kt 
│   │       ├── ProfileViewModel.kt 
│   │       └── ProfileUiState.kt 
│   │ 
│   │ 
│   └── navigation/ 
│       └── NavGraph.kt 
│ 
├── common/                         # Common utilities & helpers 
│   ├── constants/ 
│   │   └── ApiConstants.kt 
│   ├── utils/ 
│   │   ├── NetworkResult.kt        # Sealed class for Success/Error/Loading 
│   │   ├── Extensions.kt           # Common extension functions 
│   │   └── DispatcherProvider.kt   # For coroutines testability 
│ 
├── MainActivity.kt                  # Host Compose + Navigation 
└── BooksExplorerApplication.kt               # Application class (Hilt)  

```


## 🔌 API Reference
 - **Google Books API** → https://developers.google.com/books/docs/v1/using

    **Example request:** https://www.googleapis.com/books/v1/volumes?q=android

## 📚 Learning Purpose
This app was built to cover:
* Jetpack Compose UI + Navigation
* RoomDB 
* Coroutines + Flows 
* Hilt Dependency Injection
* MVVM + Clean Architecture
* Retrofit Networking
* Paging 3
* Writing Unit Tests + enforcing coverage thresholds
* Enforcing code quality with Detekt

---

## 👨‍💻 Author
**Shubham Gadekar**
- GitHub: [shubhamgadekar](https://github.com/shubhamgadekar)  
