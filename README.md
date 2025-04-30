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
![1](https://github.com/user-attachments/assets/6043af57-cfd9-44fc-8a56-ee0e418c75f9)
![2](https://github.com/user-attachments/assets/3aa5a7b4-5db7-4527-9296-8a4c9353e9f4)
![3](https://github.com/user-attachments/assets/b2b82dd7-3ea5-48a1-b904-e3650512e968)
![4](https://github.com/user-attachments/assets/994fc560-b722-4255-b8ee-1e2f2e261162)
![5](https://github.com/user-attachments/assets/edc32525-96b6-44e4-a65f-5238151937f2)
![6](https://github.com/user-attachments/assets/e367fa3f-c2d7-4819-b0ab-fba5edaaccd9)
![8](https://github.com/user-attachments/assets/28bb1702-7a25-45b0-ac24-a377e06b4a08)

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
