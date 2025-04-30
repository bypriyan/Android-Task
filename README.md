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
