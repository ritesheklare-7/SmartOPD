# 🏥 SmartOPD — Automated Clinic Token & Availability Management

> **Queue Less, Care More** — Eliminating OPD chaos with real-time digital token management

---

## 📌 Problem Statement

OPD departments in clinics and hospitals rely on manual token systems causing:
- Long queues and overcrowding in waiting areas
- No real-time visibility into doctor availability
- Poor handling of doctor leave leading to appointment cancellations
- Patient dissatisfaction due to lack of updates and communication

---

## ✅ Our Solution

**SmartOPD** is an Android application that automates OPD token allocation, manages doctor availability in real-time, and sends instant alerts to patients when schedules change — all from a clean, easy-to-use mobile interface.

---

## 🚀 Key Features

### 👨‍⚕️ Doctor Module
- 🔘 **Availability Toggle** — Mark yourself available or on leave instantly
- 🎫 **Live Token Dashboard** — See how many tokens are issued vs remaining
- 👥 **Waiting Patient List** — Live list of all waiting patients with token numbers
- 🔴 **Mark Session Full** — Stop new token generation with one tap
- 📅 **Planned Leave** — Pick a date, patients notified automatically
- 🚨 **Emergency Leave** — Instant toggle cancels all tokens and alerts patients
- 🔧 **Session Capacity** — Set max tokens and Morning/Evening time slots
- 🔒 **Logout** — Secure sign-out with Firebase Auth session clearing

### 🧑‍💼 Patient Module
- 🔍 **Find Doctor** — See all available doctors with live status indicators
- ⚡ **One Tap Token** — Book a token instantly with confirmation dialog
- 📊 **Live Queue Tracker** — See your token number, current serving number, and people ahead
- ⏱ **Estimated Wait Time** — Calculated in real-time (people ahead × 5 mins)
- 🔔 **Smart Notifications** — Leave alerts, cancellation notices, "You're next!" alerts
- 🟢🔴🟠 **Doctor Status** — Available / On Leave / Session Full shown clearly

### 🔐 Auth Module
- 📝 **Registration** — Register as Patient or Doctor with role toggle
- 🔑 **Role-Based Login** — Automatically redirects to correct module based on role
- 🔄 **Auto Login** — Remembers session, skips login if already signed in
- 🚪 **Secure Logout** — Clears back stack, cannot return without login

---

## 📱 App Screens

### Auth Flow
| Screen | Description |
|--------|-------------|
| Splash | App intro with logo animation |
| Login | Email + password login with Firebase Auth |
| Registration | Register as Patient or Doctor with role toggle |

### Doctor Module (3 Fragments)
| Fragment | Description |
|----------|-------------|
| Dashboard | Availability toggle, token count, waiting patient list, mark session full |
| Session | Set max tokens per session, choose Morning/Evening slot, reset session |
| Leave | Emergency leave toggle, planned leave date picker, logout |

### Patient Module (3 Fragments)
| Fragment | Description |
|----------|-------------|
| Find Doctor | Live doctor list with status, get token button |
| My Token | Big token number display, live queue tracker, wait time |
| Notifications | Leave alerts, cancellation notices, "your turn" alerts |

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| Platform | Android (Java) |
| UI | ConstraintLayout + Material Design Components |
| Auth | Firebase Authentication |
| Database | Firebase Realtime Database |
| User Data | Firebase Firestore |
| Notifications | Firebase Cloud Messaging (FCM) |
| Architecture | Single Activity + Multiple Fragments |

---

## 🔥 Firebase Structure

