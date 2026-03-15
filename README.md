# EcoStay Retreat 🌿

An eco-friendly resort booking Android application that allows guests to explore sustainable accommodations, book eco-adventures, and manage their stay seamlessly.

---

## ✨ Features

### Guest Features
- User authentication (register/login)
- Browse and filter eco-friendly rooms
- Book rooms with date selection
- Browse and book eco-activities with availability checking
- View personal booking history
- Receive notifications
- Explore eco-events and offers
- Manage profile preferences

### Admin Features
- Admin dashboard
- Manage rooms (add, edit, delete)
- Manage activities (add, edit, delete)
- View all user bookings
- Send notifications to all users
- Manage user accounts
- Manage notifications (edit, delete)

---

## 🛠️ Technology Stack

- **Language:** Java
- **Framework:** Android Native SDK
- **Database:** SQLite
- **Architecture:** Fragment-based with Navigation Drawer
- **UI Components:** RecyclerView, CardView, ConstraintLayout
- **Build Tool:** Gradle
- **Minimum SDK** API 24 (Android 7.0)
- **Target SDK** API 34 (Android 14)

---

## 📋 Prerequisites

- Android Studio (Giraffe or newer)
- Minimum SDK: API 24 (Android 7.0)
- Target SDK: API 34

---

## 🚀 Installation

1. Clone the repository
   ```bash
   git clone https://github.com/Prabath397/EcoStay-Retreat-AndroidApplication.git
   ```
2. Open project in Android Studio

3. Let Gradle sync complete

4. Run on emulator or physical device

---

## 📱 Admin Login

- Email: admin@ecostay.com
- Password: admin123

---

## 📁 Project Structure

```
EcoStay/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/ecostay/
│   │   │   │   ├── activities/        # Activity classes
│   │   │   │   ├── fragments/         # Fragment classes
│   │   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   │   ├── models/            # Data model classes
│   │   │   │   └── database/           # DBHelper.java
│   │   │   └── res/
│   │   │       ├── layout/             # XML layout files
│   │   │       ├── drawable/           # Images and icons
│   │   │       └── values/             # strings, colors, styles
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── README.md
└── build.gradle
```
---

## 🗄️ Database Schema
The app uses SQLite with 6 tables:

- users - User accounts and admin status
- rooms - Eco-friendly room details
- activities - Activity information
- bookings - Room reservations
- activity_bookings - Activity reservations
- notifications - User notifications

---

## 🔒 Security Features

- Password validation (minimum 6 characters)
- Role-based access control (guest vs admin)
- Admin protection (cannot delete admin users)
- Confirmation dialogs for destructive actions
- Input validation for all forms

---

## 📝 License

This project is created for educational purposes as part of an academic assignment at ICBT Campus.

---

## 👤 Author

**Prabath Jayasuriya**  
Mobile Application Development – CSE5011
**Academic Year:** 2026, Semester 3