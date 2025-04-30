# 📍 MyPlaces App

A Kotlin-based Android application to manage and explore your favorite places on a map. Built using modern Android development practices and robust architecture components.

---

## ✨ Features

- 🔐 **Authentication**
  - Firebase Email/Password login and signup
  - Session persistence using DataStore
  - Upload and display user profile image

- 🧭 **Add Place**
  - Select location using Google Maps
  - Add place name, description, and attach a photo
  - Image compression and upload to Firebase Storage
  - Store details in Firebase Firestore and local Room Database

- 🗺️ **Map View**
  - View all places with custom markers (thumbnail preview)
  - Marker click shows detailed info
  - Syncs from Room for offline support

- 🔄 **Offline and Sync**
  - Room Database for offline storage
  - Firebase Firestore for cloud sync
  - Auto-cache user profile and places data using DataStore

- 🧹 **User Profile**
  - Display user name, email, and profile image
  - Cached using DataStore

- 🚪 **Logout**
  - Confirmation dialog before logout
  - Clears DataStore and navigates to Auth screen

---

## 🛠️ Built With

- Kotlin + MVVM Architecture
- Firebase Authentication, Firestore, and Storage
- Google Maps SDK
- Room Database
- DataStore (Proto)
- Dagger Hilt for Dependency Injection
- Coroutines + Flow

---

## 📽️ Demo Video

[▶️ Watch App Demo](https://github.com/user-attachments/assets/4c917d31-14ac-4fd2-b86c-7c5925a29885)

---

## 🖼️ Screenshots

| Login | Signup | Home | Add Place |
|-------|--------|------|-----------|
| ![Login](https://github.com/user-attachments/assets/6043af57-cfd9-44fc-8a56-ee0e418c75f9) | ![Signup](https://github.com/user-attachments/assets/3aa5a7b4-5db7-4527-9296-8a4c9353e9f4) | ![Home](https://github.com/user-attachments/assets/b2b82dd7-3ea5-48a1-b904-e3650512e968) | ![Add](https://github.com/user-attachments/assets/edc32525-96b6-44e4-a65f-5238151937f2) |

| Profile | Places | Logout |
|---------|--------|--------|
| ![Profile](https://github.com/user-attachments/assets/994fc560-b722-4255-b8ee-1e2f2e261162) | ![Places](https://github.com/user-attachments/assets/e367fa3f-c2d7-4819-b0ab-fba5edaaccd9) | ![Logout](https://github.com/user-attachments/assets/28bb1702-7a25-45b0-ac24-a377e06b4a08) |

---

## ⚙️ Getting Started

To get started with the app, follow these steps:

### 🔧 Prerequisites

- Android Studio Flamingo or higher
- Firebase Project setup (Firestore, Auth, Storage)
- Google Maps API Key

---

### 1. Clone the Repository

```bash
git clone https://github.com/bypriyan/Android-Task.git
cd Android-Task
