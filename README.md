# 📍 MyPlaces App

A Kotlin-based Android application to manage and explore your favorite places on a map. Supports image uploads, offline access, Firebase authentication, and real-time syncing with Firestore.

---

## ✨ Features

- 🔐 **Authentication**
  - Firebase Email/Password login and registration
  - Auto-login with saved session using DataStore

- 🧭 **Add Place**
  - Pick location from interactive Google Map
  - Add name, description, and photo
  - Upload image to Firebase Storage with compression
  - Save place to Firestore and Room (offline support)

- 🗺️ **Map View**
  - Display all added places as custom markers with image thumbnails
  - Real-time data loading from Room

- 🔄 **Data Sync**
  - Firebase Firestore for online sync
  - Room Database for offline access
  - Auto-fetch user details from Firestore and cache locally

- 🧹 **User Profile**
  - Upload profile image
  - Store user info in Firestore
  - Load and cache user profile using DataStore

- 🚪 **Logout**
  - Logout option with confirmation alert dialog
  - Redirect to authentication screen

## 📽️ App Demo

https://github.com/user-attachments/assets/4c917d31-14ac-4fd2-b86c-7c5925a29885

## 🖼️ Screenshots
![1](https://github.com/user-attachments/assets/202960e5-30e4-4358-9dd5-7b8207b6fdfb)
![2](https://github.com/user-attachments/assets/43d0b2a3-9c08-418b-866e-dd21ccb141e4)
![3](https://github.com/user-attachments/assets/410fb90d-6e5c-4a12-ba01-ca22ad8d2b8a)
![4](https://github.com/user-attachments/assets/eac409c2-617a-4615-b053-7cedd169f502)
![5](https://github.com/user-attachments/assets/9bc10a56-926c-4e8e-a1a7-87835ceff5e2)
![6](https://github.com/user-attachments/assets/1a563986-2fd7-49d1-b8d9-1ad8e837612d)
![8](https://github.com/user-attachments/assets/c6f12406-9ee2-4374-ba91-71e2ac83a7ba)

---

## ⚙️ Setup Instructions

### 🔧 Prerequisites

- Android Studio Flamingo or higher
- Firebase Project
- Google Maps API Key

### 1. Clone this Repository

```bash
git clone https://github.com/yourusername/MyPlacesApp.git
cd MyPlacesApp
