
# RescueNowApp - A Real-Time Emergency Alert System

**RescueNowApp** is an Android emergency alert application designed to help users—especially in rural and urban areas—quickly send emergency alerts along with their live location via SMS and Firebase. The app can be triggered using on-screen emergency buttons or volume keys, and supports notifications to essential services like Police, Ambulance, Fire, or a saved emergency contact. It is designed to work reliably even in low-connectivity regions.

---

## Features

- Real-time GPS Location Sharing
- Emergency Triggers for:
  - Police
  - Ambulance
  - Fire Service
  - Contact Person (Custom)
- Emergency SMS with Google Maps Location Link
- Firebase Integration for Alert Logging
- Volume Button Trigger (Background Service)
- Supports Android 13, 14, and 15 (API 33–34)
- Runtime Permissions Handling (SMS, Location, Notifications)

---

## Technologies Used

- Kotlin
- Android Studio (AppCompat + XML UI)
- Firebase Realtime Database / Cloud Messaging
- Android Location Services
- Foreground Services
- SMS Manager

---

## Getting Started

### Prerequisites

- Android Studio (Giraffe, Hedgehog, or Meerkat recommended)
- Android SDK 33–34
- Firebase project setup (optional for full features)

### Steps

1. **Clone the Repo**
   ```bash
   git clone https://github.com/swayamprakashm/RescueNowApp.git


2. **Open in Android Studio**

3. **Run on Device or Emulator**

---

## Permissions Required

Make sure the app requests and grants the following permissions:

* `ACCESS_FINE_LOCATION`
* `SEND_SMS`
* `POST_NOTIFICATIONS` (Android 13+)
* `FOREGROUND_SERVICE_LOCATION` (Android 14+)

---

## Screenshots

### App Logo:
<img src="Images/ic_app_logo.webp" alt="RescueNowApp" width="120"/>

### App Home Screen:
<img src="Images/Output5.jpg" alt="App HomeScreen" width="350"/>

### Dialer Screens via App (101, 100, 108):
<img src="Images/Output1.jpg" alt="Dialer 101" width="300"/> <img src="Images/Output2.jpg" alt="Dialer 100" width="300"/> <img src="Images/Output4.jpg" alt="Dialer 108" width="300"/>

### App Message Output:
<img src="Images/Output3.png" alt="Dialer Message" width="350"/>

### App Map Layout:
<img src="Images/Output6.jpg" alt="Map Output" width="350"/>

---

## Future Enhancements

* Emergency alert history
* Voice command activation
* Local language support
* SOS signal with siren

---

## Developed By

**M. Swayam Prakash**

Github: [https://github.com/swayamprakashm](https://github.com/swayamprakashm)

---

## License

This project is licensed under the MIT License. 