```
SmartOPD/
│
├── doctors/
│   └── {doctorId}/
│       ├── name              : "Dr. Raj Sharma"
│       ├── specialization    : "General Physician"
│       ├── isAvailable       : true / false
│       ├── sessionFull       : true / false
│       ├── emergencyLeave    : true / false
│       ├── maxTokens         : 40
│       ├── currentToken      : 23
│       ├── timeSlot          : "Morning" / "Evening"
│       └── plannedLeave      : "DD/MM/YYYY"
│
├── tokens/
│   └── {tokenId}/
│       ├── patientName       : "Rahul Sharma"
│       ├── phone             : "9876543210"
│       ├── patientId         : "uid_xyz"
│       ├── doctorId          : "doctor_001"
│       ├── doctorName        : "Raj Sharma"
│       ├── tokenNumber       : "23"
│       ├── status            : "waiting" / "done" / "cancelled"
│       ├── timeSlot          : "Morning"
│       └── timestamp         : 1700000000000
│
└── notifications/
    └── {patientId}/
        └── {notifId}/
            ├── title         : "Emergency Leave"
            ├── message       : "Your token has been cancelled"
            ├── timestamp     : 1700000000000
            └── read          : false
```

### Firestore Structure (User Profiles)
```
users/
└── {uid}/
    ├── uid       : "firebase_uid"
    ├── name      : "Rahul Sharma"
    ├── email     : "rahul@email.com"
    ├── phone     : "9876543210"
    ├── role      : "patient" / "doctor"
    └── createdAt : 1700000000000
```

---

## 📽️ Demo Flow

1. **Doctor registers** → selects "Doctor" toggle → logs in
2. **Doctor sets** session capacity (40 tokens), Morning slot
3. **Doctor toggles** Available → status turns green
4. **Patient registers** → selects "Patient" toggle → logs in
5. **Patient opens** Find Doctor tab → sees doctor as Available
6. **Patient taps** "Get Token" → confirms → gets Token #1
7. **Queue advances** → Patient sees "Almost Your Turn!" in Notifications
8. **Doctor marks** Emergency Leave → Patient instantly gets cancellation alert
9. **Doctor logs out** → cannot go back without re-login

---

## 🗂️ Project Structure

```
app/
├── DoctorModule/
│   ├── DoctorHomeActivity.java
│   ├── Fragments/
│   │   ├── DashboardFragment.java
│   │   ├── SessionFragment.java
│   │   └── LeaveFragment.java
│   └── Adapters/
│       └── PatientAdapter.java
│
├── PatientModule/
│   ├── PatientHomeActivity.java
│   ├── Fragments/
│   │   ├── FindDoctorFragment.java
│   │   ├── MyTokenFragment.java
│   │   └── NotificationsFragment.java
│   └── Adapters/
│       └── DoctorAdapter.java
│
├── models/
│   ├── Doctor.java
│   └── Patient.java
│
├── utils/
│   └── FirebaseHelper.java
│
├── LoginActivity.java
├── RegistrationActivity.java
└── SplashActivity.java
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/SmartOPD.git
cd SmartOPD
```

### 2. Connect Firebase
- Go to [Firebase Console](https://console.firebase.google.com)
- Create project → Register Android app with package `com.app.smartopd`
- Download `google-services.json` → paste in `app/` folder
- Enable: **Realtime Database**, **Firestore**, **Authentication**
- Set Realtime Database rules to test mode

### 3. Add Dependencies (`build.gradle app level`)
```gradle
implementation platform('com.google.firebase:firebase-bom:32.7.0')
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-firestore'
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-messaging'
```

### 4. Run the App
- Open in Android Studio
- Click **Run** or press `Shift + F10`

---

## 👨‍💻 Team

| Name | Role |
|------|------|
| [Your Name] | Android Developer — Doctor Module |
| [Name] | Android Developer — Patient Module |
| [Name] | Firebase & Backend Integration |
| [Name] | UI/UX Design |

---

## 🏆 Built At

**[Hackathon Name]** — [Date] | [Location]

---

## 🎯 Impact

- ✅ **Zero manual work** — tokens auto-generate and auto-expire
- ✅ **Real-time queue** — patients wait from home, not crowded halls
- ✅ **Instant leave handling** — no patient shows up for cancelled doctor
- ✅ **Role-based access** — clean separation of doctor and patient experience
- ✅ **Scalable** — works for any clinic, any number of doctors

---

## 📄 License

MIT License — Free to use and modify

---

<p align="center">
  Built with ❤️ for better healthcare
</p>