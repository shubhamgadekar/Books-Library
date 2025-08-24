# 📚 Book Explorer App

An Android app built with **Jetpack Compose** that lets you explore books using the **Google Books API / OpenLibrary API**.  
This project is designed to practice and demonstrate modern Android development concepts like **MVVM, Clean Architecture, Hilt, Flows, RoomDB, Paging 3, Navigation, and Unit Testing**.

---

## ✨ Features
- 🔎 Search books by title, author, or keyword
- 📖 View detailed book information (title, author, description, cover image)
- ❤️ Save books to favorites (stored locally with RoomDB)
- 🔄 Offline support with cached data
- 🌗 Dark/Light theme with Material 3
- 📑 Pagination with infinite scroll using Paging 3
- 👤 Simple profile screen (LiveData + DataStore)

---

## 🛠️ Tech Stack
- **UI:** Jetpack Compose (Material 3, Navigation Compose)
- **Architecture:** MVVM + Clean Architecture
- **Asynchronous:** Kotlin Coroutines, Flow, LiveData
- **Dependency Injection:** Hilt
- **Networking:** Retrofit + OkHttp
- **Local Storage:** Room Database (with relations)
- **Pagination:** Paging 3 (API + Room cache)
- **Patterns:** Factory Method, Builder Pattern
- **Testing:** JUnit, Turbine (Flow testing), MockK

---

## 🏗️ Project Structure

```plaintext
com.example.bookexplorer/
│
├── data/               # Data sources (API, DB, Paging)
├── domain/             # Business logic (UseCases, Models, Repositories)
├── di/                 # Hilt modules for dependencies
├── presentation/       # UI layer (Compose screens, ViewModels, NavGraph)
├── common/             # Utilities, constants, patterns 

```


## 🔌 API Reference
 - **Google Books API** → https://developers.google.com/books/docs/v1/using

    **Example request:** https://www.googleapis.com/books/v1/volumes?q=android

## 📚 Learning Purpose
This app was built to cover:
* Jetpack Compose UI + Navigation
* RoomDB with relations
* Coroutines + Flows vs LiveData
* Hilt Dependency Injection
* MVVM + Clean Architecture
* Retrofit Networking
* Paging 3 (with RemoteMediator)
* Unit Testing (DAO, Repository, ViewModel)
* Factory & Builder Patterns