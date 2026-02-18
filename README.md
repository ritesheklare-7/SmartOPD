# 🏥 SmartOPD — Automated Clinic Token & Availability Management

> Say goodbye to OPD queues. Smart tokens. Real-time tracking. Instant alerts.

---

## 📌 Problem Statement
OPD departments rely on manual token systems causing:
- Long queues & overcrowding
- No real-time doctor availability tracking
- Poor handling of doctor leave & appointment cancellations

---

## ✅ Solution
**SmartOPD** is an Android application that automates OPD token allocation,
manages doctor availability in real-time, and sends instant alerts to patients
when schedules change.

---

## 🚀 Key Features
- 🎫 Dynamic token generation based on doctor session capacity
- 📊 Live queue dashboard — track your turn from anywhere
- 🩺 Doctor availability & leave management panel
- 🔔 Instant push notifications for leave alerts & turn updates
- ⚡ Real-time sync via Firebase

---

## 👥 User Roles
| Role | Access |
|------|--------|
| 🧑‍💼 Patient | Get token, live queue tracking, notifications |
| 👨‍⚕️ Doctor/Admin | Set availability, manage capacity, mark leave |

---

## 🛠️ Tech Stack
| Layer | Technology |
|-------|------------|
| Platform | Android (Java / Kotlin) |
| Backend | Firebase Realtime Database |
| Authentication | Firebase Auth |
| Notifications | Firebase Cloud Messaging (FCM) |
| UI | Material Design Components |

---

## 📱 App Screens
### Patient Side
- Login / Register
- Home — View available doctors & live token status
- Get Token — One tap token generation
- My Token — Live queue tracker with estimated wait time
- Notifications — Leave alerts & turn updates

### Admin / Doctor Side
- Doctor Dashboard — Toggle availability, view queue
- Session Setup — Set max tokens & time slots
- Leave Management — Mark planned or emergency leave

---

## 🔥 Firebase Structure
```
SmartOPD/
├── doctors/
│   └── doctorId/
│       ├── name, specialization
│       ├── isAvailable: true/false
│       ├── maxTokens: 40
│       └── currentToken: 23
├── tokens/
│   └── tokenId/
│       ├── patientName, phone
│       ├── doctorId, tokenNumber
│       └── status: waiting/done
└── notifications/
    └── patientId/ → messages[]
```

---

## 📽️ Demo Flow
1. Admin sets doctor availability & max tokens (40)
2. Patient opens app → sees live doctor status → gets Token #1
3. Queue advances → Patient gets "You're next!" notification
4. Doctor marks emergency leave → all patients get instant cancellation alert

---

## 👨‍💻 Team
| Name      | Role |

|  Raji     |Frontend developer|
| Chaitanya | Android Developer |
| Sahil     | UI/UX & Firebase |
| Ritesh    | Backend & Notifications |

---

## 🏆 Built At
**[Hackathon Name]** — [Date]

---

## 📄 License
MIT License